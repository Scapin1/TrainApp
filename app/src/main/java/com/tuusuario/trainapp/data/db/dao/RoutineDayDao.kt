package com.tuusuario.trainapp.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Transaction
import com.tuusuario.trainapp.data.db.entities.RoutineDay
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutineDayDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(day: RoutineDay): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(days: List<RoutineDay>)

    @Update
    suspend fun update(day: RoutineDay): Int

    @Delete
    suspend fun delete(day: RoutineDay): Int

    @Query("SELECT * FROM routine_days WHERE routineId = :routineId ORDER BY dayIndex")
    fun getByRoutine(routineId: Long): Flow<List<RoutineDay>>

    @Query("SELECT * FROM routine_days WHERE routineId = :routineId ORDER BY dayIndex")
    suspend fun getByRoutineSync(routineId: Long): List<RoutineDay>

    @Query("SELECT * FROM routine_days WHERE id = :id")
    suspend fun getById(id: Long): RoutineDay?

    @Query("SELECT MAX(dayIndex) FROM routine_days WHERE routineId = :routineId")
    suspend fun getMaxDayIndex(routineId: Long): Int?

    @Query("DELETE FROM routine_days WHERE routineId = :routineId")
    suspend fun deleteByRoutine(routineId: Long)

    @Query("UPDATE routine_days SET dayIndex = :newIndex WHERE id = :id")
    suspend fun updateDayIndex(id: Long, newIndex: Int)

    @Query("SELECT * FROM routine_days ORDER BY routineId, dayIndex")
    suspend fun getAllSync(): List<RoutineDay>
}