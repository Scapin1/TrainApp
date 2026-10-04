package com.tuusuario.trainapp.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "workouts",
    foreignKeys = [
        ForeignKey(
            entity = Routine::class,
            parentColumns = ["id"],
            childColumns = ["routineId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("routineId"), Index("startedAt")]
)
data class Workout(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    var routineId: Long?,
    var routineDayIndex: Int,
    var startedAt: Long = System.currentTimeMillis(),
    var endedAt: Long? = null,
    var durationSec: Int = 0,
    var blockSnapshotsJson: String = "[]", // JSON array of block snapshots with exercises
    var notes: String = "",
    var globalSensationStart: String? = null, // JSON serialized GlobalSensation
    var globalSensationEnd: String? = null
)