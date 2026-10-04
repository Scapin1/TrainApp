package com.tuusuario.trainapp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class BodyWeight(
    val id: Long = 0,
    val date: Long = System.currentTimeMillis(), // timestamp
    val weightKg: Double,
    val photoUri: String? = null,
    val notes: String = ""
)

@Serializable
data class GlobalSensation(
    val id: Long = 0,
    val date: Long = System.currentTimeMillis(), // timestamp
    val sleep: Int,
    val fatigue: Int,
    val motivation: Int,
    val pain: Int,
    val painNotes: String = "",
    val notes: String = "",
    val isStart: Boolean = true
)

@Serializable
data class AppSettings(
    val key: String,
    val valueJson: String
)