package com.eyther.lumbridge.features.tools.overview.viewmodel

import androidx.navigation.NavController
import com.eyther.lumbridge.features.tools.overview.model.ToolItem
import com.eyther.lumbridge.features.tools.overview.model.ToolScreenViewState
import kotlinx.coroutines.flow.StateFlow

interface IToolsScreenViewModel {
    val viewState: StateFlow<ToolScreenViewState>

    /**
     * Navigate to the selected tool screen.
     *
     * @param toolItem the selected tool item
     * @param navController the navigation controller
     * @see ToolItem
     */
    fun navigate(toolItem: ToolItem, navController: NavController)
}