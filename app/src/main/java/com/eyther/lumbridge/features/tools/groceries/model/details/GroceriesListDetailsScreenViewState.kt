package com.eyther.lumbridge.features.tools.groceries.model.details

sealed interface GroceriesListDetailsScreenViewState {
    data object Loading: GroceriesListDetailsScreenViewState

    data class Content(
        val inputState: GroceriesListDetailsScreenInputState = GroceriesListDetailsScreenInputState()
    ): GroceriesListDetailsScreenViewState
}
