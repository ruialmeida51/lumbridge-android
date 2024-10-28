package com.eyther.lumbridge.domain.model.expenses

import java.time.LocalDate

data class ExpenseDomain(
    val id: Long,
    val categoryType: ExpensesCategoryTypes,
    val expenseName: String,
    val expenseAmount: Float,
    val date: LocalDate
)