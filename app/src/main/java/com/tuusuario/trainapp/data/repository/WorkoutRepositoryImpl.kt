package com.tuusuario.trainapp.data.repository

import com.tuusuario.trainapp.data.db.AppDatabase
import com.tuusuario.trainapp.data.db.dao.WorkoutDao
import com.tuusuario.trainapp.data.db.dao.WorkoutExerciseDao
import com.tuusuario.trainapp.data.db.dao.SetLogDao
import com.tuusuario.trainapp.data.db.entities.Workout
import com.tuusuario.trainapp.data.db.entities.WorkoutExercise
import com.tuusuario.trainapp.data.db.entities.SetLog
import com.tuusuario.trainapp.data.mapper.WorkoutMapper
import com.tuusuario.trainapp.domain.model.Workout as DomainWorkout
import com.tuusuario.trainapp.domain.model.WorkoutDetail
import com.tuusuario.trainapp.domain.model.WorkoutExercise as DomainWorkoutExercise
import com.tuusuario.trainapp.domain.model.SetLog as DomainSetLog
import com.tuusuario.trainapp.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WorkoutRepositoryImpl(
    private val database: AppDatabase
) : WorkoutRepository {

    private val workoutDao: WorkoutDao = database.workoutDao()
    private val workoutExerciseDao: WorkoutExerciseDao = database.workoutExerciseDao()
    private val setLogDao: SetLogDao = database.setLogDao()

    override fun getAll(): Flow<List<DomainWorkout>> =
        workoutDao.getAll().map { entities: List<Workout> ->
            entities.map { w: Workout -> WorkoutMapper.toDomain(w) }
        }

    override suspend fun getAllSync(): List<DomainWorkout> =
        workoutDao.getAllSync().map { w: Workout -> WorkoutMapper.toDomain(w) }

    override suspend fun getById(id: Long): DomainWorkout? =
        workoutDao.getById(id)?.let { w: Workout -> WorkoutMapper.toDomain(w) }

    override fun getByRoutine(routineId: Long): Flow<List<DomainWorkout>> =
        workoutDao.getByRoutine(routineId).map { entities: List<Workout> ->
            entities.map { w: Workout -> WorkoutMapper.toDomain(w) }
        }

    override fun getByDateRange(start: java.time.LocalDateTime, end: java.time.LocalDateTime): Flow<List<DomainWorkout>> =
        workoutDao.getByDateRange(start.toString(), end.toString()).map { entities: List<Workout> ->
            entities.map { w: Workout -> WorkoutMapper.toDomain(w) }
        }

    override suspend fun getLastWorkout(routineId: Long): DomainWorkout? =
        workoutDao.getLastWorkout(routineId)?.let { w: Workout -> WorkoutMapper.toDomain(w) }

    override suspend fun getLastWorkout(): DomainWorkout? =
        workoutDao.getLastWorkout()?.let { w: Workout -> WorkoutMapper.toDomain(w) }

    override suspend fun insert(workout: DomainWorkout): Long {
        val entity = WorkoutMapper.toEntity(workout)
        return workoutDao.insert(entity)
    }

    override suspend fun update(workout: DomainWorkout) {
        val entity = WorkoutMapper.toEntity(workout)
        workoutDao.update(entity)
    }

    override suspend fun delete(id: Long) {
        workoutDao.deleteById(id)
    }

    override suspend fun getDetail(workoutId: Long): WorkoutDetail? {
        // TODO: Implement
        return null
    }

    override suspend fun exportJson(): String {
        // TODO: Implement
        return "{}"
    }

    override suspend fun exportCsv(): String {
        // TODO: Implement
        return ""
    }
}