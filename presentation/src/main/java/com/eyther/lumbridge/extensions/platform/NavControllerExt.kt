package com.eyther.lumbridge.extensions.platform

import androidx.navigation.NavController
import com.eyther.lumbridge.ui.navigation.NavigationItem

fun NavController.navigateTo(navigationItem: NavigationItem) {
    navigate(navigationItem.route)
}

fun NavController.navigateToWithArgs(navigationItem: NavigationItem, vararg args: Any) {
    navigate(navigationItem.buildRouteWithArgs(*args))
}
