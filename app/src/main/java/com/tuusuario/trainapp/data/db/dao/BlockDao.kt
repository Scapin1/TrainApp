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
import com.tuusuario.trainapp.data.db.entities.Block
import com.tuusuario.trainapp.data.db.entities.TemplateExercise
import kotlinx.coroutines.flow.Flow

@Dao
interface BlockDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(block: Block): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(blocks: List<Block>)

    @Update
    suspend fun update(block: Block): Int

    @Delete
    suspend fun delete(block: Block): Int

    @Query("SELECT * FROM blocks WHERE dayId = :dayId ORDER BY blockIndex")
    fun getByDay(dayId: Long): Flow<List<Block>>

    @Query("SELECT * FROM blocks WHERE dayId = :dayId ORDER BY blockIndex")
    suspend fun getByDaySync(dayId: Long): List<Block>

    @Query("SELECT * FROM blocks WHERE id = :id")
    suspend fun getById(id: Long): Block?

    @Query("SELECT MAX(blockIndex) FROM blocks WHERE dayId = :dayId")
    suspend fun getMaxBlockIndex(dayId: Long): Int?

    @Query("DELETE FROM blocks WHERE dayId = :dayId")
    suspend fun deleteByDay(dayId: Long)

    @Transaction
    @Query("SELECT * FROM blocks WHERE dayId = :dayId ORDER BY blockIndex")
    fun getByDayWithExercises(dayId: Long): Flow<List<BlockWithExercises>>

    @Transaction
    @Query("SELECT * FROM blocks WHERE id = :id")
    fun getByIdWithExercises(id: Long): Flow<BlockWithExercises?>

    @Query("SELECT * FROM blocks ORDER BY dayId, blockIndex")
    suspend fun getAllSync(): List<Block>
}

data class BlockWithExercises(
    @Embedded val block: Block,
    @Relation(
        parentColumn = "id",
        entityColumn = "blockId"
    ) val exercises: List<TemplateExercise>
)