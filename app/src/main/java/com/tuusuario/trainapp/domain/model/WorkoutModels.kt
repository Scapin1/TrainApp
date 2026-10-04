package com.tuusuario.trainapp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ExerciseSensation(
    val rating: Int, // 1-10
    val notes: String = ""
)

@Serializable
data class Workout(
    val id: Long = 0,
    val routineId: Long?,
    val routineDayIndex: Int,
    val startedAt: Long = System.currentTimeMillis(), // timestamp
    val endedAt: Long? = null, // timestamp
    val durationSec: Int = 0,
    val blockSnapshotsJson: String = "[]",
    val notes: String = "",
    val globalSensationStart: String? = null,
    val globalSensationEnd: String? = null
)

@Serializable
data class WorkoutExercise(
    val id: Long = 0,
    val workoutId: Long,
    val blockId: Long?,
    val templateExerciseId: Long?,
    val exerciseName: String,
    val order: Int,
    val setsJson: String = "[]", // JSON serialized List<SetLog>
    val completedAt: Long? = null, // timestamp
    val skipped: Boolean = false,
    val sensation: ExerciseSensation? = null
)

@Serializable
data class SetLog(
    val id: Long = 0,
    val workoutExerciseId: Long,
    val setIndex: Int,
    val weight: Double? = null,
    val reps: Int? = null,
    val rpe: Int? = null,
    val completed: Boolean = false,
    val durationSec: Int = 0,
    val restAfterSec: Int = 0
)

@Serializable
data class WorkoutDetail(
    val workout: Workout,
    val exercises: List<WorkoutExercise>,
    val blocks: List<BlockSnapshot>
)

@Serializable
data class BlockSnapshot(
    val blockId: Long,
    val blockName: String,
    val blockType: com.tuusuario.trainapp.data.db.entities.BlockType,
    val exercises: List<WorkoutExercise>
)