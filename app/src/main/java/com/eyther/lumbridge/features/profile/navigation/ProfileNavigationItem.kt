package com.eyther.lumbridge.features.profile.navigation

import com.eyther.lumbridge.ui.navigation.NavigationItem

sealed class ProfileNavigationItem(route: String, label: String) : NavigationItem(route, label) {
    data object ProfileOverview : ProfileNavigationItem(
        route = "profile_overview",
        label = "Profile"
    )

    data object EditProfile : ProfileNavigationItem(
        route = "edit_profile",
        label = "Edit Profile"
    )

    data object Settings : ProfileNavigationItem(
        route = "settings",
        label = "Settings"
    )

    data object EditFinancialProfile : ProfileNavigationItem(
        route = "edit_financial_profile",
        label = "Edit Financial Profile"
    )
}
