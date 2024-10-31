package com.eyther.lumbridge.features.tools.overview.viewmodel

import androidx.lifecycle.ViewModel
import com.eyther.lumbridge.features.tools.overview.model.ToolItem
import com.eyther.lumbridge.features.tools.overview.model.ToolScreenViewState
import com.eyther.lumbridge.features.tools.navigation.ToolsNavigationItem
import com.eyther.lumbridge.ui.navigation.NavigationItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class ToolsScreenViewModel @Inject constructor() : ViewModel(), IToolsScreenViewModel {

    override val viewState = MutableStateFlow<ToolScreenViewState>(getInitialState())

    private fun getInitialState() =
        ToolScreenViewState.Content(ToolItem.getItems())

    override fun getRouteToNavigate(toolItem: ToolItem): NavigationItem {
        return when (toolItem) {
            ToolItem.Personal.NetSalaryCalculator -> ToolsNavigationItem.NetSalary.Input
            ToolItem.Personal.CurrencyConverter -> ToolsNavigationItem.CurrencyConverter
            ToolItem.Lifestyle.ShoppingList -> ToolsNavigationItem.Shopping.ShoppingList
            ToolItem.Lifestyle.Notes -> ToolsNavigationItem.Notes.NotesList
            ToolItem.Lifestyle.RecurringPayments -> ToolsNavigationItem.RecurringPayments.Overview
        }
    }
}
