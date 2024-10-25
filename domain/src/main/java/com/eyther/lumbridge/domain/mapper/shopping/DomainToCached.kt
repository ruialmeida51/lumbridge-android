package com.eyther.lumbridge.domain.mapper.shopping

import com.eyther.lumbridge.data.model.shopping.local.ShoppingListCached
import com.eyther.lumbridge.data.model.shopping.local.ShoppingListEntryCached
import com.eyther.lumbridge.domain.model.shopping.ShoppingList
import com.eyther.lumbridge.domain.model.shopping.ShoppingListEntry

fun ShoppingList.toCached() = ShoppingListCached(
    id = id,
    title = title,
    showTickedItems = showTickedItems,
    entries = entries.map { it.toCached() }
)

fun ShoppingListEntry.toCached() = ShoppingListEntryCached(
    index = index,
    text = text,
    selected = selected
)

