package com.eyther.lumbridge.features.tools.shopping.model.details

sealed interface ShoppingListDetailsScreenViewEffect {
    data object NavigateBack : ShoppingListDetailsScreenViewEffect
}
