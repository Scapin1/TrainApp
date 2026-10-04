package com.tuusuario.trainapp

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.tuusuario.trainapp.di.AppContainer
import com.tuusuario.trainapp.worker.AutoBackupWorker
import com.tuusuario.trainapp.worker.InactivityReminderWorker
import com.tuusuario.trainapp.worker.WeighInReminderWorker
import java.util.concurrent.TimeUnit

class TrainApp : Application() {

    private var container: AppContainer? = null

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
        scheduleWorkers()
    }

    fun container(): AppContainer = container!!

    companion object {
        @Suppress("UNUSED_PARAMETER")
        fun getInstance(context: Context): TrainApp {
            return context.applicationContext as TrainApp
        }
    }

    private fun scheduleWorkers() {
        val workManager = WorkManager.getInstance(this)

        // Auto backup - daily at 3 AM
        val backupRequest = PeriodicWorkRequest.Builder(AutoBackupWorker::class.java, 24, TimeUnit.HOURS)
            .setInitialDelay(3, TimeUnit.HOURS) // Run at ~3 AM
            .build()
        workManager.enqueueUniquePeriodicWork(
            "auto_backup",
            ExistingPeriodicWorkPolicy.KEEP,
            backupRequest
        )

        // Weigh-in reminder - weekly on Friday 10:00
        val weighInRequest = PeriodicWorkRequest.Builder(WeighInReminderWorker::class.java, 7, TimeUnit.DAYS)
            .setInitialDelay(calculateInitialDelayForFriday10AM(), TimeUnit.MILLISECONDS)
            .build()
        workManager.enqueueUniquePeriodicWork(
            "weigh_in_reminder",
            ExistingPeriodicWorkPolicy.KEEP,
            weighInRequest
        )

        // Inactivity reminder - daily at 19:00
        val inactivityRequest = PeriodicWorkRequest.Builder(InactivityReminderWorker::class.java, 24, TimeUnit.HOURS)
            .setInitialDelay(calculateInitialDelayFor7PM(), TimeUnit.MILLISECONDS)
            .addTag("inactivity_reminder")
            .build()
        workManager.enqueueUniquePeriodicWork(
            "inactivity_reminder",
            ExistingPeriodicWorkPolicy.KEEP,
            inactivityRequest
        )
    }

    private fun calculateInitialDelayForFriday10AM(): Long {
        val now = java.util.Calendar.getInstance()
        val target = java.util.Calendar.getInstance().apply {
            set(java.util.Calendar.DAY_OF_WEEK, java.util.Calendar.FRIDAY)
            set(java.util.Calendar.HOUR_OF_DAY, 10)
            set(java.util.Calendar.MINUTE, 0)
            set(java.util.Calendar.SECOND, 0)
            set(java.util.Calendar.MILLISECOND, 0)
            if (before(now)) add(java.util.Calendar.WEEK_OF_YEAR, 1)
        }
        return target.timeInMillis - now.timeInMillis
    }

    private fun calculateInitialDelayFor7PM(): Long {
        val now = java.util.Calendar.getInstance()
        val target = java.util.Calendar.getInstance().apply {
            set(java.util.Calendar.HOUR_OF_DAY, 19)
            set(java.util.Calendar.MINUTE, 0)
            set(java.util.Calendar.SECOND, 0)
            set(java.util.Calendar.MILLISECOND, 0)
            if (before(now)) add(java.util.Calendar.DAY_OF_YEAR, 1)
        }
        return target.timeInMillis - now.timeInMillis
    }
}