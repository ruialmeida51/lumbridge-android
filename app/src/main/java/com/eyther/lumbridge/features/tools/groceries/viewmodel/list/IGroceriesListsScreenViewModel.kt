package com.eyther.lumbridge.features.tools.groceries.viewmodel.list

import com.eyther.lumbridge.features.tools.groceries.model.list.GroceriesListsScreenViewState
import com.eyther.lumbridge.model.groceries.GroceriesListUi
import kotlinx.coroutines.flow.StateFlow

interface IGroceriesListScreenViewModel {
    val viewState: StateFlow<GroceriesListsScreenViewState>

    /**
     * Deletes a grocery list from the database.
     *
     * @param groceryItem The grocery list to delete.
     */
    fun onDeleteGroceryItem(groceryItem: GroceriesListUi)
}
