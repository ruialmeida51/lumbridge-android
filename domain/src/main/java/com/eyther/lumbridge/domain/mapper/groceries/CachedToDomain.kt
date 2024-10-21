package com.eyther.lumbridge.domain.mapper.groceries

import com.eyther.lumbridge.data.model.groceries.local.GroceriesListCached
import com.eyther.lumbridge.data.model.groceries.local.GroceriesListEntryCached
import com.eyther.lumbridge.domain.model.groceries.GroceriesList
import com.eyther.lumbridge.domain.model.groceries.GroceriesListEntry

fun GroceriesListCached.toDomain() = GroceriesList(
    id = id,
    showTickedItems = showTickedItems,
    title = title,
    entries = entries.map { it.toDomain() }
)

fun GroceriesListEntryCached.toDomain() = GroceriesListEntry(
    index = index,
    text = text,
    selected = selected
)

fun List<GroceriesListCached>.toDomain() = map { it.toDomain() }