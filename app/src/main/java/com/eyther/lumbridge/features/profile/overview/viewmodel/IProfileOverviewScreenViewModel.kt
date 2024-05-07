package com.eyther.lumbridge.features.profile.overview.viewmodel

import androidx.navigation.NavController
import com.eyther.lumbridge.features.profile.overview.model.ProfileOverviewScreenViewState
import com.eyther.lumbridge.ui.navigation.NavigationItem
import kotlinx.coroutines.flow.StateFlow

interface IProfileOverviewScreenViewModel {
    val viewState: StateFlow<ProfileOverviewScreenViewState>

    /**
     * Navigate to the selected tool screen.
     *
     * @param navController the navigation controller
     * @param navItem the navigation item
     */
    fun navigate(navItem: NavigationItem, navController: NavController)
}
