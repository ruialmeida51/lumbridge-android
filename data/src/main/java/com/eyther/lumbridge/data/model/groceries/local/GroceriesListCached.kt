package com.eyther.lumbridge.data.model.groceries.local

data class GroceriesListCached(
    val id: Long = -1,
    val showTickedItems: Boolean,
    val title: String,
    val entries: List<GroceriesListEntryCached>
)
