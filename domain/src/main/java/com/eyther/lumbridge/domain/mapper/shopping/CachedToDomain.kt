package com.eyther.lumbridge.domain.mapper.shopping

import com.eyther.lumbridge.data.model.shopping.local.ShoppingListCached
import com.eyther.lumbridge.data.model.shopping.local.ShoppingListEntryCached
import com.eyther.lumbridge.domain.model.shopping.ShoppingList
import com.eyther.lumbridge.domain.model.shopping.ShoppingListEntry

fun ShoppingListCached.toDomain() = ShoppingList(
    id = id,
    showTickedItems = showTickedItems,
    title = title,
    entries = entries.map { it.toDomain() }
)

fun ShoppingListEntryCached.toDomain() = ShoppingListEntry(
    index = index,
    text = text,
    selected = selected
)

fun List<ShoppingListCached>.toDomain() = map { it.toDomain() }
