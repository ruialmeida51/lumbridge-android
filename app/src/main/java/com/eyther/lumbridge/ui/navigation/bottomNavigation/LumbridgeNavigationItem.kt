package com.eyther.lumbridge.ui.navigation.bottomNavigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.eyther.lumbridge.R
import com.eyther.lumbridge.ui.navigation.NavigationItem

sealed class LumbridgeNavigationItem(
    route: String,
    @StringRes label: Int,
    @DrawableRes val icon: Int
) : NavigationItem(route, label) {
    companion object {
        fun items() = listOf(Feed, Tools, Expenses, Overview, Profile)
    }

    data object Feed : LumbridgeNavigationItem(
        route = "feed", icon = R.drawable.ic_news, label = R.string.feed
    )

    data object Tools : LumbridgeNavigationItem(
        route = "tools", icon = R.drawable.ic_widgets, label = R.string.tools
    )

    data object Expenses : LumbridgeNavigationItem(
        route = "expenses", icon = R.drawable.ic_payments, label = R.string.expenses
    )

    data object Overview : LumbridgeNavigationItem(
        route = "overview", icon = R.drawable.ic_chart, label = R.string.overview
    )

    data object Profile : LumbridgeNavigationItem(
        route = "profile", icon = R.drawable.ic_person, label = R.string.profile
    )
}
