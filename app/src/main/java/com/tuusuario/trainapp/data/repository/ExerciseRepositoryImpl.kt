package com.tuusuario.trainapp.data.repository

import com.tuusuario.trainapp.data.db.AppDatabase
import com.tuusuario.trainapp.data.db.dao.ExerciseCatalogDao
import com.tuusuario.trainapp.data.db.dao.ExerciseCategoryDao
import com.tuusuario.trainapp.data.db.entities.ExerciseCatalog
import com.tuusuario.trainapp.data.db.entities.ExerciseCategory
import com.tuusuario.trainapp.data.mapper.ExerciseMapper
import com.tuusuario.trainapp.data.mapper.ExerciseCategoryMapper
import com.tuusuario.trainapp.domain.model.ExerciseCatalog as DomainExerciseCatalog
import com.tuusuario.trainapp.domain.model.ExerciseCategory as DomainExerciseCategory
import com.tuusuario.trainapp.domain.repository.ExerciseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ExerciseRepositoryImpl(
    private val database: AppDatabase
) : ExerciseRepository {

    private val exerciseCatalogDao: ExerciseCatalogDao = database.exerciseCatalogDao()
    private val exerciseCategoryDao: ExerciseCategoryDao = database.exerciseCategoryDao()

    // Catalog
    override fun getAll(): Flow<List<DomainExerciseCatalog>> =
        exerciseCatalogDao.getAll().map { entities: List<ExerciseCatalog> ->
            entities.map { e: ExerciseCatalog -> ExerciseMapper.toDomain(e) }
        }

    override suspend fun getAllSync(): List<DomainExerciseCatalog> =
        exerciseCatalogDao.getAllSync().map { e: ExerciseCatalog -> ExerciseMapper.toDomain(e) }

    override suspend fun getById(id: Long): DomainExerciseCatalog? =
        exerciseCatalogDao.getById(id)?.let { e: ExerciseCatalog -> ExerciseMapper.toDomain(e) }

    override fun getByCategory(categoryId: Long): Flow<List<DomainExerciseCatalog>> =
        exerciseCatalogDao.getByCategory(categoryId).map { entities: List<ExerciseCatalog> ->
            entities.map { e: ExerciseCatalog -> ExerciseMapper.toDomain(e) }
        }

    override fun getByMuscle(muscle: com.tuusuario.trainapp.data.db.entities.MuscleGroup): Flow<List<DomainExerciseCatalog>> =
        exerciseCatalogDao.getByMuscle(muscle).map { entities: List<ExerciseCatalog> ->
            entities.map { e: ExerciseCatalog -> ExerciseMapper.toDomain(e) }
        }

    override fun getByPattern(pattern: com.tuusuario.trainapp.data.db.entities.ExercisePattern): Flow<List<DomainExerciseCatalog>> =
        exerciseCatalogDao.getByPattern(pattern).map { entities: List<ExerciseCatalog> ->
            entities.map { e: ExerciseCatalog -> ExerciseMapper.toDomain(e) }
        }

    override fun getByEquipment(equipment: com.tuusuario.trainapp.data.db.entities.Equipment): Flow<List<DomainExerciseCatalog>> =
        exerciseCatalogDao.getByEquipment(equipment).map { entities: List<ExerciseCatalog> ->
            entities.map { e: ExerciseCatalog -> ExerciseMapper.toDomain(e) }
        }

    override fun getCustom(): Flow<List<DomainExerciseCatalog>> =
        exerciseCatalogDao.getByCustom(true).map { entities: List<ExerciseCatalog> ->
            entities.map { e: ExerciseCatalog -> ExerciseMapper.toDomain(e) }
        }

    override fun search(query: String): Flow<List<DomainExerciseCatalog>> =
        exerciseCatalogDao.search("%$query%").map { entities: List<ExerciseCatalog> ->
            entities.map { e: ExerciseCatalog -> ExerciseMapper.toDomain(e) }
        }

    override fun getRecentCustom(limit: Int): Flow<List<DomainExerciseCatalog>> =
        exerciseCatalogDao.getRecentCustom(limit).map { entities: List<ExerciseCatalog> ->
            entities.map { e: ExerciseCatalog -> ExerciseMapper.toDomain(e) }
        }

    override suspend fun insert(exercise: com.tuusuario.trainapp.domain.model.ExerciseCatalog): Long {
        val entity = ExerciseMapper.toEntity(exercise)
        return exerciseCatalogDao.insert(entity)
    }

    override suspend fun update(exercise: com.tuusuario.trainapp.domain.model.ExerciseCatalog) {
        val entity = ExerciseMapper.toEntity(exercise)
        exerciseCatalogDao.update(entity)
    }

    override suspend fun delete(id: Long) {
        exerciseCatalogDao.deleteById(id)
    }

    override suspend fun deleteAllCustom() {
        exerciseCatalogDao.deleteAllCustom()
    }

    // Categories
    override fun getCategories(): Flow<List<com.tuusuario.trainapp.domain.model.ExerciseCategory>> =
        database.exerciseCategoryDao().getAll().map { entities: List<ExerciseCategory> ->
            entities.map { e: ExerciseCategory -> ExerciseCategoryMapper.toDomain(e) }
        }

    override suspend fun getAllCategoriesSync(): List<com.tuusuario.trainapp.domain.model.ExerciseCategory> =
        database.exerciseCategoryDao().getAllSync().map { e: ExerciseCategory -> ExerciseCategoryMapper.toDomain(e) }

    override suspend fun getCategoryById(id: Long): com.tuusuario.trainapp.domain.model.ExerciseCategory? =
        database.exerciseCategoryDao().getById(id)?.let { e: ExerciseCategory -> ExerciseCategoryMapper.toDomain(e) }

    override suspend fun insertCategory(category: com.tuusuario.trainapp.domain.model.ExerciseCategory): Long {
        val entity = ExerciseCategoryMapper.toEntity(category)
        return database.exerciseCategoryDao().insert(entity)
    }

    override suspend fun updateCategory(category: com.tuusuario.trainapp.domain.model.ExerciseCategory) {
        val entity = ExerciseCategoryMapper.toEntity(category)
        database.exerciseCategoryDao().update(entity)
    }

    override suspend fun deleteCategory(id: Long) {
        database.exerciseCategoryDao().deleteById(id)
    }

    override suspend fun deleteAllCustomCategories() {
        database.exerciseCategoryDao().deleteAllCustom()
    }
}