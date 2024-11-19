package com.eyther.lumbridge.usecase.snapshotsalary

import com.eyther.lumbridge.model.snapshotsalary.SnapshotNetSalaryUi
import javax.inject.Inject

/**
 * The method will try to fetch the most up to date net salary snapshot, if it exists, for the given date.
 *
 * @property snapshotSalaryRepository The repository to fetch the snapshot salary from.
 */
class GetMostRecentSnapshotSalaryForDateUseCase @Inject constructor() {
    operator fun invoke(
        snapshotNetSalaries: List<SnapshotNetSalaryUi>,
        year: Int,
        month: Int
    ): SnapshotNetSalaryUi? {
        return snapshotNetSalaries
            .filter { it.year < year || (it.year <= year && it.month <= month) }
            .maxWithOrNull(compareBy({ it.year }, { it.month }))
            // If the year and month are in the past related to the snapshot salaries, we return the last snapshot salary.
            // This is because the snapshot salaries were introduced after the expenses, so we may have cases where
            // we don't have a snapshot salary for a given month. It's a best effort approach.
            ?: snapshotNetSalaries.firstOrNull()
    }
}
