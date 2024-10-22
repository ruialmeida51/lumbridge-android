package com.eyther.lumbridge.mapper.groceries

import com.eyther.lumbridge.domain.model.groceries.GroceriesList
import com.eyther.lumbridge.domain.model.groceries.GroceriesListEntry
import com.eyther.lumbridge.model.groceries.GroceriesListEntryUi
import com.eyther.lumbridge.model.groceries.GroceriesListUi

fun GroceriesListUi.toDomain() = GroceriesList(
    id = id,
    showTickedItems = showTickedItems,
    title = title,
    entries = entries.map { it.toDomain() }
)

fun GroceriesListEntryUi.toDomain() = GroceriesListEntry(
    index = index,
    text = text,
    selected = selected
)
