package com.eyther.lumbridge.features.overview.viewmodel

import androidx.navigation.NavHostController
import com.eyther.lumbridge.features.overview.model.FinancialOverviewScreenViewState
import com.eyther.lumbridge.ui.navigation.NavigationItem
import kotlinx.coroutines.flow.StateFlow

interface FinancialOverviewScreenViewModelInterface {
    val viewState : StateFlow<FinancialOverviewScreenViewState>

    /**
     * Navigates to the edit economic profile screen.
     *
     * @param navController the navigation controller.
     * @param navItem the navigation item.
     */
    fun navigate(navItem: NavigationItem, navController: NavHostController)
}
