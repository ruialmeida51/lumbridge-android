package com.eyther.lumbridge.features.tools.shopping.viewmodel.list

import com.eyther.lumbridge.features.tools.shopping.model.list.ShoppingListScreenViewState
import com.eyther.lumbridge.model.shopping.ShoppingListUi
import kotlinx.coroutines.flow.StateFlow

interface IShoppingListScreenViewModel {
    val viewState: StateFlow<ShoppingListScreenViewState>

    /**
     * Deletes a shopping list from the database.
     *
     * @param shoppingList The shopping list to delete.
     */
    fun onDeleteShoppingList(shoppingList: ShoppingListUi)
}
