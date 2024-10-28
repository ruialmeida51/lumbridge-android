package com.eyther.lumbridge.model.expenses

import java.time.LocalDate

data class ExpenseUi(
    val id: Long = -1,
    val categoryType: ExpensesCategoryTypesUi,
    val expenseName: String,
    val expenseAmount: Float,
    val date: LocalDate
)
