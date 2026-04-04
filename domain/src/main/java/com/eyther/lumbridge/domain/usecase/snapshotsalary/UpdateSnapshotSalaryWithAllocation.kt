package com.eyther.lumbridge.domain.usecase.snapshotsalary

import com.eyther.lumbridge.domain.model.netsalary.allocation.MoneyAllocation
import com.eyther.lumbridge.domain.model.netsalary.allocation.MoneyAllocationType
import com.eyther.lumbridge.domain.model.user.UserFinancialsDomain
import com.eyther.lumbridge.domain.repository.snapshotsalary.SnapshotSalaryRepository
import java.time.LocalDate
import javax.inject.Inject

class UpdateSnapshotSalaryWithAllocation @Inject constructor(
    private val snapshotSalaryRepository: SnapshotSalaryRepository
) {
    suspend operator fun invoke(
        userFinancialsDomain: UserFinancialsDomain,
        monthlyNetSalary: Float,
        foodCardAmount: Float,
        snapshotYear: Int?,
        snapshotMonth: Int?
    ) {
        val now = LocalDate.now()
        val currentSnapshotSalaryForDate = snapshotSalaryRepository.getSnapshotNetSalaryByYearMonth(
            year = snapshotYear ?: now.year,
            month = snapshotMonth ?: now.monthValue
        ) ?: return

        snapshotSalaryRepository.saveSnapshotNetSalary(
            currentSnapshotSalaryForDate.copy(
                netSalary = monthlyNetSalary,
                moneyAllocations = getMoneyAllocations(userFinancialsDomain, monthlyNetSalary),
                foodCardAmount = foodCardAmount
            )
        )
    }

    private fun getMoneyAllocations(userFinancials: UserFinancialsDomain, netSalary: Float): List<MoneyAllocation> {
        return listOfNotNull(
            userFinancials.savingsPercentage?.let { MoneyAllocation(MoneyAllocationType.Savings, it, netSalary) },
            userFinancials.necessitiesPercentage?.let { MoneyAllocation(MoneyAllocationType.Necessities, it, netSalary) },
            userFinancials.luxuriesPercentage?.let { MoneyAllocation(MoneyAllocationType.Luxuries, it, netSalary) }
        )
    }
}
