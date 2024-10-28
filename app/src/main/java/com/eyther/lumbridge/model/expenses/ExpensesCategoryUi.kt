package com.eyther.lumbridge.model.expenses

data class ExpensesCategoryUi(
    val categoryType: ExpensesCategoryTypesUi,
    val spent: Float = -1f,
    val expanded: Boolean = false,
    val expensesDetailedUi: List<ExpensesDetailedUi>
)
