package com.eyther.lumbridge.data.model.expenses.local

data class ExpensesMonthCached(
    val id: Long = -1,
    val month: Int,
    val year: Int,
    val day: Int,
    val snapshotMonthlyNetSalary: Float,
    val categories: List<ExpensesCategoryCached>
)
