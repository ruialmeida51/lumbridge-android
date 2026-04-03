package com.eyther.lumbridge.features.tools.overview.model

sealed interface ToolScreenViewState {
    data class Content(
        val options: Map<Int, List<ToolItem>>
    ) : ToolScreenViewState
}
