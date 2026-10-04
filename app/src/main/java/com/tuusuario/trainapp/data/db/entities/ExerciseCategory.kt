package com.tuusuario.trainapp.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "exercise_categories")
data class ExerciseCategory(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    var name: String,
    var isSystem: Boolean = false,
    var order: Int = 0
)