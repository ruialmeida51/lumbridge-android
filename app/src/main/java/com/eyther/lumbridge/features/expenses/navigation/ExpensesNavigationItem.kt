package com.eyther.lumbridge.features.expenses.navigation

import androidx.annotation.StringRes
import com.eyther.lumbridge.R
import com.eyther.lumbridge.ui.navigation.NavigationItem
import kotlinx.serialization.Serializable

@Serializable
sealed class ExpensesNavigationItem(
    override val route: String,
    @StringRes override val label: Int
) : NavigationItem() {

    companion object {
        const val ARG_EXPENSE_ID = "expenseId"
        const val ARG_MONTH = "month"
        const val ARG_YEAR = "year"
    }

    @Serializable
    data object ExpensesOverview : ExpensesNavigationItem(
        route = "expenses_overview",
        label = R.string.expenses_overview
    )

    @Serializable
    data object AddExpense : ExpensesNavigationItem(
        route = "add_expense/{$ARG_MONTH}/{$ARG_YEAR}",
        label = R.string.expenses_overview_add_expense
    )

    @Serializable
    data object EditFinancialProfile : ExpensesNavigationItem(
        route = "edit_financial_profile",
        label = R.string.edit_financial_profile
    )

    @Serializable
    data object EditExpense : ExpensesNavigationItem(
        route = "edit_expense/{$ARG_EXPENSE_ID}",
        label = R.string.expenses_overview_edit_expense
    )

    @Serializable
    data object ExpensesMonthDetail : ExpensesNavigationItem(
        route = "expenses_month_detail/{$ARG_MONTH}/{$ARG_YEAR}",
        label = R.string.expenses_month_detail
    )
}
