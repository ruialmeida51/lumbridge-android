package com.eyther.lumbridge.data.mappers.shopping

import com.eyther.lumbridge.data.model.shopping.entity.ShoppingListEntity
import com.eyther.lumbridge.data.model.shopping.entity.ShoppingListEntryEntity
import com.eyther.lumbridge.data.model.shopping.local.ShoppingListCached
import com.eyther.lumbridge.data.model.shopping.local.ShoppingListEntryCached

fun ShoppingListEntity.toCached() = ShoppingListCached(
    id = shoppingListId,
    showTickedItems = showTickedItems,
    title = title,
    entries = shoppingListItems.map { it.toCached() }
)

fun ShoppingListEntryEntity.toCached() = ShoppingListEntryCached(
    index = index,
    text = text,
    selected = selected
)
