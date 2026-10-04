package com.tuusuario.trainapp.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.tuusuario.trainapp.data.db.entities.Workout
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface WorkoutDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(workout: Workout): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(workouts: List<Workout>)

    @Update
    suspend fun update(workout: Workout): Int

    @Delete
    suspend fun delete(workout: Workout): Int

    @Query("SELECT * FROM workouts ORDER BY startedAt DESC")
    fun getAll(): Flow<List<Workout>>

    @Query("SELECT * FROM workouts ORDER BY startedAt DESC")
    suspend fun getAllSync(): List<Workout>

    @Query("SELECT * FROM workouts WHERE id = :id")
    suspend fun getById(id: Long): Workout?

    @Query("SELECT * FROM workouts WHERE routineId = :routineId ORDER BY startedAt DESC")
    fun getByRoutine(routineId: Long): Flow<List<Workout>>

    @Query("SELECT * FROM workouts WHERE date(startedAt) BETWEEN date(:start) AND date(:end) ORDER BY startedAt DESC")
    fun getByDateRange(start: String, end: String): Flow<List<Workout>>

    @Query("SELECT * FROM workouts WHERE routineId = :routineId ORDER BY startedAt DESC LIMIT 1")
    suspend fun getLastWorkout(routineId: Long): Workout?

    @Query("SELECT * FROM workouts ORDER BY startedAt DESC LIMIT 1")
    suspend fun getLastWorkout(): Workout?

    @Query("SELECT COUNT(*) FROM workouts WHERE date(startedAt) = date(:date)")
    suspend fun getCountByDate(date: String): Int

    @Query("DELETE FROM workouts")
    suspend fun deleteAll()

    @Query("DELETE FROM workouts WHERE id = :id")
    suspend fun deleteById(id: Long)
}