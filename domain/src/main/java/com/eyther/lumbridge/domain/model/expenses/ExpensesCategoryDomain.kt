package com.eyther.lumbridge.domain.model.expenses

data class ExpensesCategoryDomain(
    val categoryType: ExpensesCategoryTypes,
    val spent: Float,
    val expenses: List<ExpenseDomain>
)
