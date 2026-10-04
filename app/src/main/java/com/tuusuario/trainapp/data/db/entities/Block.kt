package com.tuusuario.trainapp.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "blocks",
    foreignKeys = [
        ForeignKey(
            entity = RoutineDay::class,
            parentColumns = ["id"],
            childColumns = ["dayId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("dayId")]
)
data class Block(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    var dayId: Long,
    var blockIndex: Int, // 0-based index within day
    var name: String,
    var type: BlockType,
    var configJson: String, // Serialized BlockConfig as JSON string
    var order: Int // For reordering within day
)