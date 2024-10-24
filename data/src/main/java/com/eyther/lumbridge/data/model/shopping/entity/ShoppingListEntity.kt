package com.eyther.lumbridge.data.model.shopping.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

const val SHOPPING_LIST_TABLE_NAME = "shopping_list"

/**
 * Represents a list of shopping items.
 *
 * @property shoppingListId The unique identifier for the shopping list.
 * @property showTickedItems Whether to show items that have been ticked off.
 * @property title The title of the shopping list.
 * @property shoppingListItems The list of items, containing the index, text, and selected
 */
@Entity(
    tableName = SHOPPING_LIST_TABLE_NAME
)
data class ShoppingListEntity(
    @PrimaryKey(autoGenerate = true) val shoppingListId: Long = 0,
    val showTickedItems: Boolean,
    val title: String,
    val shoppingListItems: ArrayList<ShoppingListEntryEntity>
)
