package com.eyther.lumbridge.features.tools.groceries.viewmodel.details.delegate

import com.eyther.lumbridge.features.tools.groceries.model.details.GroceriesListDetailsScreenInputState
import kotlinx.coroutines.flow.StateFlow

interface IGroceriesListDetailsScreenInputHandler {
    val inputState: StateFlow<GroceriesListDetailsScreenInputState>

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
    fun onKeyboardNext(index: Int)
    fun updateIndex(oldIndex: Int, newIndex: Int)

    /**
     * Helper function to update the input state.
     * @param update the lambda to update the input state.
     * @see GroceriesListDetailsScreenInputState
     */
    fun updateInput(update: (GroceriesListDetailsScreenInputState) -> GroceriesListDetailsScreenInputState)
}