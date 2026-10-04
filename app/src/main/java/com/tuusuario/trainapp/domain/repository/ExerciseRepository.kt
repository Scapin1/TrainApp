package com.tuusuario.trainapp.domain.repository

import com.tuusuario.trainapp.data.db.entities.MuscleGroup
import com.tuusuario.trainapp.data.db.entities.ExercisePattern
import com.tuusuario.trainapp.data.db.entities.Equipment
import com.tuusuario.trainapp.domain.model.ExerciseCatalog
import com.tuusuario.trainapp.domain.model.ExerciseCategory
import kotlinx.coroutines.flow.Flow

interface ExerciseRepository {
    // Catalog
    fun getAll(): Flow<List<ExerciseCatalog>>
    suspend fun getAllSync(): List<ExerciseCatalog>
    suspend fun getById(id: Long): ExerciseCatalog?
    fun getByCategory(categoryId: Long): Flow<List<ExerciseCatalog>>
    fun getByMuscle(muscle: MuscleGroup): Flow<List<ExerciseCatalog>>
    fun getByPattern(pattern: ExercisePattern): Flow<List<ExerciseCatalog>>
    fun getByEquipment(equipment: Equipment): Flow<List<ExerciseCatalog>>
    fun getCustom(): Flow<List<ExerciseCatalog>>
    fun search(query: String): Flow<List<ExerciseCatalog>>
    fun getRecentCustom(limit: Int): Flow<List<ExerciseCatalog>>
    suspend fun insert(exercise: ExerciseCatalog): Long
    suspend fun update(exercise: ExerciseCatalog)
    suspend fun delete(id: Long)
    suspend fun deleteAllCustom()

    // Categories
    fun getCategories(): Flow<List<ExerciseCategory>>
    suspend fun getAllCategoriesSync(): List<ExerciseCategory>
    suspend fun getCategoryById(id: Long): ExerciseCategory?
    suspend fun insertCategory(category: ExerciseCategory): Long
    suspend fun updateCategory(category: ExerciseCategory)
    suspend fun deleteCategory(id: Long)
    suspend fun deleteAllCustomCategories()
}