package com.eyther.lumbridge.data.model.expenses.local

data class ExpensesDetailedCached(
    val id: Long = -1,
    val parentCategoryId: Long = -1,
    val expenseName: String,
    val expenseAmount: Float
)
