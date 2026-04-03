package com.eyther.lumbridge.data.repository.netsalary.portugal

import com.eyther.lumbridge.data.datasource.netsalary.local.PortugalIrsBracketsLocalDataSource
import com.eyther.lumbridge.data.mapper.netsalary.toCached
import com.eyther.lumbridge.data.model.netsalary.local.portugal.PortugalFlatRateCached
import com.eyther.lumbridge.data.model.netsalary.local.portugal.PortugalTaxPercentageCached
import com.eyther.lumbridge.domain.model.netsalary.NetSalary
import com.eyther.lumbridge.domain.model.netsalary.allocation.MoneyAllocation
import com.eyther.lumbridge.domain.model.netsalary.allocation.MoneyAllocationType
import com.eyther.lumbridge.domain.model.netsalary.deduction.Deduction
import com.eyther.lumbridge.domain.model.netsalary.deduction.DeductionType
import com.eyther.lumbridge.domain.model.netsalary.deduction.DuodecimosType
import com.eyther.lumbridge.domain.model.netsalary.percountry.portugal.PortugalIrsTableType
import com.eyther.lumbridge.domain.model.netsalary.percountry.portugal.PortugalTaxDeductions
import com.eyther.lumbridge.domain.model.user.UserFinancialsDomain
import com.eyther.lumbridge.domain.repository.netsalary.NetSalaryCalculator
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

    override suspend fun calculateAnnualSalary(monthlySalary: Float): Float {
        return if (monthlySalary == 0f) 0f else monthlySalary * RECEIVING_MONTHS_WITHOUT_DUODECIMOS
    }

    override suspend fun calculateMonthlySalary(annualSalary: Float): Float {
        return if (annualSalary == 0f) 0f else annualSalary / RECEIVING_MONTHS_WITHOUT_DUODECIMOS
    }

    override suspend fun calculate(userFinancialsDomain: UserFinancialsDomain): NetSalary {
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

    private fun getMoneyAllocation(
        savingsPercentage: Float?,
        necessitiesPercentage: Float?,
        luxuriesPercentage: Float?,
        netSalary: Float
    ): List<MoneyAllocation>? {
        val moneyAllocations = listOf(savingsPercentage, necessitiesPercentage, luxuriesPercentage)
        if (moneyAllocations.mapNotNull { it }.sum() > 100 || moneyAllocations.all { it == null }) return null

        return listOfNotNull(
            savingsPercentage?.let { MoneyAllocation(type = MoneyAllocationType.Savings, percentage = it, netSalary = netSalary) },
            necessitiesPercentage?.let { MoneyAllocation(type = MoneyAllocationType.Necessities, percentage = it, netSalary = netSalary) },
            luxuriesPercentage?.let { MoneyAllocation(type = MoneyAllocationType.Luxuries, percentage = it, netSalary = netSalary) }
        )
    }

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

        val actualMonthlyGrossSalary = getMonthlyGrossSalaryConsideringDuodecimos(userFinancialsDomain = userFinancialsDomain)
        val socialSecurityDeductions = deductSocialSecurityFromSalary(monthlyGrossSalary = actualMonthlyGrossSalary)
        val monthlyNetSalary = actualMonthlyGrossSalary - (actualMonthlyGrossSalary * deductionPercentage).toInt() - socialSecurityDeductions

        return PortugalTaxDeductions(
            netSalary = monthlyNetSalary,
            irsDeductionValue = deductionValue,
            irsBracketPercentage = deductionPercentage,
            ssDeductionValue = socialSecurityDeductions,
            ssDeductionPercentage = SOCIAL_SECURITY_DEDUCTION
        )
    }

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

    private fun deductSocialSecurityFromSalary(monthlyGrossSalary: Float): Float {
        return monthlyGrossSalary * SOCIAL_SECURITY_DEDUCTION
    }

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
