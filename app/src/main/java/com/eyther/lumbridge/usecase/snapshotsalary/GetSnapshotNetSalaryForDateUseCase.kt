package com.eyther.lumbridge.usecase.snapshotsalary

import com.eyther.lumbridge.domain.repository.snapshotsalary.SnapshotSalaryRepository
import com.eyther.lumbridge.mapper.snapshotsalary.toUi
import javax.inject.Inject

class GetSnapshotNetSalaryForDateUseCase @Inject constructor(
    private val snapshotSalaryRepository: SnapshotSalaryRepository
) {
    suspend operator fun invoke(year: Int, month: Int): Float {
        return snapshotSalaryRepository.getSnapshotNetSalaryByYearMonth(year, month)
            ?.toUi()
            ?.netSalary ?: 0f
    }
}
