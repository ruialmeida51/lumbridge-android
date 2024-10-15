package com.eyther.lumbridge.features.overview.viewmodel

import com.eyther.lumbridge.features.overview.model.FinancialOverviewScreenViewState
import com.eyther.lumbridge.features.overview.model.FinancialOverviewTabItem
import kotlinx.coroutines.flow.StateFlow

interface IFinancialOverviewScreenViewModel{
    val viewState : StateFlow<FinancialOverviewScreenViewState>

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
