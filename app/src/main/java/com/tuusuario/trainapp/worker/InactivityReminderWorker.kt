package com.tuusuario.trainapp.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.tuusuario.trainapp.data.db.AppDatabase
import com.tuusuario.trainapp.data.db.entities.Workout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class InactivityReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val CHANNEL_ID = "inactivity_channel"
    private val NOTIFICATION_ID = 2

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val database = AppDatabase.getInstance(applicationContext)
            val workoutDao = database.workoutDao()
            
            // Get inactivity days from settings (default 3)
            val inactivityDays = inputData.getInt("inactivity_days", 3)
            
            val lastWorkout = workoutDao.getLastWorkout()
            
            val shouldNotify = lastWorkout?.let { workout ->
                val lastDate = LocalDate.ofInstant(
                    java.time.Instant.ofEpochMilli(workout.startedAt),
                    ZoneId.systemDefault()
                )
                val daysSince = ChronoUnit.DAYS.between(lastDate, LocalDate.now(ZoneId.systemDefault()))
                daysSince >= inactivityDays
            } ?: true // If no workouts ever, notify
            
            if (shouldNotify) {
                showNotification(
                    "🏋️ Inactividad detectada",
                    "Hace ${inactivityDays}+ días que no entrenas. Tu rutina te espera."
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
            val channel = NotificationChannel(CHANNEL_ID, "Inactividad entrenamiento", NotificationManager.IMPORTANCE_HIGH)
            channel.description = "Recordatorio por inactividad en entrenamientos"
            channel.enableVibration(true)
            notificationManager.createNotificationChannel(channel)
        }
        
        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(0, 500, 200, 500))
            .build()
        
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}