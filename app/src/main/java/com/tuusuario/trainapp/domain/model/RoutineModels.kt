package com.tuusuario.trainapp.domain.model

import com.tuusuario.trainapp.data.db.entities.BlockType
import com.tuusuario.trainapp.data.db.entities.CycleType
import kotlinx.serialization.Serializable

@Serializable
data class Routine(
    val id: Long = 0,
    val name: String,
    val description: String = "",
    val dayCount: Int,
    val cycleType: CycleType = CycleType.LINEAR,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Serializable
data class RoutineDay(
    val id: Long = 0,
    val routineId: Long,
    val dayIndex: Int,
    val name: String,
    val blockOrder: List<Long> = emptyList()
)

@Serializable
data class Block(
    val id: Long = 0,
    val dayId: Long,
    val blockIndex: Int,
    val name: String,
    val type: BlockType,
    val configJson: String, // JSON serialized BlockConfig
    val order: Int
)

@Serializable
data class TemplateExercise(
    val id: Long = 0,
    val blockId: Long,
    val exerciseId: Long,
    val order: Int,
    val setsTarget: Int = 0,
    val repsTarget: Int = 0,
    val weightTarget: Double? = null,
    val rpeTarget: Int? = null,
    val restMipSec: Int = 60,
    val restMapSec: Int = 120,
    val timeWorkSec: Int = 0,
    val timeRestSec: Int = 0,
    val rounds: Int = 0,
    val configJson: String = "{}"
)

@Serializable
data class RoutineWithDays(
    val routine: Routine,
    val days: List<RoutineDay>
)