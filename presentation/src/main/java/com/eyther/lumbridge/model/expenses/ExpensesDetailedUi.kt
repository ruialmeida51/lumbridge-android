package com.eyther.lumbridge.model.expenses

import com.eyther.lumbridge.model.finance.MoneyAllocationTypeUi
import java.time.LocalDate

data class ExpensesDetailedUi(
    val id: Long = -1,
    val expenseName: String,
    val expenseAmount: Float,
    val date: LocalDate,
    val allocationTypeUi: MoneyAllocationTypeUi
)
