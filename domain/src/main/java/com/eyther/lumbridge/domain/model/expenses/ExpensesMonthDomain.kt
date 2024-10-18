package com.eyther.lumbridge.domain.model.expenses

import java.time.Month
import java.time.Year

data class ExpensesMonthDomain(
    val id: Long = -1,
    val month: Month,
    val year: Year,
    val day: Int,
    val snapshotMonthlyNetSalary: Float,
    val categoryExpenses: List<ExpensesCategoryDomain>
)
