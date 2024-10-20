package com.eyther.lumbridge.data.model.groceries.local

data class GroceriesListCached(
    val id: Long = -1,
    val title: String,
    val entries: List<GroceriesListEntryCached>
)
