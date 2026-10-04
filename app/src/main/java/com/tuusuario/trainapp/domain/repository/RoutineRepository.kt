package com.tuusuario.trainapp.domain.repository

import com.tuusuario.trainapp.domain.model.Routine
import com.tuusuario.trainapp.domain.model.RoutineWithDays
import kotlinx.coroutines.flow.Flow

interface RoutineRepository {
    fun getAll(): Flow<List<Routine>>
    suspend fun getById(id: Long): Routine?
    suspend fun getWithDays(id: Long): RoutineWithDays?
    suspend fun insert(routine: Routine): Long
    suspend fun update(routine: Routine)
    suspend fun delete(id: Long)
    suspend fun duplicate(id: Long): Long
    suspend fun reorderDays(routineId: Long, dayIds: List<Long>)
}