package com.eyther.lumbridge.features.tools.viewmodel

import kotlinx.coroutines.flow.StateFlow

interface ToolsScreenViewModelInterface {
	sealed interface ToolScreenViewState {
		data object Initial : ToolScreenViewState
		data class Content(val options: List<String>) : ToolScreenViewState
	}
	
	val viewState: StateFlow<ToolScreenViewState>
}