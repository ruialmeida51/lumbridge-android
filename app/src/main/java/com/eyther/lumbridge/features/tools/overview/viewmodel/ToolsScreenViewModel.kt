package com.eyther.lumbridge.features.tools.overview.viewmodel

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.eyther.lumbridge.features.tools.overview.model.ToolItem
import com.eyther.lumbridge.features.tools.overview.model.ToolItem.Mortgage
import com.eyther.lumbridge.features.tools.overview.model.ToolScreenViewState
import com.eyther.lumbridge.features.tools.overview.navigation.ToolsNavigationItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class ToolsScreenViewModel @Inject constructor() : ViewModel(), IToolsScreenViewModel {

    override val viewState = MutableStateFlow<ToolScreenViewState>(getInitialState())

    private fun getInitialState() =
        ToolScreenViewState.Content(ToolItem.getItems())

    override fun navigate(toolItem: ToolItem, navController: NavController) {
        navController.navigate(
            when (toolItem) {
                ToolItem.Personal.NetSalaryCalculator -> ToolsNavigationItem.NetSalary.Input.route
                ToolItem.Personal.CurrencyConverter -> ToolsNavigationItem.CurrencyConverter.route
                ToolItem.Personal.SavingsCalculator -> ToolsNavigationItem.Savings.route
                Mortgage.MortgageCalculator -> ToolsNavigationItem.Mortgage.route
            }
        )
    }
}
