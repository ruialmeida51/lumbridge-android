package com.eyther.lumbridge.domain.repository.netsalary.portugal

import com.eyther.lumbridge.domain.model.finance.NetSalary
import com.eyther.lumbridge.domain.model.finance.allocation.MoneyAllocation
import com.eyther.lumbridge.domain.model.finance.allocation.MoneyAllocationType
import com.eyther.lumbridge.domain.model.finance.deduction.Deduction
import com.eyther.lumbridge.domain.model.finance.deduction.DeductionType
import com.eyther.lumbridge.domain.model.user.UserFinancialsDomain
import com.eyther.lumbridge.domain.repository.netsalary.NetSalaryCalculator
import com.eyther.lumbridge.domain.repository.netsalary.portugal.irs.model.PortugalIrsBracketType
import javax.inject.Inject
import kotlin.math.ceil
import kotlin.math.floor

class PortugalNetSalaryCalculator @Inject constructor() : NetSalaryCalculator {
    companion object {
        private const val WORKING_DAYS_IN_MONTH = 22f
        private const val RECEIVING_MONTHS_WITHOUT_DUODECIMOS = 14
    }

    /**
     * Calculates the annual salary based on the monthly salary.
     * In Portugal you get paid 14 months.
     *
     * @param monthlySalary the monthly salary to calculate the annual salary from
     *
     * @return the annual salary
     */
    override fun calculateAnnualSalary(monthlySalary: Float): Float {
        return if (monthlySalary == 0f) {
            0f
        } else {
            monthlySalary * RECEIVING_MONTHS_WITHOUT_DUODECIMOS
        }
    }

    /**
     * Calculates the monthly salary based on the annual salary.
     * In Portugal you get paid 14 months.
     *
     * @param annualSalary the annual salary to calculate the monthly salary from
     *
     * @return the monthly salary
     */
    override fun calculateMonthlySalary(annualSalary: Float): Float {
        return if (annualSalary == 0f) {
            0f
        } else {
            annualSalary / RECEIVING_MONTHS_WITHOUT_DUODECIMOS
        }
    }

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
        val foodCardMonthly = userFinancialsDomain.foodCardPerDiem * WORKING_DAYS_IN_MONTH
        val portugalIrsBracket =
            PortugalIrsBracketType.of(userFinancialsDomain).getIrsBracket(userFinancialsDomain)

        return NetSalary(
            monthlyGrossSalary = userFinancialsDomain.annualGrossSalary / RECEIVING_MONTHS_WITHOUT_DUODECIMOS,
            monthlyNetSalary = portugalIrsBracket.netSalary,
            annualGrossSalary = userFinancialsDomain.annualGrossSalary,
            annualNetSalary = portugalIrsBracket.netSalary * RECEIVING_MONTHS_WITHOUT_DUODECIMOS,
            monthlyFoodCard = ceil(foodCardMonthly),
            dailyFoodCard = userFinancialsDomain.foodCardPerDiem,
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
