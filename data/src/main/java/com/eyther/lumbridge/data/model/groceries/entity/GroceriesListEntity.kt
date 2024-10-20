package com.eyther.lumbridge.data.model.groceries.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

const val GROCERIES_LIST_TABLE_NAME = "groceries_list"

/**
 * Represents a list of groceries items.
 *
 * @property groceriesListId The unique identifier for the groceries list.
 * @property showTickedItems Whether to show items that have been ticked off.
 * @property title The title of the groceries list.
 * @property groceriesListItems The list of groceries items, containing the index, text, and selected
 */
@Entity(
    tableName = GROCERIES_LIST_TABLE_NAME
)
data class GroceriesListEntity(
    @PrimaryKey(autoGenerate = true) val groceriesListId: Long = 0,
    val showTickedItems: Boolean,
    val title: String,
    val groceriesListItems: ArrayList<GroceriesListEntryEntity>
)

