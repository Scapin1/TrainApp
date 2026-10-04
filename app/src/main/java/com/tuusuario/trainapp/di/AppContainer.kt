package com.tuusuario.trainapp.di

import android.content.Context
import com.tuusuario.trainapp.data.db.AppDatabase
import com.tuusuario.trainapp.data.repository.BackupRepositoryImpl
import com.tuusuario.trainapp.data.repository.BodyRepositoryImpl
import com.tuusuario.trainapp.data.repository.ExerciseRepositoryImpl
import com.tuusuario.trainapp.data.repository.RoutineRepositoryImpl
import com.tuusuario.trainapp.data.repository.SettingsRepositoryImpl
import com.tuusuario.trainapp.data.repository.WorkoutRepositoryImpl
import com.tuusuario.trainapp.domain.repository.BackupRepository
import com.tuusuario.trainapp.domain.repository.BodyRepository
import com.tuusuario.trainapp.domain.repository.ExerciseRepository
import com.tuusuario.trainapp.domain.repository.RoutineRepository
import com.tuusuario.trainapp.domain.repository.SettingsRepository
import com.tuusuario.trainapp.domain.repository.WorkoutRepository
import com.tuusuario.trainapp.data.backup.BackupManager

class AppContainer @Suppress("UNUSED_PARAMETER") constructor(context: Context) {
    val database: AppDatabase = AppDatabase.getInstance(context)
    val routineRepository: RoutineRepository = RoutineRepositoryImpl(database)
    val exerciseRepository: ExerciseRepository = ExerciseRepositoryImpl(database)
    val workoutRepository: WorkoutRepository = WorkoutRepositoryImpl(database)
    val bodyRepository: BodyRepository = BodyRepositoryImpl(database)
    val settingsRepository: SettingsRepository = SettingsRepositoryImpl(database)
    val backupRepository: BackupRepository = BackupRepositoryImpl()
    val backupManager: BackupManager = BackupManager(database, context)
}