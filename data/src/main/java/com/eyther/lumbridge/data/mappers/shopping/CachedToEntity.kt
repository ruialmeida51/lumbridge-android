package com.eyther.lumbridge.data.mappers.shopping

import com.eyther.lumbridge.data.model.shopping.entity.ShoppingListEntity
import com.eyther.lumbridge.data.model.shopping.entity.ShoppingListEntryEntity
import com.eyther.lumbridge.data.model.shopping.local.ShoppingListCached
import com.eyther.lumbridge.data.model.shopping.local.ShoppingListEntryCached
import java.util.ArrayList

fun ShoppingListCached.toEntity() = ShoppingListEntity(
    title = title,
    showTickedItems = showTickedItems,
    shoppingListItems = ArrayList(entries.map { it.toEntity() })
)

fun ShoppingListEntryCached.toEntity() = ShoppingListEntryEntity(
    index = index,
    text = text,
    selected = selected
)
