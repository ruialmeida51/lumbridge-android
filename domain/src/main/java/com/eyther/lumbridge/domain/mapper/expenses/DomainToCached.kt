package com.eyther.lumbridge.domain.mapper.expenses

import com.eyther.lumbridge.data.model.expenses.local.ExpenseCached
import com.eyther.lumbridge.domain.model.expenses.ExpenseDomain

fun ExpenseDomain.toCached() = ExpenseCached(
    expenseId = id,
    categoryTypeOrdinal = categoryType.ordinal,
    allocationTypeOrdinal = allocation.ordinal,
    name = expenseName,
    amount = expenseAmount,
    date = date
)
