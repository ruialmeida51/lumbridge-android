package com.eyther.lumbridge.ui.navigation.bottomNavigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.eyther.lumbridge.R
import com.eyther.lumbridge.ui.navigation.NavigationItem
import kotlinx.serialization.Serializable

@Serializable
sealed class LumbridgeNavigationItem(
    override val route: String,
    @StringRes override val label: Int,
    @DrawableRes val icon: Int
) : NavigationItem() {
    companion object {
        fun items() = listOf(Feed, Tools, Expenses, Overview, Profile)
    }

    @Serializable
    data object Feed : LumbridgeNavigationItem(
        route = "feed", icon = R.drawable.ic_news, label = R.string.feed
    )

    @Serializable
    data object Tools : LumbridgeNavigationItem(
        route = "tools", icon = R.drawable.ic_widgets, label = R.string.tools
    )

    @Serializable
    data object Expenses : LumbridgeNavigationItem(
        route = "expenses", icon = R.drawable.ic_payments, label = R.string.expenses
    )

    @Serializable
    data object Overview : LumbridgeNavigationItem(
        route = "overview", icon = R.drawable.ic_monitoring, label = R.string.overview
    )

    @Serializable
    data object Profile : LumbridgeNavigationItem(
        route = "profile", icon = R.drawable.ic_person, label = R.string.profile
    )
}
