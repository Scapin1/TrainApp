package com.tuusuario.trainapp.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tuusuario.trainapp.data.db.entities.AppSettings
import kotlinx.coroutines.flow.Flow

@Dao
interface AppSettingsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(settings: AppSettings): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(settings: List<AppSettings>)

    @Update
    suspend fun update(settings: AppSettings): Int

    @Query("SELECT * FROM app_settings WHERE key = :key")
    suspend fun get(key: String): AppSettings?

    @Query("SELECT valueJson FROM app_settings WHERE key = :key")
    suspend fun getValue(key: String): String?

    @Query("SELECT * FROM app_settings")
    fun getAll(): Flow<List<AppSettings>>

    @Query("SELECT * FROM app_settings")
    suspend fun getAllSync(): List<AppSettings>

    @Query("DELETE FROM app_settings WHERE key = :key")
    suspend fun delete(key: String)

    @Query("DELETE FROM app_settings")
    suspend fun deleteAll()
}