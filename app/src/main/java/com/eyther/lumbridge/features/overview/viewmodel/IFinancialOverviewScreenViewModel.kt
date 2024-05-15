package com.eyther.lumbridge.features.overview.viewmodel

import androidx.navigation.NavHostController
import com.eyther.lumbridge.features.overview.model.FinancialOverviewScreenViewState
import com.eyther.lumbridge.features.overview.model.FinancialOverviewTabItem
import com.eyther.lumbridge.ui.navigation.NavigationItem
import kotlinx.coroutines.flow.StateFlow

interface IFinancialOverviewScreenViewModel{
    val viewState : StateFlow<FinancialOverviewScreenViewState>

    /**
     * Navigates to the edit economic profile screen.
     *
     * @param navController the navigation controller.
     * @param navItem the navigation item.
     */
    fun navigate(navItem: NavigationItem, navController: NavHostController)

    /**
     * Navigates to the financial overview tab.
     * @param tabItem the tab item.
     */
    fun onTabSelected(tabItem: FinancialOverviewTabItem)

    /**
     * Flags a month as paid.
     */
    fun onPayment()
}
