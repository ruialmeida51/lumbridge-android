package com.eyther.lumbridge.data.repository.snapshotsalary

import com.eyther.lumbridge.domain.repository.snapshotsalary.SnapshotSalaryRepository
import com.eyther.lumbridge.data.datasource.snapshotsalary.local.SnapshotSalaryLocalDataSource
import com.eyther.lumbridge.domain.mapper.snapshotsalary.toCached
import com.eyther.lumbridge.domain.mapper.snapshotsalary.toDomain
import com.eyther.lumbridge.domain.model.snapshotsalary.SnapshotNetSalaryDomain
import com.eyther.lumbridge.shared.di.model.Schedulers
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SnapshotSalaryRepositoryImpl @Inject constructor(
    private val snapshotSalaryLocalDataSource: SnapshotSalaryLocalDataSource,
    private val schedulers: Schedulers
) : SnapshotSalaryRepository {
    override val snapshotNetSalaryFlow = snapshotSalaryLocalDataSource
        .snapshotNetSalaryFlow
        .mapNotNull { it.toDomain() }

    override suspend fun saveSnapshotNetSalary(snapshotNetSalary: SnapshotNetSalaryDomain) = withContext(schedulers.io) {
        snapshotSalaryLocalDataSource.saveSnapshotNetSalary(snapshotNetSalary.toCached())
    }

    override suspend fun getSnapshotNetSalaryByYearMonth(year: Int, month: Int): SnapshotNetSalaryDomain? = withContext(schedulers.io) {
        snapshotSalaryLocalDataSource.getSnapshotNetSalaryByYearMonth(year, month)?.toDomain()
    }

    override suspend fun getAllSnapshotNetSalaries(): List<SnapshotNetSalaryDomain> = withContext(schedulers.io) {
        snapshotSalaryLocalDataSource.getAllSnapshotNetSalaries().map { it.toDomain() }
    }
}
