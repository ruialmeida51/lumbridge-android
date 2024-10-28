package com.eyther.lumbridge.usecase.snapshotsalary

import com.eyther.lumbridge.domain.repository.snapshotsalary.SnapshotSalaryRepository
import javax.inject.Inject

class GetAllSnapshotNetSalaryUseCase @Inject constructor(
    private val snapshotSalaryRepository: SnapshotSalaryRepository
) {
    suspend operator fun invoke(year: Int, month: Int): Float {
        return snapshotSalaryRepository.getSnapshotNetSalaryByYearMonth(year, month)?.netSalary ?: 0f
    }
}
