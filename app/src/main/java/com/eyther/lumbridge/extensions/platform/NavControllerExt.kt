package com.eyther.lumbridge.extensions.platform

import androidx.navigation.NavController
import com.eyther.lumbridge.ui.navigation.NavigationItem

fun NavController.navigate(navigationItem: NavigationItem) {
    navigate(navigationItem.route)
}
