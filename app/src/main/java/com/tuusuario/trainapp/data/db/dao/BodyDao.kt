package com.tuusuario.trainapp.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.tuusuario.trainapp.data.db.entities.BodyWeight
import com.tuusuario.trainapp.data.db.entities.GlobalSensation
import kotlinx.coroutines.flow.Flow

@Dao
interface BodyWeightDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bodyWeight: BodyWeight): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(bodyWeights: List<BodyWeight>)

    @Update
    suspend fun update(bodyWeight: BodyWeight): Int

    @Delete
    suspend fun delete(bodyWeight: BodyWeight): Int

    @Query("SELECT * FROM body_weights ORDER BY date DESC")
    fun getAll(): Flow<List<BodyWeight>>

    @Query("SELECT * FROM body_weights ORDER BY date DESC")
    suspend fun getAllSync(): List<BodyWeight>

    @Query("SELECT * FROM body_weights WHERE id = :id")
    suspend fun getById(id: Long): BodyWeight?

    @Query("SELECT * FROM body_weights ORDER BY date DESC LIMIT 1")
    suspend fun getLatest(): BodyWeight?

    @Query("SELECT * FROM body_weights WHERE date BETWEEN :start AND :end ORDER BY date")
    fun getByWeek(start: Long, end: Long): Flow<List<BodyWeight>>

    @Query("SELECT * FROM body_weights WHERE date = :date")
    suspend fun getByDate(date: Long): BodyWeight?

    @Query("SELECT COUNT(*) FROM body_weights WHERE date BETWEEN :start AND :end")
    suspend fun getCountThisWeek(start: Long, end: Long): Int

    @Query("DELETE FROM body_weights")
    suspend fun deleteAll()

    @Query("DELETE FROM body_weights WHERE id = :id")
    suspend fun deleteById(id: Long)
}

@Dao
interface GlobalSensationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(sensation: GlobalSensation): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(sensations: List<GlobalSensation>)

    @Update
    suspend fun update(sensation: GlobalSensation): Int

    @Delete
    suspend fun delete(sensation: GlobalSensation): Int

    @Query("SELECT * FROM global_sensations ORDER BY date DESC, isStart DESC")
    fun getAll(): Flow<List<GlobalSensation>>

    @Query("SELECT * FROM global_sensations ORDER BY date DESC, isStart DESC")
    suspend fun getAllSync(): List<GlobalSensation>

    @Query("SELECT * FROM global_sensations WHERE id = :id")
    suspend fun getById(id: Long): GlobalSensation?

    @Query("SELECT * FROM global_sensations WHERE date = :date AND isStart = :isStart")
    suspend fun getByDate(date: Long, isStart: Boolean): GlobalSensation?

    @Query("SELECT * FROM global_sensations ORDER BY date DESC LIMIT 1")
    suspend fun getLatest(): GlobalSensation?
}