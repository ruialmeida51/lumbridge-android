package com.eyther.lumbridge.domain.model.shopping

data class ShoppingList(
    val id: Long,
    val showTickedItems: Boolean,
    val title: String,
    val entries: List<ShoppingListEntry>
)
