package com.eyther.lumbridge.features.tools.overview.navigation

import com.eyther.lumbridge.ui.navigation.NavigationItem

sealed class ToolsNavigationItem(route: String, label: String) : NavigationItem(route, label) {
    data object Overview : ToolsNavigationItem(
        route = "tools_overview",
        label = "Tools"
    )

    data object NetSalary : ToolsNavigationItem(
        route = "net_salary",
        label = "Net Salary Calculator"
    )

    data object CostToCompany : ToolsNavigationItem(
        route = "cost_to_company",
        label = "CTC Calculator"
    )

    data object Savings : ToolsNavigationItem(
        route = "savings",
        label = "Savings Calculator"
    )

    data object Mortgage : ToolsNavigationItem(
        route = "mortgage",
        label = "Mortgage Calculator"
    )
}
