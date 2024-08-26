package com.eyther.lumbridge.data.model.expenses.local

data class ExpensesMonthCached(
    val id: Long = -1,
    val month: Int,
    val year: Int,
    val categories: List<ExpensesCategoryCached>
)
