package com.tuusuario.trainapp.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "workout_exercises",
    foreignKeys = [
        ForeignKey(
            entity = Workout::class,
            parentColumns = ["id"],
            childColumns = ["workoutId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Block::class,
            parentColumns = ["id"],
            childColumns = ["blockId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = TemplateExercise::class,
            parentColumns = ["id"],
            childColumns = ["templateExerciseId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("workoutId"), Index("blockId"), Index("templateExerciseId")]
)
data class WorkoutExercise(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    var workoutId: Long,
    var blockId: Long?,
    var templateExerciseId: Long?,
    var exerciseName: String, // Denormalized for history
    var order: Int,
    var setsJson: String = "[]", // JSON array of SetLog
    var completedAt: Long? = null, // timestamp
    var skipped: Boolean = false,
    var sensationJson: String? = null // JSON serialized ExerciseSensation
)

@Serializable
data class ExerciseSensation(
    val rating: Int, // 1-10
    val notes: String = ""
)