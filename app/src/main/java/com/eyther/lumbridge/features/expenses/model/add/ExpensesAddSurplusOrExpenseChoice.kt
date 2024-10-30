package com.eyther.lumbridge.features.expenses.model.add

import androidx.annotation.StringRes
import com.eyther.lumbridge.R

sealed class ExpensesAddSurplusOrExpenseChoice(
    @StringRes val label: Int,
    val ordinal: Int
) {
    companion object {
        fun entries() = listOf(Expense, Surplus)
    }

    data object Expense : ExpensesAddSurplusOrExpenseChoice(
        label = R.string.expense,
        ordinal = 0
    )

    data object Surplus : ExpensesAddSurplusOrExpenseChoice(
        label = R.string.surplus,
        ordinal = 1
    )
}
