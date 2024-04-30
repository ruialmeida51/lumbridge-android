package com.eyther.lumbridge.features.tools.overview.viewmodel

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.eyther.lumbridge.features.tools.overview.model.ToolItem
import com.eyther.lumbridge.features.tools.overview.model.ToolItem.*
import com.eyther.lumbridge.features.tools.overview.model.ToolScreenViewState
import com.eyther.lumbridge.features.tools.overview.navigation.ToolsNavigationItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class ToolsScreenViewModel @Inject constructor() : ViewModel(), ToolsScreenViewModelInterface {

    override val viewState = MutableStateFlow<ToolScreenViewState>(getInitialState())

    private fun getInitialState() = ToolScreenViewState.Content(ToolItem.entries)

    fun onItemClick(toolItem: ToolItem, navController: NavController) {
        navController.navigate(
            when (toolItem) {
                NetSalary -> ToolsNavigationItem.NetSalary.route
                CTC -> ToolsNavigationItem.CostToCompany.route
                Savings -> ToolsNavigationItem.Savings.route
                Mortgage -> ToolsNavigationItem.Mortgage.route
            }
        )
    }
}
