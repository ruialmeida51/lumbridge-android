package com.eyther.lumbridge.data.mappers.expenses

import com.eyther.lumbridge.data.model.expenses.entity.ExpenseEntity
import com.eyther.lumbridge.data.model.expenses.local.ExpenseCached

fun ExpenseEntity.toCached() = ExpenseCached(
    expenseId = expenseId,
    categoryTypeOrdinal = categoryTypeOrdinal,
    date = date,
    amount = amount,
    name = name
)
