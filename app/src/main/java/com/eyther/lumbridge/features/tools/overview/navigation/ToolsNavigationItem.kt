package com.eyther.lumbridge.features.tools.overview.navigation

import androidx.annotation.StringRes
import com.eyther.lumbridge.R
import com.eyther.lumbridge.ui.navigation.NavigationItem

sealed class ToolsNavigationItem(route: String, @StringRes label: Int) : NavigationItem(route, label) {
    data object Overview : ToolsNavigationItem(
        route = "tools_overview",
        label = R.string.tools
    )

    data object NetSalary : ToolsNavigationItem(
        route = "net_salary",
        label = R.string.tools_net_salary_calculator
    )

    data object CostToCompany : ToolsNavigationItem(
        route = "cost_to_company",
        label = R.string.tools_ctc_calculator_short
    )

    data object Savings : ToolsNavigationItem(
        route = "savings",
        label = R.string.tools_savings_calculator
    )

    data object Mortgage : ToolsNavigationItem(
        route = "mortgage",
        label = R.string.tools_mortgage_calculator
    )

    data object CurrencyConverter : ToolsNavigationItem(
        route = "currency_converter",
        label = R.string.tools_currency_converter
    )
}
