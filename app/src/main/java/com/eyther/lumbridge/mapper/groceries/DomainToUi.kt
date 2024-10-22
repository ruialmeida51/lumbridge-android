package com.eyther.lumbridge.mapper.groceries

import com.eyther.lumbridge.domain.model.groceries.GroceriesList
import com.eyther.lumbridge.domain.model.groceries.GroceriesListEntry
import com.eyther.lumbridge.model.groceries.GroceriesListEntryUi
import com.eyther.lumbridge.model.groceries.GroceriesListUi

fun GroceriesList.toUi() = GroceriesListUi(
    id = id,
    showTickedItems = showTickedItems,
    title = title,
    entries = entries.map { it.toUi() }.sortedBy { it.index }
)

fun GroceriesListEntry.toUi() = GroceriesListEntryUi(
    index = index,
    text = text,
    selected = selected
)

fun List<GroceriesList>.toUi() = map { it.toUi() }
