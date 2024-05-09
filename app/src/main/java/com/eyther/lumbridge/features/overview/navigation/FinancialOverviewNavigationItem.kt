package com.eyther.lumbridge.features.overview.navigation

import androidx.annotation.StringRes
import com.eyther.lumbridge.R
import com.eyther.lumbridge.ui.navigation.NavigationItem

sealed class FinancialOverviewNavigationItem(
    route: String,
    @StringRes label: Int
) : NavigationItem(route, label) {
    data object FinancialOverview : FinancialOverviewNavigationItem(
        route = "financial_overview",
        label = R.string.financial_overview
    )

    data object EditFinancialProfile : FinancialOverviewNavigationItem(
        route = "edit_financial_profile",
        label = R.string.edit_financial_profile
    )
}
