package com.eyther.lumbridge.usecase.snapshotsalary

import com.eyther.lumbridge.domain.repository.snapshotsalary.SnapshotSalaryRepository
import com.eyther.lumbridge.mapper.snapshotsalary.toDomain
import com.eyther.lumbridge.model.snapshotsalary.SnapshotNetSalaryUi
import javax.inject.Inject

class SaveSnapshotNetSalaryUseCase @Inject constructor(
    private val snapshotSalaryRepository: SnapshotSalaryRepository
) {
    suspend operator fun invoke(snapshotNetSalaryUi: SnapshotNetSalaryUi) {
        snapshotSalaryRepository.saveSnapshotNetSalary(snapshotNetSalaryUi.toDomain())
    }
}
