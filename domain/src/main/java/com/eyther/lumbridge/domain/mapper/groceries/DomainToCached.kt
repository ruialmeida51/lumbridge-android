package com.eyther.lumbridge.domain.mapper.groceries

import com.eyther.lumbridge.data.model.groceries.local.GroceriesListCached
import com.eyther.lumbridge.data.model.groceries.local.GroceriesListEntryCached
import com.eyther.lumbridge.domain.model.groceries.GroceriesList
import com.eyther.lumbridge.domain.model.groceries.GroceriesListEntry

fun GroceriesList.toCached() = GroceriesListCached(
    id = id,
    title = title,
    entries = entries.map { it.toCached() }
)

fun GroceriesListEntry.toCached() = GroceriesListEntryCached(
    index = index,
    text = text,
    selected = selected
)

