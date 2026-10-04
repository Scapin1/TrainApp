package com.tuusuario.trainapp.domain.repository

import com.tuusuario.trainapp.domain.model.BodyWeight
import com.tuusuario.trainapp.domain.model.GlobalSensation
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface BodyRepository {
    // Body Weight
    fun getAllWeights(): Flow<List<BodyWeight>>
    suspend fun getAllWeightsSync(): List<BodyWeight>
    suspend fun getLatestWeight(): BodyWeight?
    suspend fun getByDate(date: LocalDate): BodyWeight?
    fun getByWeek(start: LocalDate): Flow<List<BodyWeight>>
    suspend fun insertWeight(weight: BodyWeight): Long
    suspend fun updateWeight(weight: BodyWeight)
    suspend fun deleteWeight(id: Long)
    suspend fun shouldNotifyWeighIn(): Boolean

    // Global Sensations
    fun getAllSensations(): Flow<List<GlobalSensation>>
    suspend fun getLatestSensation(): GlobalSensation?
    suspend fun getByDate(date: LocalDate, isStart: Boolean): GlobalSensation?
    suspend fun insertSensation(sensation: GlobalSensation): Long
}