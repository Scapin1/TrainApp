package com.tuusuario.trainapp.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.tuusuario.trainapp.data.db.entities.WorkoutExercise
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutExerciseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exercise: WorkoutExercise): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(exercises: List<WorkoutExercise>)

    @Update
    suspend fun update(exercise: WorkoutExercise): Int

    @Delete
    suspend fun delete(exercise: WorkoutExercise): Int

    @Query("SELECT * FROM workout_exercises WHERE workoutId = :workoutId ORDER BY `order`")
    fun getByWorkout(workoutId: Long): Flow<List<WorkoutExercise>>

    @Query("SELECT * FROM workout_exercises WHERE workoutId = :workoutId ORDER BY `order`")
    suspend fun getByWorkoutSync(workoutId: Long): List<WorkoutExercise>

    @Query("SELECT * FROM workout_exercises WHERE id = :id")
    suspend fun getById(id: Long): WorkoutExercise?

    @Query("DELETE FROM workout_exercises WHERE workoutId = :workoutId")
    suspend fun deleteByWorkout(workoutId: Long)

    @Query("SELECT * FROM workout_exercises ORDER BY workoutId, `order`")
    suspend fun getAllSync(): List<WorkoutExercise>
}