package com.eyther.lumbridge.domain.repository.finance.portugal

import com.eyther.lumbridge.domain.model.finance.Deduction
import com.eyther.lumbridge.domain.model.finance.DeductionType
import com.eyther.lumbridge.domain.model.finance.MoneyAllocation
import com.eyther.lumbridge.domain.model.finance.MoneyAllocationType
import com.eyther.lumbridge.domain.model.finance.NetSalary
import com.eyther.lumbridge.domain.model.user.UserFinancialsDomain
import com.eyther.lumbridge.domain.repository.finance.NetSalaryCalculator
import com.eyther.lumbridge.domain.repository.finance.portugal.irs.model.PortugalIrsBracketType
import javax.inject.Inject
import kotlin.math.ceil
import kotlin.math.floor

class PortugalNetSalaryCalculator @Inject constructor() : NetSalaryCalculator {
    /**
     * Calculates the net salary for Portugal and saves the deductions made.
     * In Portugal, typically, you get paid 14 months.
     *
     * TODO - Future features:
     *  * Does not yet support 'Duodécimos' or diluting the
     *   'Subsídio de Natal' and 'Subsídio de Férias' over 12 months.
     *
     * @param userFinancialsDomain the user financials to calculate the net salary from
     * @return the net salary and the deductions made
     */
    override fun calculate(
        userFinancialsDomain: UserFinancialsDomain
    ): NetSalary {
        val foodCardMonthly = userFinancialsDomain.foodCardPerDiem * 22f
        val portugalIrsBracket =
            PortugalIrsBracketType.of(userFinancialsDomain).getIrsBracket(userFinancialsDomain)

        return NetSalary(
            // We round up to the nearest integer in Portugal.
            salary = portugalIrsBracket.netSalary,
            foodCard = ceil(foodCardMonthly),
            deductions = getDeductions(
                irsDeduction = portugalIrsBracket.irsDeductionValue,
                ssDeduction = portugalIrsBracket.ssDeductionValue,
                flatRate = portugalIrsBracket.flatRate,
                irsBracket = portugalIrsBracket.irsBracketPercentage,
                ssBracket = portugalIrsBracket.ssDeductionPercentage
            ),
            moneyAllocation = getMoneyAllocation(
                savingsPercentage = userFinancialsDomain.savingsPercentage,
                necessitiesPercentage = userFinancialsDomain.necessitiesPercentage,
                luxuriesPercentage = userFinancialsDomain.luxuriesPercentage,
                netSalary = portugalIrsBracket.netSalary
            )
        )
    }

    /**
     * Returns the deductions made for the net salary.
     */
    private fun getDeductions(
        irsDeduction: Float,
        ssDeduction: Float,
        flatRate: Float,
        irsBracket: Float,
        ssBracket: Float
    ) = listOf(
        Deduction.PercentageDeduction(
            type = DeductionType.PortugalDeductionType.IRS,
            value = floor(irsDeduction),
            percentage = irsBracket
        ),
        Deduction.PercentageDeduction(
            type = DeductionType.PortugalDeductionType.SocialSecurity,
            value = floor(ssDeduction),
            percentage = ssBracket
        ),
        Deduction.FlatDeduction(
            type = DeductionType.PortugalDeductionType.Flat,
            value = floor(flatRate)
        )
    )


    /**
     * Returns the money allocation for the net salary.
     */
    private fun getMoneyAllocation(
        savingsPercentage: Int?,
        necessitiesPercentage: Int?,
        luxuriesPercentage: Int?,
        netSalary: Float
    ): List<MoneyAllocation>? {

        val moneyAllocations = listOf(savingsPercentage, necessitiesPercentage, luxuriesPercentage)

        if (moneyAllocations.mapNotNull { it }.sum() > 100 || moneyAllocations.all { it == null }) {
            return null
        }

        return listOf(
            MoneyAllocation(
                type = MoneyAllocationType.Savings,
                amount = ceil(netSalary * (savingsPercentage?.div(100f) ?: 0f))
            ),
            MoneyAllocation(
                type = MoneyAllocationType.Necessities,
                amount = ceil(netSalary * (necessitiesPercentage?.div(100f) ?: 0f))
            ),
            MoneyAllocation(
                type = MoneyAllocationType.Luxuries,
                amount = ceil(netSalary * (luxuriesPercentage?.div(100f) ?: 0f))
            )
        )
    }
}
