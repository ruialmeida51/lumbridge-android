package com.eyther.lumbridge.model.expenses

import java.time.Month
import java.time.Year

data class ExpensesMonthUi(
    val id: Long = 0,
    val month: Month,
    val year: Year,
    val spent: Float = -1f,
    val remainder: Float = -1f,
    val expanded: Boolean = false,
    val categoryExpenses: List<ExpensesCategoryUi>
)
