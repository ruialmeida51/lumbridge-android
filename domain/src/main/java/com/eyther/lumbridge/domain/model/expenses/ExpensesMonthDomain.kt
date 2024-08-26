package com.eyther.lumbridge.domain.model.expenses

import java.time.Month
import java.time.Year

data class ExpensesMonthDomain(
    val id: Long = -1,
    val month: Month,
    val year: Year,
    val categoryExpenses: List<ExpensesCategoryDomain>
)
