package com.eyther.lumbridge.features.tools.groceries.model.list

import com.eyther.lumbridge.model.groceries.GroceriesListUi

sealed interface GroceriesListScreenViewState {
    data object Loading: GroceriesListScreenViewState
    data object Empty: GroceriesListScreenViewState

    data class Content(
        val groceriesList: List<GroceriesListUi>
    ): GroceriesListScreenViewState
}
