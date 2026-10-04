package com.tuusuario.trainapp.domain.repository

import com.tuusuario.trainapp.domain.model.AppSettings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getAll(): Flow<List<AppSettings>>
    suspend fun getAllSync(): List<AppSettings>
    suspend fun get(key: String): String?
    suspend fun set(key: String, value: String)
    suspend fun delete(key: String)
    suspend fun resetDefaults()
}