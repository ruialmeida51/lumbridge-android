package com.eyther.lumbridge.mapper.finance

import com.eyther.lumbridge.domain.model.finance.Deduction
import com.eyther.lumbridge.domain.model.finance.NetSalary
import com.eyther.lumbridge.model.finance.DeductionUi
import com.eyther.lumbridge.model.finance.NetSalaryUi

fun NetSalary.toUi(): NetSalaryUi {
    return NetSalaryUi(
        salary = salary,
        foodCard = foodCard,
        deductions = deductions.map { it.toUi() }
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
