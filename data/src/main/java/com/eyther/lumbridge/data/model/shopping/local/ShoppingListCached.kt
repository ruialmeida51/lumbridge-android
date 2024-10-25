package com.eyther.lumbridge.data.model.shopping.local

data class ShoppingListCached(
    val id: Long = -1,
    val showTickedItems: Boolean,
    val title: String,
    val entries: List<ShoppingListEntryCached>
)
