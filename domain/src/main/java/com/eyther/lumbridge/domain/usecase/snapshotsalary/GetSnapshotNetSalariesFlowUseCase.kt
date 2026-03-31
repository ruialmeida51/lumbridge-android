package com.eyther.lumbridge.domain.usecase.snapshotsalary

import com.eyther.lumbridge.domain.repository.snapshotsalary.SnapshotSalaryRepository
import com.eyther.lumbridge.domain.mapper.snapshotsalary.toUi
import com.eyther.lumbridge.domain.model.snapshotsalary.SnapshotNetSalaryUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetSnapshotNetSalariesFlowUseCase @Inject constructor(
    private val snapshotSalaryRepository: SnapshotSalaryRepository
) {
    operator fun invoke(): Flow<List<SnapshotNetSalaryUi>> = snapshotSalaryRepository
        .snapshotNetSalaryFlow
        .map { snapshotSalaries ->
            snapshotSalaries
                .map { snapshot -> snapshot.toUi() }
        }
}
