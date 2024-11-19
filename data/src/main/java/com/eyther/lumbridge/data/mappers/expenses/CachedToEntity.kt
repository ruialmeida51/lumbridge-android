package com.eyther.lumbridge.data.mappers.expenses

import com.eyther.lumbridge.data.model.expenses.entity.ExpenseEntity
import com.eyther.lumbridge.data.model.expenses.local.ExpenseCached

fun ExpenseCached.toEntity() = ExpenseEntity(
    categoryTypeOrdinal = categoryTypeOrdinal,
    allocationTypeOrdinal = allocationTypeOrdinal,
    date = date,
    amount = amount,
    name = name
)
