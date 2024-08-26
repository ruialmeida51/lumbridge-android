package com.eyther.lumbridge.data.model.expenses.local

data class ExpensesCategoryCached(
    val id: Long = -1,
    val parentMonthId: Long = -1,
    val categoryTypeOrdinal: Int,
    val details: List<ExpensesDetailedCached>
)
