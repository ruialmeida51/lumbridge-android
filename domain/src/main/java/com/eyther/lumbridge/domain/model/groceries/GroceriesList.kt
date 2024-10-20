package com.eyther.lumbridge.domain.model.groceries

data class GroceriesList(
    val id: Long,
    val title: String,
    val entries: List<GroceriesListEntry>
)
