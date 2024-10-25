package com.eyther.lumbridge.mapper.finance

import com.eyther.lumbridge.R
import com.eyther.lumbridge.domain.model.finance.NetSalary
import com.eyther.lumbridge.domain.model.finance.allocation.MoneyAllocation
import com.eyther.lumbridge.domain.model.finance.allocation.MoneyAllocationType
import com.eyther.lumbridge.domain.model.finance.deduction.Deduction
import com.eyther.lumbridge.domain.model.finance.deduction.DeductionType
import com.eyther.lumbridge.extensions.kotlin.forceTwoDecimalsPlaces
import com.eyther.lumbridge.model.finance.DeductionUi
import com.eyther.lumbridge.model.finance.MoneyAllocationUi
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
        moneyAllocations = moneyAllocation?.map { it.toUi() }
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

private fun getLabelForDeductionType(type: DeductionType): Int {
    return when (type) {
        DeductionType.PortugalDeductionType.IRS -> R.string.irs
        DeductionType.PortugalDeductionType.SocialSecurity -> R.string.social_security
    }
}

fun MoneyAllocation.toUi(): MoneyAllocationUi {
    return MoneyAllocationUi(
        amount = amount,
        label = getLabelForMoneyAllocationType(type)
    )
}

private fun getLabelForMoneyAllocationType(type: MoneyAllocationType): Int {
    return when (type) {
        MoneyAllocationType.Luxuries -> R.string.luxuries
        MoneyAllocationType.Necessities -> R.string.necessities
        MoneyAllocationType.Savings -> R.string.savings
    }
}
