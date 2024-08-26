package com.eyther.lumbridge.domain.model.expenses

data class ExpensesCategoryDomain(
    val id: Long = -1,
    val parentMonthId: Long = -1,
    val categoryType: ExpensesCategoryTypes,
    val detailedExpenses: List<ExpensesDetailedDomain>
)
