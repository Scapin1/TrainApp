package com.tuusuario.trainapp.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.tuusuario.trainapp.data.db.entities.ExerciseCatalog
import com.tuusuario.trainapp.data.db.entities.ExerciseCategory
import com.tuusuario.trainapp.data.db.entities.MuscleGroup
import com.tuusuario.trainapp.data.db.entities.ExercisePattern
import com.tuusuario.trainapp.data.db.entities.Equipment
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseCatalogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exercise: ExerciseCatalog): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(exercises: List<ExerciseCatalog>)

    @Update
    suspend fun update(exercise: ExerciseCatalog): Int

    @Delete
    suspend fun delete(exercise: ExerciseCatalog): Int

    @Query("SELECT * FROM exercise_catalog ORDER BY isCustom, name")
    fun getAll(): Flow<List<ExerciseCatalog>>

    @Query("SELECT * FROM exercise_catalog ORDER BY isCustom, name")
    suspend fun getAllSync(): List<ExerciseCatalog>

    @Query("SELECT * FROM exercise_catalog WHERE id = :id")
    suspend fun getById(id: Long): ExerciseCatalog?

    @Query("SELECT * FROM exercise_catalog WHERE id = :id")
    suspend fun getByIdSync(id: Long): ExerciseCatalog?

    @Query("SELECT * FROM exercise_catalog WHERE categoryId = :categoryId ORDER BY name")
    fun getByCategory(categoryId: Long): Flow<List<ExerciseCatalog>>

    @Query("SELECT * FROM exercise_catalog WHERE primaryMuscle = :muscle ORDER BY name")
    fun getByMuscle(muscle: MuscleGroup): Flow<List<ExerciseCatalog>>

    @Query("SELECT * FROM exercise_catalog WHERE pattern = :pattern ORDER BY name")
    fun getByPattern(pattern: ExercisePattern): Flow<List<ExerciseCatalog>>

    @Query("SELECT * FROM exercise_catalog WHERE equipment = :equipment ORDER BY name")
    fun getByEquipment(equipment: Equipment): Flow<List<ExerciseCatalog>>

    @Query("SELECT * FROM exercise_catalog WHERE isCustom = :isCustom ORDER BY name")
    fun getByCustom(isCustom: Boolean): Flow<List<ExerciseCatalog>>

    @Query("SELECT * FROM exercise_catalog WHERE name LIKE :query OR aliasesJson LIKE :query ORDER BY isCustom, name")
    fun search(query: String): Flow<List<ExerciseCatalog>>

    @Query("SELECT * FROM exercise_catalog WHERE isCustom = 1 ORDER BY createdAt DESC LIMIT :limit")
    fun getRecentCustom(limit: Int): Flow<List<ExerciseCatalog>>

    @Query("DELETE FROM exercise_catalog WHERE isCustom = 1")
    suspend fun deleteAllCustom()

    @Query("DELETE FROM exercise_catalog WHERE id = :id")
    suspend fun deleteById(id: Long)
}

@Dao
interface ExerciseCategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: ExerciseCategory): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(categories: List<ExerciseCategory>)

    @Update
    suspend fun update(category: ExerciseCategory): Int

    @Delete
    suspend fun delete(category: ExerciseCategory): Int

    @Query("SELECT * FROM exercise_categories ORDER BY isSystem DESC, `order`, name")
    fun getAll(): Flow<List<ExerciseCategory>>

    @Query("SELECT * FROM exercise_categories ORDER BY isSystem DESC, `order`, name")
    suspend fun getAllSync(): List<ExerciseCategory>

    @Query("SELECT * FROM exercise_categories WHERE id = :id")
    suspend fun getById(id: Long): ExerciseCategory?

    @Query("SELECT MAX(`order`) FROM exercise_categories")
    suspend fun getMaxOrder(): Int?

    @Query("DELETE FROM exercise_categories WHERE isSystem = 0")
    suspend fun deleteAllCustom()

    @Query("DELETE FROM exercise_categories WHERE id = :id")
    suspend fun deleteById(id: Long)
}