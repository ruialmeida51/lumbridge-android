package com.eyther.lumbridge.model.expenses

import java.time.LocalDate

data class ExpensesDetailedUi(
    val id: Long = -1,
    val expenseName: String,
    val expenseAmount: Float,
    val date: LocalDate
)
