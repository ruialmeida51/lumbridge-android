package com.eyther.lumbridge.features.overview.navigation

import com.eyther.lumbridge.ui.navigation.NavigationItem

sealed class FinancialOverviewNavigationItem(route: String, label: String) : NavigationItem(route, label) {
    data object FinancialOverview : FinancialOverviewNavigationItem(
        route = "financial_overview",
        label = "Financial Overview"
    )

    data object EditFinancialProfile : FinancialOverviewNavigationItem(
        route = "edit_financial_profile",
        label = "Edit Financial Profile"
    )
}
