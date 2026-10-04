package com.tuusuario.trainapp.data.backup

import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tuusuario.trainapp.data.db.AppDatabase
import com.tuusuario.trainapp.data.db.entities.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.FileInputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BackupManager(
    private val database: AppDatabase,
    private val context: Context
) {

    private val gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        .setLenient()
        .create()

    sealed class ExportResult {
        data class Success(val uri: Uri) : ExportResult()
        data class Failure(val error: String) : ExportResult()
    }

    sealed class ImportResult {
        data class Success(val report: ImportReport) : ImportResult()
        data class Failure(val error: String) : ImportResult()
    }

    data class ImportReport(
        val imported: Boolean,
        val routinesCount: Int,
        val workoutsCount: Int,
        val bodyWeightsCount: Int,
        val settingsCount: Int,
        val errors: List<String>
    )

    suspend fun exportToFile(): ExportResult = withContext(Dispatchers.IO) {
        try {
            val routineDao = database.routineDao()
            val dayDao = database.routineDayDao()
            val blockDao = database.blockDao()
            val templateDao = database.templateExerciseDao()
            val exerciseDao = database.exerciseCatalogDao()
            val categoryDao = database.exerciseCategoryDao()
            val workoutDao = database.workoutDao()
            val workoutExerciseDao = database.workoutExerciseDao()
            val setLogDao = database.setLogDao()
            val bodyWeightDao = database.bodyWeightDao()
            val sensationDao = database.globalSensationDao()
            val settingsDao = database.appSettingsDao()

            val routines: List<Routine> = routineDao.getAllSync()
            val routineDays: List<RoutineDay> = dayDao.getAllSync()
            val blocks: List<Block> = blockDao.getAllSync()
            val templateExercises: List<TemplateExercise> = templateDao.getAllSync()
            val exerciseCatalog: List<ExerciseCatalog> = exerciseDao.getAllSync()
            val exerciseCategories: List<ExerciseCategory> = categoryDao.getAllSync()
            val workouts: List<Workout> = workoutDao.getAllSync()
            val workoutExercises: List<WorkoutExercise> = workoutExerciseDao.getAllSync()
            val setLogs: List<SetLog> = setLogDao.getAllSync()
            val bodyWeights: List<BodyWeight> = bodyWeightDao.getAllSync()
            val globalSensations: List<GlobalSensation> = sensationDao.getAllSync()
            val appSettings: List<AppSettings> = settingsDao.getAllSync()

            val schema = BackupSchema(
                exportedAt = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).format(Date()),
                routines = routines,
                routineDays = routineDays,
                blocks = blocks,
                templateExercises = templateExercises,
                exerciseCatalog = exerciseCatalog,
                exerciseCategories = exerciseCategories,
                workouts = workouts,
                workoutExercises = workoutExercises,
                setLogs = setLogs,
                bodyWeights = bodyWeights,
                globalSensations = globalSensations,
                appSettings = appSettings
            )

            val jsonString = gson.toJson(schema)
            val fileName = "trainapp_backup_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())}.json"
            val file = File(context.filesDir, "backups/$fileName")
            file.parentFile?.mkdirs()
            
            FileOutputStream(file).use { output ->
                OutputStreamWriter(output).use { writer ->
                    writer.write(jsonString)
                }
            }
            
            val uri = Uri.fromFile(file)
            ExportResult.Success(uri)
        } catch (e: Exception) {
            ExportResult.Failure(e.message ?: "Unknown error")
        }
    }

    suspend fun exportWorkoutsCsv(): ExportResult = withContext(Dispatchers.IO) {
        try {
            val workoutDao = database.workoutDao()
            val workoutExerciseDao = database.workoutExerciseDao()
            val setLogDao = database.setLogDao()
            val exerciseDao = database.exerciseCatalogDao()

            val workouts: List<Workout> = workoutDao.getAllSync()
            val csvBuilder = StringBuilder()
            csvBuilder.appendLine("date,routine,day,exercise,set,weight,reps,rpe,rest_sec,duration_sec,completed")

            for (workout in workouts) {
                val exercises: List<WorkoutExercise> = workoutExerciseDao.getByWorkoutSync(workout.id)
                for (we in exercises) {
                    val sets: List<SetLog> = setLogDao.getByWorkoutExerciseSync(we.id)
                    val exerciseName = we.templateExerciseId?.let { exerciseDao.getByIdSync(it) }?.name ?: we.exerciseName
                    for (set in sets) {
                        csvBuilder.appendLine(
                            "${workout.startedAt}," +
                            "${workout.routineId}," +
                            "${workout.routineDayIndex}," +
                            "$exerciseName," +
                            "${set.setIndex}," +
                            "${set.weight}," +
                            "${set.reps}," +
                            "${set.rpe}," +
                            "${set.restAfterSec}," +
                            "${set.durationSec}," +
                            "${set.completed}"
                        )
                    }
                }
            }

            val fileName = "trainapp_workouts_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())}.csv"
            val file = File(context.filesDir, "backups/$fileName")
            file.parentFile?.mkdirs()
            
            FileOutputStream(file).use { output ->
                OutputStreamWriter(output).use { writer ->
                    writer.write(csvBuilder.toString())
                }
            }
            
            ExportResult.Success(Uri.fromFile(file))
        } catch (e: Exception) {
            ExportResult.Failure(e.message ?: "Unknown error")
        }
    }

    suspend fun importFromFile(file: File): ImportResult = withContext(Dispatchers.IO) {
        try {
            val jsonString = FileInputStream(file).use { input ->
                InputStreamReader(input).use { reader ->
                    reader.readText()
                }
            }
            val schema: BackupSchema = gson.fromJson(jsonString, BackupSchema::class.java)

            if (schema.schemaVersion != 1) {
                return@withContext ImportResult.Failure("Unsupported schema version: ${schema.schemaVersion}")
            }

            val routineDao = database.routineDao()
            val dayDao = database.routineDayDao()
            val blockDao = database.blockDao()
            val templateDao = database.templateExerciseDao()
            val exerciseDao = database.exerciseCatalogDao()
            val categoryDao = database.exerciseCategoryDao()
            val workoutDao = database.workoutDao()
            val workoutExerciseDao = database.workoutExerciseDao()
            val setLogDao = database.setLogDao()
            val bodyWeightDao = database.bodyWeightDao()
            val sensationDao = database.globalSensationDao()
            val settingsDao = database.appSettingsDao()

            val errors = mutableListOf<String>()

            // Import categories first
            for (cat in schema.exerciseCategories) {
                try { categoryDao.insert(cat.copy(id = 0)) } catch (e: Exception) { errors.add("Category ${cat.name}: ${e.message}") }
            }

            // Import exercises (only custom ones to avoid duplicates)
            for (ex in schema.exerciseCatalog.filter { it.isCustom }) {
                try { exerciseDao.insert(ex.copy(id = 0)) } catch (e: Exception) { errors.add("Exercise ${ex.name}: ${e.message}") }
            }

            // Import routines
            var routinesCount = 0
            for (routine in schema.routines) {
                try {
                    val newRoutineId = routineDao.insert(routine.copy(id = 0))
                    routinesCount++
                } catch (e: Exception) { errors.add("Routine ${routine.name}: ${e.message}") }
            }

            // Import workouts
            var workoutsCount = 0
            for (workout in schema.workouts) {
                try { workoutDao.insert(workout.copy(id = 0)); workoutsCount++ } catch (e: Exception) { errors.add("Workout ${workout.id}: ${e.message}") }
            }

            // Import body weights
            var bodyWeightsCount = 0
            for (bw in schema.bodyWeights) {
                try { bodyWeightDao.insert(bw.copy(id = 0)); bodyWeightsCount++ } catch (e: Exception) { errors.add("BodyWeight ${bw.id}: ${e.message}") }
            }

            // Import settings
            var settingsCount = 0
            for (setting in schema.appSettings) {
                try { settingsDao.insert(setting); settingsCount++ } catch (e: Exception) { errors.add("Setting ${setting.key}: ${e.message}") }
            }

            ImportResult.Success(ImportReport(
                imported = true,
                routinesCount = routinesCount,
                workoutsCount = workoutsCount,
                bodyWeightsCount = bodyWeightsCount,
                settingsCount = settingsCount,
                errors = errors
            ))
        } catch (e: Exception) {
            ImportResult.Failure(e.message ?: "Unknown error")
        }
    }

    suspend fun cleanupOldBackups(keepCount: Int = 30) = withContext(Dispatchers.IO) {
        val dir = File(context.filesDir, "backups")
        if (dir.exists()) {
            val files = dir.listFiles()?.filter { it.name.endsWith(".json") }?.sortedByDescending { it.lastModified() } ?: return@withContext
            files.drop(keepCount).forEach { it.delete() }
        }
    }

    suspend fun getLastBackupTime(): Long? = withContext(Dispatchers.IO) {
        val dir = File(context.filesDir, "backups")
        val files = dir.listFiles()?.filter { it.name.endsWith(".json") }?.sortedByDescending { it.lastModified() } ?: return@withContext null
        files.firstOrNull()?.lastModified()
    }
}

private fun StringBuilder.appendLine(line: String) {
    this.append(line).append("\n")
}