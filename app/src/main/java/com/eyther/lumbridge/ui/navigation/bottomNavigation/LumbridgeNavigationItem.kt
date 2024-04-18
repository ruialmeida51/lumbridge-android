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
        fun items() = listOf(Feed, Guides, Play, Tools, Profile)
    }

    data object Feed : LumbridgeNavigationItem(
        route = "feed", icon = R.drawable.ic_coffee_chat, label = "Feed"
    )

    data object Guides : LumbridgeNavigationItem(
        route = "guides", icon = R.drawable.ic_parchment, label = "Guides"
    )

    data object Play : LumbridgeNavigationItem(
        route = "play", icon = R.drawable.ic_adventure, label = "Play"
    )

    data object Tools : LumbridgeNavigationItem(
        route = "tools", icon = R.drawable.ic_bar_chart, label = "Tools"
    )

    data object Profile : LumbridgeNavigationItem(
        route = "profile", icon = R.drawable.ic_king, label = "Profile"
    )
}
