package com.eyther.lumbridge.domain.mapper.expenses

import com.eyther.lumbridge.data.model.expenses.local.ExpenseCached
import com.eyther.lumbridge.domain.model.expenses.ExpenseDomain
import com.eyther.lumbridge.domain.model.expenses.ExpensesCategoryTypes
import com.eyther.lumbridge.domain.model.netsalary.allocation.MoneyAllocationType

fun ExpenseCached.toDomain() = ExpenseDomain(
    id = expenseId,
    categoryType = ExpensesCategoryTypes.of(categoryTypeOrdinal),
    allocation = MoneyAllocationType.of(allocationTypeOrdinal),
    expenseName = name,
    expenseAmount = amount,
    date = date
)

fun List<ExpenseCached>.toDomain() = map { it.toDomain() }
