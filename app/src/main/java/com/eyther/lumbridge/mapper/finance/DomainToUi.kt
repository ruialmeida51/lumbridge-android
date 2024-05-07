package com.eyther.lumbridge.mapper.finance

import com.eyther.lumbridge.domain.model.finance.Deduction
import com.eyther.lumbridge.domain.model.finance.MoneyAllocation
import com.eyther.lumbridge.domain.model.finance.NetSalary
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
            label = type.name
        )

        is Deduction.PercentageDeduction -> DeductionUi(
            percentage = (percentage * 100).toString(),
            amount = value,
            label = type.name
        )
    }
}

fun MoneyAllocation.toUi(): MoneyAllocationUi {
    return MoneyAllocationUi(
        amount = amount,
        label = type.name
    )
}
