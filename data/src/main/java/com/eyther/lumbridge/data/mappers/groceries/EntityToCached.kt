package com.eyther.lumbridge.data.mappers.groceries

import com.eyther.lumbridge.data.model.groceries.entity.GroceriesListEntity
import com.eyther.lumbridge.data.model.groceries.entity.GroceriesListEntryEntity
import com.eyther.lumbridge.data.model.groceries.local.GroceriesListCached
import com.eyther.lumbridge.data.model.groceries.local.GroceriesListEntryCached

fun GroceriesListEntity.toCached() = GroceriesListCached(
    id = groceriesListId,
    title = title,
    entries = groceriesListItems.map { it.toCached() }
)

fun GroceriesListEntryEntity.toCached() = GroceriesListEntryCached(
    index = index,
    text = text,
    selected = selected
)
