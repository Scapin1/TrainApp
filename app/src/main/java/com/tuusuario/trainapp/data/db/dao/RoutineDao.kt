package com.tuusuario.trainapp.data.db.dao

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Delete
import com.tuusuario.trainapp.data.db.entities.Routine
import com.tuusuario.trainapp.data.db.entities.RoutineDay
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutineDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(routine: Routine): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(routines: List<Routine>)

    @Update
    suspend fun update(routine: Routine): Int

    @Delete
    suspend fun delete(routine: Routine): Int

    @Query("SELECT * FROM routines ORDER BY updatedAt DESC")
    fun getAll(): Flow<List<Routine>>

    @Query("SELECT * FROM routines WHERE id = :id")
    fun getById(id: Long): Flow<Routine?>

    @Query("SELECT * FROM routines WHERE id = :id")
    suspend fun getByIdSync(id: Long): Routine?

    @Query("SELECT * FROM routines ORDER BY updatedAt DESC")
    suspend fun getAllSync(): List<Routine>

    @Transaction
    @Query("SELECT * FROM routines WHERE id = :id")
    fun getWithDays(id: Long): Flow<RoutineWithDays>

    @Query("DELETE FROM routines")
    suspend fun deleteAll()

    @Query("DELETE FROM routines WHERE id = :id")
    suspend fun delete(id: Long)
}

data class RoutineWithDays(
    @Embedded val routine: Routine,
    @Relation(
        parentColumn = "id",
        entityColumn = "routineId"
    ) val days: List<RoutineDay>
)