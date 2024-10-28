package com.eyther.lumbridge.usecase.snapshotsalary

import com.eyther.lumbridge.domain.repository.snapshotsalary.SnapshotSalaryRepository
import com.eyther.lumbridge.mapper.snapshotsalary.toDomain
import com.eyther.lumbridge.model.snapshotsalary.SnapshotNetSalaryUi
import java.time.LocalDate
import javax.inject.Inject

class SaveSnapshotNetSalaryUseCase @Inject constructor(
    private val snapshotSalaryRepository: SnapshotSalaryRepository,
) {
    suspend operator fun invoke(netSalary: Float) {
        val now = LocalDate.now()

        val currentSnapshotSalaryForDate =
            snapshotSalaryRepository.getSnapshotNetSalaryByYearMonth(now.year, now.monthValue)

        if (currentSnapshotSalaryForDate != null) {
            snapshotSalaryRepository.saveSnapshotNetSalary(
                currentSnapshotSalaryForDate.copy(netSalary = netSalary)
            )
        } else {
            val snapshotNetSalaryUi = SnapshotNetSalaryUi(
                year = now.year,
                month = now.monthValue,
                netSalary = netSalary
            )

            snapshotSalaryRepository.saveSnapshotNetSalary(snapshotNetSalaryUi.toDomain())
        }
    }
}
