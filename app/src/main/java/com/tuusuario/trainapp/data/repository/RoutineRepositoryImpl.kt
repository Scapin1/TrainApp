package com.tuusuario.trainapp.data.repository

import com.tuusuario.trainapp.data.db.AppDatabase
import com.tuusuario.trainapp.data.db.dao.RoutineDao
import com.tuusuario.trainapp.data.db.dao.RoutineDayDao
import com.tuusuario.trainapp.data.db.entities.Routine
import com.tuusuario.trainapp.data.db.entities.RoutineDay
import com.tuusuario.trainapp.data.mapper.RoutineMapper
import com.tuusuario.trainapp.domain.model.Routine as DomainRoutine
import com.tuusuario.trainapp.domain.model.RoutineDay as DomainRoutineDay
import com.tuusuario.trainapp.domain.model.RoutineWithDays
import com.tuusuario.trainapp.domain.repository.RoutineRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class RoutineRepositoryImpl(
    private val database: AppDatabase
) : RoutineRepository {

    private val routineDao: RoutineDao = database.routineDao()
    private val routineDayDao: RoutineDayDao = database.routineDayDao()

    override fun getAll(): Flow<List<DomainRoutine>> =
        routineDao.getAll().map { routineList: List<Routine> ->
            routineList.map { r: Routine -> RoutineMapper.toDomain(r) }
        }

    override suspend fun getById(id: Long): DomainRoutine? =
        routineDao.getByIdSync(id)?.let { entity -> RoutineMapper.toDomain(entity as com.tuusuario.trainapp.data.db.entities.Routine) }

    override suspend fun getWithDays(id: Long): RoutineWithDays? {
        val entity: com.tuusuario.trainapp.data.db.dao.RoutineWithDays? = routineDao.getWithDays(id).first()
        return entity?.let { entityWithDays ->
            RoutineWithDays(
                routine = RoutineMapper.toDomain(entityWithDays.routine),
                days = entityWithDays.days.map { d: RoutineDay -> RoutineMapper.toDomainDay(d) }
            )
        }
    }

    override suspend fun insert(routine: DomainRoutine): Long {
        val entity = RoutineMapper.toEntity(routine)
        return routineDao.insert(entity)
    }

    override suspend fun update(routine: DomainRoutine) {
        val entity = RoutineMapper.toEntity(routine)
        routineDao.update(entity)
    }

    override suspend fun delete(id: Long) {
        routineDao.delete(id)
    }

    override suspend fun duplicate(id: Long): Long {
        val original = routineDao.getByIdSync(id) ?: return -1
        val now = System.currentTimeMillis()
        val copy = original.copy(id = 0, name = "${original.name} (Copy)", createdAt = now, updatedAt = now)
        val newId = routineDao.insert(copy)

        // Duplicate days
        val days = routineDayDao.getByRoutineSync(id)
        for (day in days) {
            val newDay = day.copy(id = 0, routineId = newId)
            routineDayDao.insert(newDay)
        }

        return newId
    }

    override suspend fun reorderDays(routineId: Long, dayIds: List<Long>) {
        dayIds.forEachIndexed { index, dayId ->
            routineDayDao.updateDayIndex(dayId, index)
        }
    }
}