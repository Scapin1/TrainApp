package com.tuusuario.trainapp.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "global_sensations",
    indices = [androidx.room.Index("date")]
)
data class GlobalSensation(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    var date: Long = System.currentTimeMillis(), // timestamp
    var sleep: Int, // 1-10
    var fatigue: Int, // 1-10
    var motivation: Int, // 1-10
    var pain: Int, // 1-10
    var painNotes: String = "",
    var notes: String = "",
    var isStart: Boolean = true // true = start of workout, false = end
)

@Serializable
@Entity(tableName = "app_settings")
data class AppSettings(
    @PrimaryKey var key: String,
    var valueJson: String
)