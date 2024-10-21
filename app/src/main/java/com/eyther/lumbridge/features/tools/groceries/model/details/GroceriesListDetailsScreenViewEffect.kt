package com.eyther.lumbridge.features.tools.groceries.model.details

sealed interface GroceriesListDetailsScreenViewEffect {
    data object NavigateBack : GroceriesListDetailsScreenViewEffect
}
