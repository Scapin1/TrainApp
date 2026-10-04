package com.tuusuario.trainapp.domain.model

import com.tuusuario.trainapp.data.db.entities.ExercisePattern
import com.tuusuario.trainapp.data.db.entities.MuscleGroup
import com.tuusuario.trainapp.data.db.entities.Equipment
import kotlinx.serialization.Serializable

@Serializable
data class ExerciseCatalog(
    val id: Long = 0,
    val name: String,
    val pattern: ExercisePattern,
    val primaryMuscle: MuscleGroup,
    val equipment: Equipment,
    val unilateral: Boolean = false,
    val bilateral: Boolean = true,
    val notes: String = "",
    val aliases: List<String> = emptyList(),
    val categoryId: Long? = null,
    val isCustom: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Serializable
data class ExerciseCategory(
    val id: Long = 0,
    val name: String,
    val isSystem: Boolean = false,
    val order: Int = 0
)