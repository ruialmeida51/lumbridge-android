package com.eyther.lumbridge.model.expenses

import com.eyther.lumbridge.model.finance.MoneyAllocationTypeUi
import java.time.LocalDate

data class ExpenseUi(
    val id: Long = -1,
    val categoryType: ExpensesCategoryTypesUi,
    val allocationTypeUi: MoneyAllocationTypeUi,
    val expenseName: String,
    val expenseAmount: Float,
    val date: LocalDate
)
