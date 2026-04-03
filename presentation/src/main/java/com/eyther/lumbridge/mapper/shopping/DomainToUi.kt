package com.eyther.lumbridge.mapper.shopping

import com.eyther.lumbridge.domain.model.shopping.ShoppingList
import com.eyther.lumbridge.domain.model.shopping.ShoppingListEntry
import com.eyther.lumbridge.model.shopping.ShoppingListEntryUi
import com.eyther.lumbridge.model.shopping.ShoppingListUi

fun ShoppingList.toUi() = ShoppingListUi(
    id = id,
    showTickedItems = showTickedItems,
    title = title,
    entries = entries.map { it.toUi() }.sortedBy { it.index }
)

fun ShoppingListEntry.toUi() = ShoppingListEntryUi(
    index = index,
    text = text,
    selected = selected
)

fun List<ShoppingList>.toUi() = map { it.toUi() }
