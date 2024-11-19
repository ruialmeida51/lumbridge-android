package com.eyther.lumbridge.mapper.finance

import com.eyther.lumbridge.R
import com.eyther.lumbridge.domain.model.netsalary.NetSalary
import com.eyther.lumbridge.domain.model.netsalary.allocation.MoneyAllocation
import com.eyther.lumbridge.domain.model.netsalary.allocation.MoneyAllocationType
import com.eyther.lumbridge.domain.model.netsalary.deduction.Deduction
import com.eyther.lumbridge.domain.model.netsalary.deduction.DeductionType
import com.eyther.lumbridge.domain.model.netsalary.deduction.DuodecimosType
import com.eyther.lumbridge.extensions.kotlin.forceTwoDecimalsPlaces
import com.eyther.lumbridge.model.finance.DeductionUi
import com.eyther.lumbridge.model.finance.DuodecimosTypeUi
import com.eyther.lumbridge.model.finance.MoneyAllocationTypeUi
import com.eyther.lumbridge.model.finance.NetSalaryUi

fun NetSalary.toUi(): NetSalaryUi {
    return NetSalaryUi(
        annualGrossSalary = annualGrossSalary,
        annualNetSalary = annualNetSalary,
        monthlyGrossSalary = monthlyGrossSalary,
        monthlyNetSalary = monthlyNetSalary,
        monthlyFoodCard = monthlyFoodCard,
        dailyFoodCard = dailyFoodCard,
        deductions = deductions.map { it.toUi() },
        moneyAllocations = moneyAllocation?.map { it.toUi() }?.sortedBy { it.ordinal },
        duodecimosTypeUi = duodecimosType.toUi()
    )
}

fun Deduction.toUi(): DeductionUi {
    return when (this) {
        is Deduction.FlatDeduction -> DeductionUi(
            percentage = null,
            amount = value,
            label = getLabelForDeductionType(type)
        )

        is Deduction.PercentageDeduction -> DeductionUi(
            percentage = (percentage * 100).forceTwoDecimalsPlaces(),
            amount = value,
            label = getLabelForDeductionType(type)
        )
    }
}

fun DuodecimosType.toUi(): DuodecimosTypeUi {
    return when (this) {
        DuodecimosType.TWELVE_MONTHS -> DuodecimosTypeUi.TwelveMonths
        DuodecimosType.THIRTEEN_MONTHS -> DuodecimosTypeUi.ThirteenMonths
        DuodecimosType.FOURTEEN_MONTHS -> DuodecimosTypeUi.FourteenMonths
    }
}

private fun getLabelForDeductionType(type: DeductionType): Int {
    return when (type) {
        DeductionType.PortugalDeductionType.IRS -> R.string.irs
        DeductionType.PortugalDeductionType.SocialSecurity -> R.string.social_security
    }
}

fun MoneyAllocation.toUi(): MoneyAllocationTypeUi = when (type) {
    is MoneyAllocationType.Luxuries -> MoneyAllocationTypeUi.Luxuries(amount)
    is MoneyAllocationType.Necessities -> MoneyAllocationTypeUi.Necessities(amount)
    is MoneyAllocationType.Savings -> MoneyAllocationTypeUi.Savings(amount)
}
