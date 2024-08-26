package com.eyther.lumbridge.model.expenses

import androidx.annotation.DrawableRes
import com.eyther.lumbridge.domain.model.expenses.ExpensesCategoryTypes

data class ExpensesCategoryUi(
    val id: Long = 0,
    val parentMonthId: Long = 0,
    val categoryType: ExpensesCategoryTypes,
    @DrawableRes val icon: Int = -1,
    val spent: Float = -1f,
    val expanded: Boolean = false,
    val expensesDetailedUi: List<ExpensesDetailedUi>
)
