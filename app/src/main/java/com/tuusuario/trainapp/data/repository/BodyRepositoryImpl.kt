package com.tuusuario.trainapp.data.repository

import com.tuusuario.trainapp.data.db.AppDatabase
import com.tuusuario.trainapp.data.db.dao.BodyWeightDao
import com.tuusuario.trainapp.data.db.dao.GlobalSensationDao
import com.tuusuario.trainapp.data.db.entities.BodyWeight
import com.tuusuario.trainapp.data.db.entities.GlobalSensation
import com.tuusuario.trainapp.data.mapper.BodyMapper
import com.tuusuario.trainapp.domain.model.BodyWeight as DomainBodyWeight
import com.tuusuario.trainapp.domain.model.GlobalSensation as DomainGlobalSensation
import com.tuusuario.trainapp.domain.repository.BodyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BodyRepositoryImpl(
    private val database: AppDatabase
) : BodyRepository {

    private val bodyWeightDao: BodyWeightDao = database.bodyWeightDao()
    private val globalSensationDao: GlobalSensationDao = database.globalSensationDao()

    // Body Weight
    override fun getAllWeights(): Flow<List<com.tuusuario.trainapp.domain.model.BodyWeight>> =
        bodyWeightDao.getAll().map { entities: List<BodyWeight> ->
            entities.map { b: BodyWeight -> BodyMapper.toDomain(b) }
        }

    override suspend fun getAllWeightsSync(): List<com.tuusuario.trainapp.domain.model.BodyWeight> =
        bodyWeightDao.getAllSync().map { b: BodyWeight -> BodyMapper.toDomain(b) }

    override suspend fun getLatestWeight(): com.tuusuario.trainapp.domain.model.BodyWeight? =
        bodyWeightDao.getLatest()?.let { b: BodyWeight -> BodyMapper.toDomain(b) }

    override suspend fun getByDate(date: java.time.LocalDate): com.tuusuario.trainapp.domain.model.BodyWeight? =
        bodyWeightDao.getByDate(date.toEpochDay())?.let { b: BodyWeight -> BodyMapper.toDomain(b) }

    override fun getByWeek(start: java.time.LocalDate): Flow<List<com.tuusuario.trainapp.domain.model.BodyWeight>> =
        bodyWeightDao.getByWeek(start.toEpochDay(), (start.toEpochDay() + 6)).map { entities: List<BodyWeight> ->
            entities.map { b: BodyWeight -> BodyMapper.toDomain(b) }
        }

    override suspend fun insertWeight(weight: com.tuusuario.trainapp.domain.model.BodyWeight): Long {
        val entity = BodyMapper.toEntity(weight)
        return bodyWeightDao.insert(entity)
    }

    override suspend fun updateWeight(weight: com.tuusuario.trainapp.domain.model.BodyWeight) {
        val entity = BodyMapper.toEntity(weight)
        bodyWeightDao.update(entity)
    }

    override suspend fun deleteWeight(id: Long) {
        bodyWeightDao.deleteById(id)
    }

    override suspend fun shouldNotifyWeighIn(): Boolean {
        val count = bodyWeightDao.getCountThisWeek(System.currentTimeMillis() - 7*24*60*60*1000, System.currentTimeMillis())
        return count == 0
    }

    // Global Sensations
    override fun getAllSensations(): Flow<List<com.tuusuario.trainapp.domain.model.GlobalSensation>> =
        globalSensationDao.getAll().map { entities: List<GlobalSensation> ->
            entities.map { s: GlobalSensation -> BodyMapper.toDomain(s) }
        }

    override suspend fun getLatestSensation(): com.tuusuario.trainapp.domain.model.GlobalSensation? =
        globalSensationDao.getLatest()?.let { s: GlobalSensation -> BodyMapper.toDomain(s) }

    override suspend fun getByDate(date: java.time.LocalDate, isStart: Boolean): com.tuusuario.trainapp.domain.model.GlobalSensation? =
        globalSensationDao.getByDate(date.toEpochDay(), isStart)?.let { s: GlobalSensation -> BodyMapper.toDomain(s) }

    override suspend fun insertSensation(sensation: com.tuusuario.trainapp.domain.model.GlobalSensation): Long {
        val entity = BodyMapper.toEntity(sensation)
        return globalSensationDao.insert(entity)
    }
}