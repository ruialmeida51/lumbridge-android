package com.eyther.lumbridge.features.profile.navigation

import androidx.annotation.StringRes
import com.eyther.lumbridge.R
import com.eyther.lumbridge.ui.navigation.NavigationItem

sealed class ProfileNavigationItem(
    route: String,
    @StringRes label: Int
) : NavigationItem(route, label) {
    data object ProfileOverview : ProfileNavigationItem(
        route = "profile_overview",
        label = R.string.profile
    )

    data object EditProfile : ProfileNavigationItem(
        route = "edit_profile",
        label = R.string.edit_profile
    )

    data object Settings : ProfileNavigationItem(
        route = "settings",
        label = R.string.settings
    )

    data object EditFinancialProfile : ProfileNavigationItem(
        route = "edit_financial_profile",
        label = R.string.edit_financial_profile
    )

    data object EditMortgageProfile : ProfileNavigationItem(
        route = "edit_mortgage_profile",
        label = R.string.edit_mortgage_profile
    )
}
