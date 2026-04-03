package com.eyther.lumbridge.domain.model.expenses

import java.time.Month
import java.time.Year

data class ExpensesMonthDomain(
    val month: Month,
    val year: Year,
    val spent: Float,
    val gained: Float,
    val remainder: Float,
    val snapshotMonthlyNetSalary: Float,
    val snapshotAllocations: List<MonthAllocationDomain>,
    val categoryExpenses: List<ExpensesCategoryDomain>
)
