package com.eyther.lumbridge.model.expenses

data class ExpensesDetailedUi(
    val id: Long = 0,
    val parentCategoryId: Long = 0,
    val expenseName: String,
    val expenseAmount: Float
)
