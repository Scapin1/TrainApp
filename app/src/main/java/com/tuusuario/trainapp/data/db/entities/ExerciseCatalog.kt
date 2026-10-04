package com.tuusuario.trainapp.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "exercise_catalog",
    foreignKeys = [
        ForeignKey(
            entity = ExerciseCategory::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("categoryId"), Index("primaryMuscle"), Index("pattern"), Index("isCustom")]
)
data class ExerciseCatalog(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    var name: String,
    var pattern: ExercisePattern,
    var primaryMuscle: MuscleGroup,
    var equipment: Equipment,
    var unilateral: Boolean = false,
    var bilateral: Boolean = true,
    var notes: String = "",
    var aliasesJson: String = "[]", // JSON array of strings
    var categoryId: Long? = null,
    var isCustom: Boolean = false,
    var createdAt: Long = System.currentTimeMillis()
)

enum class ExercisePattern {
    PUSH_HORIZ, PUSH_VERT,
    PULL_HORIZ, PULL_VERT,
    SQUAT, HINGE, LUNGE,
    CORE, CARRY, CARDIO,
    OTHER
}

enum class MuscleGroup {
    CHEST, BACK, SHOULDERS, TRICEPS, BICEPS,
    QUADS, HAMSTRINGS, GLUTES, CALVES,
    CORE, FULL_BODY, FOREARMS, TRAPS, LATS
}

enum class Equipment {
    BARBELL, DUMBBELL, KETTLEBELL, MACHINE, CABLE,
    BANDS, TRX, BODYWEIGHT, MEDICINE_BALL, OTHER
}