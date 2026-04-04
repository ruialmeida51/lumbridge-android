package com.eyther.lumbridge.features.tools.shopping.viewmodel.details.delegate

import com.eyther.lumbridge.features.tools.shopping.model.details.ShoppingListDetailsScreenInputState
import kotlinx.coroutines.flow.StateFlow

interface IShoppingListDetailsScreenInputHandler {
    val inputState: StateFlow<ShoppingListDetailsScreenInputState>

    /**
     * Methods called on different input changes. Each one updates the input state with
     * the new value and performs any necessary validation.
     */
    fun onShowTickedItemsChanged(showTickedItems: Boolean)
    fun onTitleChanged(name: String?)
    fun onEntryTextUpdated(index: Int, text: String)
    fun onEntrySelected(index: Int, selected: Boolean)
    fun onClearItem(index: Int)
    fun onKeyboardDelete(index: Int): Boolean
    fun onKeyboardNext(cursorPosition: Int?, index: Int)

    /**
     * Helper function to update the input state.
     * @param update the lambda to update the input state.
     * @see ShoppingListDetailsScreenInputState
     */
    fun updateInput(update: (ShoppingListDetailsScreenInputState) -> ShoppingListDetailsScreenInputState)
}
