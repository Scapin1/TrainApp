package com.tuusuario.trainapp.domain.repository

import com.tuusuario.trainapp.domain.model.Workout
import com.tuusuario.trainapp.domain.model.WorkoutDetail
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface WorkoutRepository {
    fun getAll(): Flow<List<Workout>>
    suspend fun getAllSync(): List<Workout>
    suspend fun getById(id: Long): Workout?
    fun getByRoutine(routineId: Long): Flow<List<Workout>>
    fun getByDateRange(start: LocalDateTime, end: LocalDateTime): Flow<List<Workout>>
    suspend fun getLastWorkout(routineId: Long): Workout?
    suspend fun getLastWorkout(): Workout?
    suspend fun insert(workout: Workout): Long
    suspend fun update(workout: Workout)
    suspend fun delete(id: Long)
    suspend fun getDetail(workoutId: Long): WorkoutDetail?
    suspend fun exportJson(): String
    suspend fun exportCsv(): String
}