package com.tuusuario.trainapp.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "routine_days",
    foreignKeys = [
        ForeignKey(
            entity = Routine::class,
            parentColumns = ["id"],
            childColumns = ["routineId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("routineId")]
)
data class RoutineDay(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    var routineId: Long,
    var dayIndex: Int, // 0-based index within routine
    var name: String,
    var blockOrderJson: String = "[]" // JSON array of block IDs in order
)