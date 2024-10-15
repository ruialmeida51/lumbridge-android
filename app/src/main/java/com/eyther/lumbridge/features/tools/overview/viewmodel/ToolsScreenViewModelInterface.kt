package com.eyther.lumbridge.features.tools.overview.viewmodel

import com.eyther.lumbridge.features.tools.overview.model.ToolItem
import com.eyther.lumbridge.features.tools.overview.model.ToolScreenViewState
import com.eyther.lumbridge.ui.navigation.NavigationItem
import kotlinx.coroutines.flow.StateFlow

interface IToolsScreenViewModel {
    val viewState: StateFlow<ToolScreenViewState>

    /**
     * Returns the route to navigate to based on the selected tool item.
     *
     * @param toolItem the selected tool item
     * @see ToolItem
     */
    fun getRouteToNavigate(toolItem: ToolItem): NavigationItem
}
