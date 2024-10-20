package com.eyther.lumbridge.model.groceries

data class GroceriesListUi(
    val id: Long = -1,
    val title: String,
    val entries: List<GroceriesListEntryUi>
)
