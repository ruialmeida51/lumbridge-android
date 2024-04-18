package com.eyther.lumbridge.features.tools.overview.viewmodel

import com.eyther.lumbridge.features.tools.overview.model.ToolScreenViewState
import kotlinx.coroutines.flow.StateFlow

interface ToolsScreenViewModelInterface {
    val viewState: StateFlow<ToolScreenViewState>
}