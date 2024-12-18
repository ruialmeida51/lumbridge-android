package com.eyther.lumbridge.features.tools.shopping.viewmodel.details

import com.eyther.lumbridge.features.tools.shopping.model.details.ShoppingListDetailsScreenViewEffect
import com.eyther.lumbridge.features.tools.shopping.model.details.ShoppingListDetailsScreenViewState
import com.eyther.lumbridge.features.tools.shopping.viewmodel.details.delegate.IShoppingListDetailsScreenInputHandler
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface IShoppingListDetailsScreenViewModel : IShoppingListDetailsScreenInputHandler {
    val viewState: StateFlow<ShoppingListDetailsScreenViewState>
    val viewEffects: SharedFlow<ShoppingListDetailsScreenViewEffect>

    /**
     * Caches a default title for the shopping list.
     * @param defaultTitle the default title to use if the user didn't provide one
     */
    fun setDefaultTitle(defaultTitle: String)

    /**
     * Saves the shopping list to the database. This will be called on onPause and on onResume. If the user leaves the screen,
     * we save the list.
     *
     * The idea here is to check if there is any content to save and only then insert it into the database. The user
     * might've just opened the screen and closed it without adding anything.
     *
     * @param finish if true, the user is leaving the screen and we emit a finish effect
     */
    fun saveShoppingList(finish: Boolean = true)

    /**
     * Attempts to delete the shopping list. It uses the shopping list ID stored
     * in the view model to delete the list.
     */
    fun onDeleteShoppingList()
}
