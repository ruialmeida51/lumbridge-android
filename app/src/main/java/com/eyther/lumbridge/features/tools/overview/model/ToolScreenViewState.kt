package com.eyther.lumbridge.features.tools.overview.model

sealed interface ToolScreenViewState {
    data class Content(
        val options: List<ToolItem>
    ) : ToolScreenViewState
}
