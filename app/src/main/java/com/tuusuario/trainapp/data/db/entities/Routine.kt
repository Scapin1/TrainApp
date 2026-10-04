package com.tuusuario.trainapp.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "routines")
data class Routine(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    var name: String,
    var description: String = "",
    var dayCount: Int,
    var cycleType: CycleType = CycleType.LINEAR,
    var createdAt: Long = System.currentTimeMillis(),
    var updatedAt: Long = System.currentTimeMillis()
)

enum class CycleType {
    LINEAR // N-day cyclic, no calendar anchor
}