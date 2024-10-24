package com.eyther.lumbridge.features.tools.shopping.viewmodel.details.delegate

import com.eyther.lumbridge.features.tools.shopping.model.details.ShoppingListDetailsScreenInputState
import com.eyther.lumbridge.features.tools.shopping.model.details.StableShoppingListItem
import com.eyther.lumbridge.ui.common.composables.model.input.CheckboxInputState
import com.eyther.lumbridge.ui.common.composables.model.input.TextInputState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import okhttp3.internal.toImmutableList
import java.util.UUID
import javax.inject.Inject

class ShoppingListDetailsScreenInputHandler @Inject constructor() : IShoppingListDetailsScreenInputHandler {
    override val inputState: MutableStateFlow<ShoppingListDetailsScreenInputState> =
        MutableStateFlow(ShoppingListDetailsScreenInputState())

    override fun onShowTickedItemsChanged(showTickedItems: Boolean) {
        updateInput { state ->
            state.copy(
                showTickedItems = showTickedItems
            )
        }
    }

    override fun onTitleChanged(name: String?) {
        updateInput { state ->
            state.copy(
                title = state.title.copy(
                    text = name
                )
            )
        }
    }

    override fun onEntrySelected(index: Int, selected: Boolean) {
        updateInput { state ->
            val itemsCheckboxState = state.items.toMutableList()
            val item = itemsCheckboxState.getOrNull(index) ?: return@updateInput state

            itemsCheckboxState[index] = item.copy(
                checkboxState = item.checkboxState.copy(
                    checked = selected
                )
            )

            state.copy(items = itemsCheckboxState.toImmutableList())
        }
    }

    override fun onEntryTextUpdated(index: Int, text: String) {
        updateInput { state ->
            val itemsTextInputState = state.items.toMutableList()
            val item = itemsTextInputState.getOrNull(index) ?: return@updateInput state

            itemsTextInputState[index] = item.copy(
                textInputState = item.textInputState.copy(
                    text = text
                )
            )

            state.copy(items = itemsTextInputState)
        }
    }

    override fun onClearItem(index: Int) {
        updateInput { state ->
            val items = state.items
                .toMutableList()
                .apply { removeAt(index) }
                .toImmutableList()

            state.copy(
                items = items
            )
        }
    }

    override fun onKeyboardDelete(index: Int): Boolean {
        updateInput { state ->
            val item = state.items.getOrNull(index) ?: return@updateInput state

            if (item.textInputState.text.isNullOrEmpty()) {
                val newList = state.items
                    .toMutableList()
                    .apply { removeAt(index) }
                    .toImmutableList()

                state.copy(items = newList)
            } else {
                state
            }
        }

        return false // Return true to indicate that the event was handled.
    }

    /**
     * Adds a new blank entry at index + 1 to the shopping list to be filled by the user.
     * The subsequent entries are shifted one index up, if they exist.
     */
    override fun onKeyboardNext(index: Int) {
        updateInput { state ->
            val items = inputState.value.items.toMutableList()

            items.add(
                index + 1, StableShoppingListItem(
                    id = UUID.randomUUID(),
                    checkboxState = CheckboxInputState(),
                    textInputState = TextInputState()
                )
            )

            state.copy(items = items.toImmutableList())
        }
    }

    override fun updateInput(update: (ShoppingListDetailsScreenInputState) -> ShoppingListDetailsScreenInputState) {
        inputState.update { currentState ->
            update(currentState)
        }
    }
}
