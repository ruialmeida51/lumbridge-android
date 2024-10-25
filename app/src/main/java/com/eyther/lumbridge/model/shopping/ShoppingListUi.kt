package com.eyther.lumbridge.model.shopping

data class ShoppingListUi(
    val id: Long = -1,
    val showTickedItems: Boolean,
    val title: String,
    val entries: List<ShoppingListEntryUi>
)
