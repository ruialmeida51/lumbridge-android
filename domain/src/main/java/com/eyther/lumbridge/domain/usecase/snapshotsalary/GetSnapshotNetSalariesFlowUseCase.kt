package com.eyther.lumbridge.domain.usecase.snapshotsalary

import com.eyther.lumbridge.domain.model.snapshotsalary.SnapshotNetSalaryDomain
import com.eyther.lumbridge.domain.repository.snapshotsalary.SnapshotSalaryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSnapshotNetSalariesFlowUseCase @Inject constructor(
    private val snapshotSalaryRepository: SnapshotSalaryRepository
) {
    operator fun invoke(): Flow<List<SnapshotNetSalaryDomain>> = snapshotSalaryRepository.snapshotNetSalaryFlow
}
