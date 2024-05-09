package com.eyther.lumbridge.ui.navigation

import androidx.annotation.StringRes

abstract class NavigationItem(
    val route: String,
    @StringRes val label: Int
)
