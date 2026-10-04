package com.tuusuario.trainapp.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.tuusuario.trainapp.data.backup.BackupManager
import com.tuusuario.trainapp.data.db.AppDatabase
import com.tuusuario.trainapp.data.db.entities.BodyWeight
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class AutoBackupWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val CHANNEL_ID = "backup_channel"
    private val NOTIFICATION_ID = 3

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val database = AppDatabase.getInstance(applicationContext)
            val backupManager = BackupManager(database, applicationContext)
            
            val result = backupManager.exportToFile()
            
            if (result is BackupManager.ExportResult.Success) {
                // Cleanup old backups
                backupManager.cleanupOldBackups(30)
                
                // Show silent notification
                showNotification("Backup completado", "Guardado en ${SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())}")
                
                Result.success()
            } else {
                showNotification("Backup fallido", "Error: ${(result as BackupManager.ExportResult.Failure).error}")
                Result.failure()
            }
        } catch (e: Exception) {
            showNotification("Backup fallido", "Excepción: ${e.message}")
            Result.failure()
        }
    }

    private fun showNotification(title: String, text: String) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, "Backup automático", NotificationManager.IMPORTANCE_LOW)
            channel.description = "Notificaciones de backup automático diario"
            notificationManager.createNotificationChannel(channel)
        }
        
        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.stat_sys_download_done)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setAutoCancel(true)
            .build()
        
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}