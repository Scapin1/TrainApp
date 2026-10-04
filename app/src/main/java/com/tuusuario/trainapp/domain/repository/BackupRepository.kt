package com.tuusuario.trainapp.domain.repository

import kotlinx.coroutines.flow.Flow

interface BackupRepository {
    suspend fun exportFullBackup(): Result<String>
    suspend fun importFullBackup(filePath: String): Result<BackupImportReport>
    suspend fun scheduleAutoBackup()
    fun getLastBackupTime(): Flow<Long?>
    suspend fun getLastBackupTimeSync(): Long?
    suspend fun cleanupOldBackups(keepCount: Int)

    sealed class Result<out T> {
        data class Success<out T>(val value: T) : Result<T>()
        data class Failure(val exception: Throwable) : Result<Nothing>()
    }
}

data class BackupImportReport(
    val imported: Boolean,
    val routinesCount: Int,
    val workoutsCount: Int,
    val bodyWeightsCount: Int,
    val settingsCount: Int,
    val errors: List<String>
)