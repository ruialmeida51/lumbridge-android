package com.eyther.lumbridge.ui.navigation.bottomNavigation

import androidx.annotation.DrawableRes
import com.eyther.lumbridge.R
import com.eyther.lumbridge.ui.navigation.NavigationItem

sealed class LumbridgeNavigationItem(
    route: String,
    label: String,
    @DrawableRes val icon: Int
) : NavigationItem(route, label) {
    companion object {
        fun items() = listOf(Feed, Tools, Overview, Profile)
    }

    data object Feed : LumbridgeNavigationItem(
        route = "feed", icon = R.drawable.ic_news, label = "Feed"
    )

    data object Tools : LumbridgeNavigationItem(
        route = "tools", icon = R.drawable.ic_calculator, label = "Tools"
    )

    data object Overview : LumbridgeNavigationItem(
        route = "overview", icon = R.drawable.ic_chart, label = "Overview"
    )

    data object Profile : LumbridgeNavigationItem(
        route = "profile", icon = R.drawable.ic_person, label = "Profile"
    )
}
