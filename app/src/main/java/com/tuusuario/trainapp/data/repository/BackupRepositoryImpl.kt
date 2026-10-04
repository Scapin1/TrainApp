package com.tuusuario.trainapp.data.repository

import com.tuusuario.trainapp.domain.repository.BackupRepository
import com.tuusuario.trainapp.domain.repository.BackupImportReport
import com.tuusuario.trainapp.domain.repository.BackupRepository.Result

class BackupRepositoryImpl : BackupRepository {

    override suspend fun exportFullBackup(): BackupRepository.Result<String> {
        // TODO: Implement full backup export
        return BackupRepository.Result.Failure(UnsupportedOperationException("Not implemented"))
    }

    override suspend fun importFullBackup(filePath: String): BackupRepository.Result<BackupImportReport> {
        // TODO: Implement full backup import
        return BackupRepository.Result.Failure(UnsupportedOperationException("Not implemented"))
    }

    override suspend fun scheduleAutoBackup() {
        // TODO: Implement auto backup scheduling
    }

    override fun getLastBackupTime(): kotlinx.coroutines.flow.Flow<Long?> {
        // TODO: Implement
        return kotlinx.coroutines.flow.flowOf(null)
    }

    override suspend fun getLastBackupTimeSync(): Long? = null

    override suspend fun cleanupOldBackups(keepCount: Int) {
        // TODO: Implement
    }
}