package com.eyther.lumbridge.features.expenses.navigation

import androidx.annotation.StringRes
import com.eyther.lumbridge.R
import com.eyther.lumbridge.ui.navigation.NavigationItem

sealed class ExpensesNavigationItem(
    route: String,
    @StringRes label: Int
) : NavigationItem(route, label) {

    companion object {
        const val ARG_EXPENSE_ID = "expenseId"
    }

    data object ExpensesOverview : ExpensesNavigationItem(
        route = "expenses_overview",
        label = R.string.expenses_overview
    )

    data object AddExpense : ExpensesNavigationItem(
        route = "add_expense",
        label = R.string.expenses_overview_add_expense
    )

    data object EditFinancialProfile : ExpensesNavigationItem(
        route = "edit_financial_profile",
        label = R.string.edit_financial_profile
    )

    data object EditExpense : ExpensesNavigationItem(
        route = "edit_expense/{$ARG_EXPENSE_ID}",
        label = R.string.expenses_overview_edit_expense
    )
}
