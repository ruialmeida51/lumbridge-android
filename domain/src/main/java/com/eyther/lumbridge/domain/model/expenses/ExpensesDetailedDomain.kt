package com.eyther.lumbridge.domain.model.expenses

data class ExpensesDetailedDomain(
    val id: Long = -1,
    val parentCategoryId: Long = -1,
    val expenseName: String,
    val expenseAmount: Float
)
