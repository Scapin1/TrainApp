package com.tuusuario.trainapp.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.tuusuario.trainapp.data.db.AppDatabase
import com.tuusuario.trainapp.data.db.entities.BodyWeight
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters

class WeighInReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val CHANNEL_ID = "weigh_in_channel"
    private val NOTIFICATION_ID = 1

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val database = AppDatabase.getInstance(applicationContext)
            val bodyWeightDao = database.bodyWeightDao()
            
            // Check if there's a weight entry this week (Monday to Sunday)
            val now = LocalDate.now(ZoneId.systemDefault())
            val weekStart = now.`with`(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY))
            val weekEnd = now.`with`(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY))
            
            val startMillis = weekStart.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            val endMillis = weekEnd.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
            
            val countThisWeek = bodyWeightDao.getCountThisWeek(startMillis, endMillis)
            
            if (countThisWeek == 0) {
                // No weight entry this week - show notification
                showNotification(
                    "⚖️ Recordatorio de pesaje",
                    "Esta semana no has registrado tu peso. ¡Hazlo hoy para mantener el seguimiento!"
                )
            }
            
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private fun showNotification(title: String, text: String) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, "Recordatorio pesaje", NotificationManager.IMPORTANCE_HIGH)
            channel.description = "Recordatorio semanal de pesaje (viernes)"
            channel.enableVibration(true)
            notificationManager.createNotificationChannel(channel)
        }
        
        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_menu_report_image)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(0, 500, 200, 500))
            .build()
        
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}