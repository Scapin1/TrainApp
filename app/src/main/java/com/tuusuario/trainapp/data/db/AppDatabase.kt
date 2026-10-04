package com.tuusuario.trainapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.tuusuario.trainapp.data.db.callback.SeedCallback
import com.tuusuario.trainapp.data.db.converters.Converters
import com.tuusuario.trainapp.data.db.dao.*
import com.tuusuario.trainapp.data.db.entities.*

@Database(
    entities = [
        Routine::class,
        RoutineDay::class,
        Block::class,
        TemplateExercise::class,
        ExerciseCatalog::class,
        ExerciseCategory::class,
        Workout::class,
        WorkoutExercise::class,
        SetLog::class,
        BodyWeight::class,
        GlobalSensation::class,
        AppSettings::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun routineDao(): RoutineDao
    abstract fun routineDayDao(): RoutineDayDao
    abstract fun blockDao(): BlockDao
    abstract fun templateExerciseDao(): TemplateExerciseDao
    abstract fun exerciseCatalogDao(): ExerciseCatalogDao
    abstract fun exerciseCategoryDao(): ExerciseCategoryDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun workoutExerciseDao(): WorkoutExerciseDao
    abstract fun setLogDao(): SetLogDao
    abstract fun bodyWeightDao(): BodyWeightDao
    abstract fun globalSensationDao(): GlobalSensationDao
    abstract fun appSettingsDao(): AppSettingsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "trainapp.db"
                )
                    .addCallback(SeedCallback(context))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}