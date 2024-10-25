package com.eyther.lumbridge.features.tools.shopping.model.details

import androidx.compose.runtime.Stable
import com.eyther.lumbridge.ui.common.composables.model.input.CheckboxInputState
import com.eyther.lumbridge.ui.common.composables.model.input.TextInputState
import java.util.UUID

/**
 * Represents the input state for the Shopping List Details screen.
 *
 * @property showTickedItems Whether to show items that have been ticked off.
 * @property title The title of the list.
 * @property items The list of items. Also contains a unique identifier for each item, auto generated in the view model.
 */
data class ShoppingListDetailsScreenInputState(
    val showTickedItems: Boolean = true,
    val title: TextInputState = TextInputState(),
    val items: List<StableShoppingListItem> = emptyList()
)

/**
 * Represents a single item in the Shopping List Details screen. We need this data class
 * because we need to store a unique identifier for each item due to how Lazy lists work.
 * They use the index as a key, and if we provide text or the checkbox state as the key
 * they change during input, causing the list to re-render. Therefore, we need a stable
 * identifier for each item.
 *
 * @property id Unique identifier for the item
 * @property checkboxState The checkbox state for the item
 * @property textInputState The text input state for the item
 */
@Stable
data class StableShoppingListItem(
    val id: UUID, // Unique identifier for the item
    val checkboxState: CheckboxInputState,
    val textInputState: TextInputState
)
