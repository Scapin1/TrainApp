package com.tuusuario.trainapp.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.tuusuario.trainapp.data.db.entities.SetLog
import kotlinx.coroutines.flow.Flow

@Dao
interface SetLogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(setLog: SetLog): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(setLogs: List<SetLog>)

    @Update
    suspend fun update(setLog: SetLog): Int

    @Delete
    suspend fun delete(setLog: SetLog): Int

    @Query("SELECT * FROM set_logs WHERE workoutExerciseId = :workoutExerciseId ORDER BY setIndex")
    fun getByWorkoutExercise(workoutExerciseId: Long): Flow<List<SetLog>>

    @Query("SELECT * FROM set_logs WHERE workoutExerciseId = :workoutExerciseId ORDER BY setIndex")
    suspend fun getByWorkoutExerciseSync(workoutExerciseId: Long): List<SetLog>

    @Query("SELECT * FROM set_logs WHERE id = :id")
    suspend fun getById(id: Long): SetLog?

    @Query("DELETE FROM set_logs WHERE workoutExerciseId = :workoutExerciseId")
    suspend fun deleteByWorkoutExercise(workoutExerciseId: Long)

    @Query("SELECT * FROM set_logs ORDER BY workoutExerciseId, setIndex")
    suspend fun getAllSync(): List<SetLog>
}