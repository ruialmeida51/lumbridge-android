package com.eyther.lumbridge.features.tools.shopping.model.list

import com.eyther.lumbridge.domain.model.shopping.ShoppingListUi

sealed interface ShoppingListScreenViewState {
    data object Loading: ShoppingListScreenViewState
    data object Empty: ShoppingListScreenViewState

    data class Content(
        val shoppingLists: List<ShoppingListUi>
    ): ShoppingListScreenViewState
}
