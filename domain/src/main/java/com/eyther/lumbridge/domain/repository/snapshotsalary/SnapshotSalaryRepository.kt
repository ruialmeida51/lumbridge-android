package com.eyther.lumbridge.domain.repository.snapshotsalary

import com.eyther.lumbridge.domain.model.snapshotsalary.SnapshotNetSalaryDomain
import kotlinx.coroutines.flow.Flow

interface SnapshotSalaryRepository {
    val snapshotNetSalaryFlow: Flow<List<SnapshotNetSalaryDomain>>
    suspend fun saveSnapshotNetSalary(snapshotNetSalary: SnapshotNetSalaryDomain)
    suspend fun getSnapshotNetSalaryByYearMonth(year: Int, month: Int): SnapshotNetSalaryDomain?
    suspend fun getAllSnapshotNetSalaries(): List<SnapshotNetSalaryDomain>
}
