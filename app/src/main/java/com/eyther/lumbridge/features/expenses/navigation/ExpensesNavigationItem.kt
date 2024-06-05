package com.eyther.lumbridge.features.expenses.navigation

import androidx.annotation.StringRes
import com.eyther.lumbridge.R
import com.eyther.lumbridge.ui.navigation.NavigationItem

sealed class ExpensesNavigationItem(
    route: String,
    @StringRes label: Int
) : NavigationItem(route, label) {

    data object ExpensesOverview : ExpensesNavigationItem(
        route = "expenses_overview",
        label = R.string.expenses_overview
    )
}
