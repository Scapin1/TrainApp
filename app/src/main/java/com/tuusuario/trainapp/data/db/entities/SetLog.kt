package com.tuusuario.trainapp.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "set_logs",
    foreignKeys = [
        ForeignKey(
            entity = WorkoutExercise::class,
            parentColumns = ["id"],
            childColumns = ["workoutExerciseId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("workoutExerciseId")]
)
data class SetLog(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    var workoutExerciseId: Long,
    var setIndex: Int, // 0-based index within exercise
    var weight: Double? = null, // null for bodyweight/circuit
    var reps: Int? = null, // null for time-based
    var rpe: Int? = null,
    var completed: Boolean = false,
    var durationSec: Int = 0, // For time-based sets
    var restAfterSec: Int = 0
)