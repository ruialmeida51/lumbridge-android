package com.eyther.lumbridge.mapper.shopping

import com.eyther.lumbridge.domain.model.shopping.ShoppingList
import com.eyther.lumbridge.domain.model.shopping.ShoppingListEntry
import com.eyther.lumbridge.model.shopping.ShoppingListEntryUi
import com.eyther.lumbridge.model.shopping.ShoppingListUi

fun ShoppingListUi.toDomain() = ShoppingList(
    id = id,
    showTickedItems = showTickedItems,
    title = title,
    entries = entries.map { it.toDomain() }
)

fun ShoppingListEntryUi.toDomain() = ShoppingListEntry(
    index = index,
    text = text,
    selected = selected
)
