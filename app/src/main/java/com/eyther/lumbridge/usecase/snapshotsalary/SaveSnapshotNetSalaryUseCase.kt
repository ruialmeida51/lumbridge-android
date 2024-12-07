package com.eyther.lumbridge.usecase.snapshotsalary

import com.eyther.lumbridge.domain.model.netsalary.allocation.MoneyAllocation
import com.eyther.lumbridge.domain.model.netsalary.allocation.MoneyAllocationType
import com.eyther.lumbridge.domain.model.snapshotsalary.SnapshotNetSalaryDomain
import com.eyther.lumbridge.domain.model.user.UserFinancialsDomain
import com.eyther.lumbridge.domain.repository.snapshotsalary.SnapshotSalaryRepository
import com.eyther.lumbridge.mapper.user.toDomain
import com.eyther.lumbridge.model.user.UserFinancialsUi
import com.eyther.lumbridge.usecase.finance.GetNetSalaryUseCase
import java.time.LocalDate
import javax.inject.Inject

class SaveSnapshotNetSalaryUseCase @Inject constructor(
    private val snapshotSalaryRepository: SnapshotSalaryRepository,
    private val getNetSalaryUseCase: GetNetSalaryUseCase
) {
    suspend operator fun invoke(
        userFinancialsUi: UserFinancialsUi
    ) {
        val now = LocalDate.now()

        val netSalary = getNetSalaryUseCase(userFinancialsUi)
        val userFinancials = userFinancialsUi.toDomain()

        val currentSnapshotSalaryForDate =
            snapshotSalaryRepository.getSnapshotNetSalaryByYearMonth(now.year, now.monthValue)

        if (currentSnapshotSalaryForDate != null) {
            snapshotSalaryRepository.saveSnapshotNetSalary(
                currentSnapshotSalaryForDate.copy(
                    netSalary = netSalary.monthlyNetSalary,
                    moneyAllocations = getMoneyAllocations(userFinancials, netSalary.monthlyNetSalary),
                    foodCardAmount = netSalary.monthlyFoodCard
                )
            )
        } else {
            val snapshotNetSalary = SnapshotNetSalaryDomain(
                year = now.year,
                month = now.monthValue,
                netSalary = netSalary.monthlyNetSalary,
                moneyAllocations = getMoneyAllocations(userFinancials, netSalary.monthlyNetSalary),
                foodCardAmount = netSalary.monthlyFoodCard
            )

            snapshotSalaryRepository.saveSnapshotNetSalary(snapshotNetSalary)
        }
    }

    private fun getMoneyAllocations(userFinancials: UserFinancialsDomain, netSalary: Float): List<MoneyAllocation> {
        return listOfNotNull(
            userFinancials.savingsPercentage?.let { MoneyAllocation(MoneyAllocationType.Savings, it, netSalary) },
            userFinancials.necessitiesPercentage?.let { MoneyAllocation(MoneyAllocationType.Necessities, it, netSalary) },
            userFinancials.luxuriesPercentage?.let { MoneyAllocation(MoneyAllocationType.Luxuries, it, netSalary) }
        )
    }
}
