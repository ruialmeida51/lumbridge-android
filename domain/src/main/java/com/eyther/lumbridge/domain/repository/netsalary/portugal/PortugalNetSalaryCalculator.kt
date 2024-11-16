package com.eyther.lumbridge.domain.repository.netsalary.portugal

import com.eyther.lumbridge.data.datasource.netsalary.local.PortugalIrsBracketsLocalDataSource
import com.eyther.lumbridge.data.model.netsalary.local.portugal.PortugalFlatRateCached
import com.eyther.lumbridge.data.model.netsalary.local.portugal.PortugalTaxPercentageCached
import com.eyther.lumbridge.domain.mapper.netsalary.toCached
import com.eyther.lumbridge.domain.model.netsalary.NetSalary
import com.eyther.lumbridge.domain.model.netsalary.allocation.MoneyAllocation
import com.eyther.lumbridge.domain.model.netsalary.allocation.MoneyAllocationType
import com.eyther.lumbridge.domain.model.netsalary.deduction.Deduction
import com.eyther.lumbridge.domain.model.netsalary.deduction.DeductionType
import com.eyther.lumbridge.domain.model.netsalary.deduction.DuodecimosType
import com.eyther.lumbridge.domain.model.user.UserFinancialsDomain
import com.eyther.lumbridge.domain.repository.netsalary.NetSalaryCalculator
import com.eyther.lumbridge.domain.model.netsalary.percountry.portugal.PortugalIrsTableType
import com.eyther.lumbridge.domain.model.netsalary.percountry.portugal.PortugalTaxDeductions
import javax.inject.Inject
import kotlin.math.ceil
import kotlin.math.floor

