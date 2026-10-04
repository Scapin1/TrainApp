package com.tuusuario.trainapp.data.repository

import com.tuusuario.trainapp.data.db.AppDatabase
import com.tuusuario.trainapp.data.db.dao.AppSettingsDao
import com.tuusuario.trainapp.data.db.entities.AppSettings
import com.tuusuario.trainapp.domain.model.AppSettings as DomainAppSettings
import com.tuusuario.trainapp.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepositoryImpl(
    private val database: AppDatabase
) : SettingsRepository {

    private val settingsDao: AppSettingsDao = database.appSettingsDao()

    override fun getAll(): Flow<List<DomainAppSettings>> =
        settingsDao.getAll().map { entities: List<AppSettings> ->
            entities.map { s: AppSettings -> DomainAppSettings(key = s.key, valueJson = s.valueJson) }
        }

    override suspend fun getAllSync(): List<DomainAppSettings> =
        settingsDao.getAllSync().map { s: AppSettings -> DomainAppSettings(key = s.key, valueJson = s.valueJson) }

    override suspend fun get(key: String): String? =
        settingsDao.getValue(key)

    override suspend fun set(key: String, value: String) {
        val settings = AppSettings(key = key, valueJson = value)
        settingsDao.insert(settings)
    }

    override suspend fun delete(key: String) {
        settingsDao.delete(key)
    }

    override suspend fun resetDefaults() {
        settingsDao.deleteAll()
    }
}