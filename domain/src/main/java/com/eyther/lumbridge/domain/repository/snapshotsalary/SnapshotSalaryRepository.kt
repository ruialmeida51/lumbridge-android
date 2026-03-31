package com.eyther.lumbridge.domain.repository.snapshotsalary

import com.eyther.lumbridge.domain.mapper.snapshotsalary.toCached
import com.eyther.lumbridge.domain.mapper.snapshotsalary.toDomain
import com.eyther.lumbridge.domain.model.snapshotsalary.SnapshotNetSalaryDomain
import com.eyther.lumbridge.shared.di.model.Schedulers
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface SnapshotSalaryRepository {
    val snapshotNetSalaryFlow
    suspend fun saveSnapshotNetSalary(snapshotNetSalary: SnapshotNetSalaryDomain)
    suspend fun getSnapshotNetSalaryByYearMonth(year: Int, month: Int): SnapshotNetSalaryDomain?
    suspend fun getAllSnapshotNetSalaries(): List<SnapshotNetSalaryDomain>
}
