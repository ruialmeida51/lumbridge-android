package com.eyther.lumbridge.data.mappers.groceries

import com.eyther.lumbridge.data.model.groceries.entity.GroceriesListEntity
import com.eyther.lumbridge.data.model.groceries.entity.GroceriesListEntryEntity
import com.eyther.lumbridge.data.model.groceries.local.GroceriesListCached
import com.eyther.lumbridge.data.model.groceries.local.GroceriesListEntryCached
import java.util.ArrayList

fun GroceriesListCached.toEntity() = GroceriesListEntity(
    title = title,
    showTickedItems = showTickedItems,
    groceriesListItems = ArrayList(entries.map { it.toEntity() })
)

fun GroceriesListEntryCached.toEntity() = GroceriesListEntryEntity(
    index = index,
    text = text,
    selected = selected
)
