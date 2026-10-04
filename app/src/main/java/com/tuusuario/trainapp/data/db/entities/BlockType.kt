package com.tuusuario.trainapp.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

enum class BlockType {
    SETS_REPS,
    CIRCUIT_TIME,
    EMOM,
    HIIT,
    SINGLE_TIMER,
    MIXED
}

@Serializable
sealed interface BlockConfig {
    @Serializable
    data class SetsReps(
        val setsTarget: Int,
        val repsTarget: Int,
        val weightTarget: Double?,
        val rpeTarget: Int?,
        val restMipSec: Int,
        val restMapSec: Int,
        val autoRestEnabled: Boolean = true
    ) : BlockConfig

    @Serializable
    data class CircuitTime(
        val timeWorkSec: Int,
        val timeRestSec: Int,
        val rounds: Int,
        val autoAdvanceDelayMs: Int = 500,
        val exerciseOrder: List<Int> = emptyList()
    ) : BlockConfig

    @Serializable
    data class Emom(
        val timeWorkSec: Int,
        val rounds: Int,
        val exercisesPerRound: Int,
        val autoAdvanceDelayMs: Int = 500
    ) : BlockConfig

    @Serializable
    data class Hiit(
        val intervals: List<HiitInterval>,
        val rounds: Int,
        val autoAdvanceDelayMs: Int = 500
    ) : BlockConfig

    @Serializable
    data class SingleTimer(
        val durationSec: Int,
        val autoAdvanceDelayMs: Int = 500
    ) : BlockConfig

    @Serializable
    data class Mixed(
        val exercises: List<MixedExerciseConfig>
    ) : BlockConfig
}

@Serializable
data class HiitInterval(
    val workSec: Int,
    val restSec: Int,
    val exerciseOrder: List<Int>
)

@Serializable
data class MixedExerciseConfig(
    val exerciseId: Long,
    val configJson: String // Serialized per-exercise config
)