package com.tuusuario.trainapp.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "template_exercises",
    foreignKeys = [
        ForeignKey(
            entity = Block::class,
            parentColumns = ["id"],
            childColumns = ["blockId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ExerciseCatalog::class,
            parentColumns = ["id"],
            childColumns = ["exerciseId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [Index("blockId"), Index("exerciseId")]
)
data class TemplateExercise(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    var blockId: Long,
    var exerciseId: Long,
    var order: Int, // 0-based index within block
    // SETS_REPS config
    var setsTarget: Int = 0,
    var repsTarget: Int = 0,
    var weightTarget: Double? = null,
    var rpeTarget: Int? = null,
    var restMipSec: Int = 60,
    var restMapSec: Int = 120,
    // CIRCUIT/EMOM/HIIT/SINGLE_TIMER config
    var timeWorkSec: Int = 0,
    var timeRestSec: Int = 0,
    var rounds: Int = 0,
    // For MIXED and per-exercise overrides
    var configJson: String = "{}"
)