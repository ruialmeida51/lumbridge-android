package com.eyther.lumbridge.features.tools.groceries.model.list

import com.eyther.lumbridge.model.groceries.GroceriesListUi

sealed interface GroceriesListsScreenViewState {
    data object Loading: GroceriesListsScreenViewState
    data object Empty: GroceriesListsScreenViewState

    data class Content(
        val groceriesList: List<GroceriesListUi>
    ): GroceriesListsScreenViewState
}