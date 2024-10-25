package com.eyther.lumbridge.features.tools.shopping.model.details

sealed interface ShoppingListDetailsScreenViewState {
    data object Loading: ShoppingListDetailsScreenViewState

    data class Content(
        val inputState: ShoppingListDetailsScreenInputState = ShoppingListDetailsScreenInputState()
    ): ShoppingListDetailsScreenViewState
}
