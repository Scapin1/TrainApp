package com.tuusuario.trainapp.data.backup

import com.tuusuario.trainapp.data.db.entities.*

data class BackupSchema(
    val schemaVersion: Int = 1,
    val exportedAt: String,
    val routines: List<Routine>,
    val routineDays: List<RoutineDay>,
    val blocks: List<Block>,
    val templateExercises: List<TemplateExercise>,
    val exerciseCatalog: List<ExerciseCatalog>,
    val exerciseCategories: List<ExerciseCategory>,
    val workouts: List<Workout>,
    val workoutExercises: List<WorkoutExercise>,
    val setLogs: List<SetLog>,
    val bodyWeights: List<BodyWeight>,
    val globalSensations: List<GlobalSensation>,
    val appSettings: List<AppSettings>
)