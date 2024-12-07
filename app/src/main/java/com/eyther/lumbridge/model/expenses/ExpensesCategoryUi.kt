package com.eyther.lumbridge.model.expenses

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Immutable
data class ExpensesCategoryUi(
    val categoryType: ExpensesCategoryTypesUi,
    val spent: Float = -1f,
    val expanded: Boolean = false,
    val expensesDetailedUi: List<ExpensesDetailedUi>
)
