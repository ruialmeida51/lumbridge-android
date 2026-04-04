package com.eyther.lumbridge.model.expenses

import com.eyther.lumbridge.model.finance.MoneyAllocationTypeUi

data class ExpensesMonthAllocationUi(
    val type: MoneyAllocationTypeUi,
    val spent: Float,
    val gained: Float
) {
    val allocated: Float
        get() = type.allocated + gained

    val labelRes: Int
        get() = type.labelRes

    val iconRes: Int
        get() = type.iconRes

    val percentage: Float
        get() = spent / allocated
}