class PortugalNetSalaryCalculator @Inject constructor(
    private val portugalIrsBracketsLocalDataSource: PortugalIrsBracketsLocalDataSource
) : NetSalaryCalculator {
    companion object {
        private const val WORKING_DAYS_IN_MONTH = 22f
        private const val RECEIVING_MONTHS_WITHOUT_DUODECIMOS = 14
        private const val MONTHS_IN_A_YEAR = 12
        private const val SOCIAL_SECURITY_DEDUCTION = 0.11f
    }

    /**
     * Calculates the annual salary based on the monthly salary.
     * In Portugal you get paid 14 months.
     *
     * @param monthlySalary the monthly salary to calculate the annual salary from
     *
     * @return the annual salary
     */
    override suspend fun calculateAnnualSalary(monthlySalary: Float): Float {
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
    override suspend fun calculateMonthlySalary(annualSalary: Float): Float {
        return if (annualSalary == 0f) {
            0f
        } else {
            annualSalary / RECEIVING_MONTHS_WITHOUT_DUODECIMOS
        }
    }

    /**
     * Calculates the net salary for Portugal and saves the deductions made.
     * In Portugal, typically, you get paid 14 months. But you can also be paid in 12 months or 13 months.
     *
     * @param userFinancialsDomain the user financials to calculate the net salary from
     * @return the net salary and the deductions made
     */
    override suspend fun calculate(
        userFinancialsDomain: UserFinancialsDomain
    ): NetSalary {
        val foodCardMonthly = userFinancialsDomain.foodCardPerDiem * WORKING_DAYS_IN_MONTH
        val deductionsForBracket = getTaxDeductionsForBracket(userFinancialsDomain)
            ?: throw IllegalArgumentException("💥 Couldn't find tax deductions for bracket")

        return NetSalary(
            monthlyGrossSalary = userFinancialsDomain.annualGrossSalary / RECEIVING_MONTHS_WITHOUT_DUODECIMOS,
            monthlyNetSalary = deductionsForBracket.netSalary,
            annualGrossSalary = userFinancialsDomain.annualGrossSalary,
            annualNetSalary = deductionsForBracket.netSalary * RECEIVING_MONTHS_WITHOUT_DUODECIMOS,
            monthlyFoodCard = ceil(foodCardMonthly),
            dailyFoodCard = userFinancialsDomain.foodCardPerDiem,
            deductions = getDeductions(
                irsDeduction = deductionsForBracket.irsDeductionValue,
                ssDeduction = deductionsForBracket.ssDeductionValue,
                irsBracket = deductionsForBracket.irsBracketPercentage,
                ssBracket = deductionsForBracket.ssDeductionPercentage
            ),
            moneyAllocation = getMoneyAllocation(
                savingsPercentage = userFinancialsDomain.savingsPercentage,
                necessitiesPercentage = userFinancialsDomain.necessitiesPercentage,
                luxuriesPercentage = userFinancialsDomain.luxuriesPercentage,
                netSalary = deductionsForBracket.netSalary
            ),
            duodecimosType = userFinancialsDomain.duodecimosType
        )
    }

    /**
     * Returns the deductions made for the net salary.
     */
    private fun getDeductions(
        irsDeduction: Float,
        ssDeduction: Float,
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

    /**
     * Returns the tax deductions for the user financials provided, including the net salary.
     *
     * The formula to calculate the net salary is:
     * R: Monthly gross salary
     * T: Effective tax percentage, not the maximum, calculated at [calculateEffectiveDeductionPercentage]
     * SS: Social Security deduction
     *
     * Net Salary = R - (R * T) - SS
     *
     * @param userFinancialsDomain the user financials to calculate the tax deductions from
     *
     * @return the tax deductions
     */
    private suspend fun getTaxDeductionsForBracket(userFinancialsDomain: UserFinancialsDomain): PortugalTaxDeductions? {
        val userBracketType = PortugalIrsTableType.of(userFinancialsDomain)
        val bracketInfo = portugalIrsBracketsLocalDataSource.getIrsBracketInfo(userBracketType.toCached())
        val monthlyGrossSalary = userFinancialsDomain.annualGrossSalary / RECEIVING_MONTHS_WITHOUT_DUODECIMOS

        val flatRate = bracketInfo.flatRate.find { monthlyGrossSalary >= it.range[0] && monthlyGrossSalary <= it.range[1] } ?: return null
        val taxPercentage = bracketInfo.taxPercentage.find { monthlyGrossSalary >= it.range[0] && monthlyGrossSalary <= it.range[1] } ?: return null

        val (deductionValue, deductionPercentage) = calculateEffectiveDeductionPercentage(
            monthlyGrossSalary = monthlyGrossSalary,
            numberOfDependents = userFinancialsDomain.numberOfDependants,
            flatRateCached = flatRate,
            taxPercentageCached = taxPercentage
        )

        val actualMonthlyGrossSalary = getMonthlyGrossSalaryConsideringDuodecimos(
            userFinancialsDomain = userFinancialsDomain
        )

        val socialSecurityDeductions = deductSocialSecurityFromSalary(
            monthlyGrossSalary = actualMonthlyGrossSalary
        )

        val monthlyNetSalary = actualMonthlyGrossSalary - (actualMonthlyGrossSalary * deductionPercentage).toInt() - socialSecurityDeductions

        return PortugalTaxDeductions(
            netSalary = monthlyNetSalary,
            irsDeductionValue = deductionValue,
            irsBracketPercentage = deductionPercentage,
            ssDeductionValue = socialSecurityDeductions,
            ssDeductionPercentage = SOCIAL_SECURITY_DEDUCTION
        )
    }

    /**
     * Calculates the IRS percentage for the user financials provided.
     *
     * The formula to calculate the IRS deduction is:
     * R: Monthly gross salary
     * T: Tax percentage
     * F: Flat rate
     * A: Additional flat rate per dependent
     * D: Number of dependents
     *
     * IRS Deduction = (R * T) - F - (A * D)
     * Social Security Deduction = R * 0.11
     *
     * It the rule of three to calculate the percentage of the IRS deduction. Example:
     *
     * 3000€ monthly salary; 500€ IRS deduction
     *
     * 3000€ === 100%
     * 500€ === x%
     *
     * x% = (500€ * 100%) / 3000€
     * x% = 16.67%
     *
     * Social security is a flat rate of 11%.
     *
     * @param monthlyGrossSalary the monthly salary to calculate the percentage from, ⚠️ it cannot have duodecimos applied to it
     * @param numberOfDependents the number of dependents the user has
     * @param flatRateCached the flat rate to subtract from the IRS deduction
     * @param taxPercentageCached the tax percentage to apply to the gross monthly salary
     *
     * @return the actual IRS percentage
     */
    private fun calculateEffectiveDeductionPercentage(
        monthlyGrossSalary: Float,
        numberOfDependents: Int?,
        flatRateCached: PortugalFlatRateCached,
        taxPercentageCached: PortugalTaxPercentageCached
    ): Pair<Float, Float> {
        val flatRateToCutDown = flatRateCached.rate
            ?: flatRateCached.formula?.let { it.taxPercentage * it.multiplier * (it.rate - monthlyGrossSalary) }
            ?: 0f

        val flatRateDependents = flatRateCached.perDependentAmount * (numberOfDependents ?: 0)

        val irsDeductions = (monthlyGrossSalary * taxPercentageCached.percentage) - (flatRateToCutDown + flatRateDependents)
        val irsDeductionPercentage = ((100 * irsDeductions) / monthlyGrossSalary) / 100

        return Pair(irsDeductions, irsDeductionPercentage)
    }

    /**
     * Deducts the social security from the salary.
     *
     * Social security is a flat rate of 11%, so we just need to multiply the salary by 0.11.
     */
    private fun deductSocialSecurityFromSalary(monthlyGrossSalary: Float): Float {
        return monthlyGrossSalary * SOCIAL_SECURITY_DEDUCTION
    }

    /**
     * Returns the monthly gross salary for the user financials provided, taking into account the duodecimos.
     *
     * The formula to calculate a duodecimo for 12 months is:
     * A: Annual gross salary
     * M: Months in a year
     * R: Monthly gross salary
     *
     * Duodecimo = A / M - R
     *
     * For 13 months, the formula is:
     * A: Annual gross salary
     * M: Months in a year
     * R: Monthly gross salary
     *
     * Duodecimo = (A / M - R) / 2
     *
     * @param userFinancialsDomain the user financials to calculate the monthly gross salary from
     *
     * @return the monthly gross salary, considering the duodecimos
     */
    private fun getMonthlyGrossSalaryConsideringDuodecimos(userFinancialsDomain: UserFinancialsDomain): Float {
        val grossSalary = userFinancialsDomain.annualGrossSalary / RECEIVING_MONTHS_WITHOUT_DUODECIMOS
        val duoedecimos = (userFinancialsDomain.annualGrossSalary / MONTHS_IN_A_YEAR) - grossSalary

        return when (userFinancialsDomain.duodecimosType) {
            DuodecimosType.TWELVE_MONTHS -> grossSalary + duoedecimos
            DuodecimosType.THIRTEEN_MONTHS -> grossSalary + (duoedecimos / 2)
            DuodecimosType.FOURTEEN_MONTHS -> grossSalary
        }
    }
}
