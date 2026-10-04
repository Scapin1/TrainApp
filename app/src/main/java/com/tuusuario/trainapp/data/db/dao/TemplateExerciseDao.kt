package com.tuusuario.trainapp.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.tuusuario.trainapp.data.db.entities.TemplateExercise
import kotlinx.coroutines.flow.Flow

@Dao
interface TemplateExerciseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exercise: TemplateExercise): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(exercises: List<TemplateExercise>)

    @Update
    suspend fun update(exercise: TemplateExercise): Int

    @Delete
    suspend fun delete(exercise: TemplateExercise): Int

    @Query("SELECT * FROM template_exercises WHERE blockId = :blockId ORDER BY `order`")
    fun getByBlock(blockId: Long): Flow<List<TemplateExercise>>

    @Query("SELECT * FROM template_exercises WHERE blockId = :blockId ORDER BY `order`")
    suspend fun getByBlockSync(blockId: Long): List<TemplateExercise>

    @Query("SELECT * FROM template_exercises WHERE id = :id")
    suspend fun getById(id: Long): TemplateExercise?

    @Query("SELECT MAX(`order`) FROM template_exercises WHERE blockId = :blockId")
    suspend fun getMaxOrder(blockId: Long): Int?

    @Query("DELETE FROM template_exercises WHERE blockId = :blockId")
    suspend fun deleteByBlock(blockId: Long)

    @Query("DELETE FROM template_exercises WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM template_exercises ORDER BY blockId, `order`")
    suspend fun getAllSync(): List<TemplateExercise>
}