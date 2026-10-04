package com.tuusuario.trainapp.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "body_weights",
    indices = [androidx.room.Index("date")]
)
data class BodyWeight(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    var date: Long = System.currentTimeMillis(), // timestamp
    var weightKg: Double,
    var photoUri: String? = null,
    var notes: String = ""
)