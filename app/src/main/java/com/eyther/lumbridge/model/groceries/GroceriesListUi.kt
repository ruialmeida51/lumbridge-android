package com.eyther.lumbridge.model.groceries

data class GroceriesListUi(
    val id: Long = -1,
    val showTickedItems: Boolean,
    val title: String,
    val entries: List<GroceriesListEntryUi>
)
