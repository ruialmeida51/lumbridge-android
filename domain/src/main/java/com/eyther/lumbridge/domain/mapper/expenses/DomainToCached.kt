package com.eyther.lumbridge.domain.mapper.expenses

import com.eyther.lumbridge.data.model.expenses.local.ExpensesCategoryCached
import com.eyther.lumbridge.data.model.expenses.local.ExpensesDetailedCached
import com.eyther.lumbridge.data.model.expenses.local.ExpensesMonthCached
import com.eyther.lumbridge.domain.model.expenses.ExpensesCategoryDomain
import com.eyther.lumbridge.domain.model.expenses.ExpensesDetailedDomain
import com.eyther.lumbridge.domain.model.expenses.ExpensesMonthDomain

fun ExpensesMonthDomain.toCached() = ExpensesMonthCached(
    id = id,
    month = month.value,
    year = year.value,
    day = day,
    snapshotMonthlyNetSalary = snapshotMonthlyNetSalary,
    categories = categoryExpenses.map { it.toCached() }
)

fun ExpensesCategoryDomain.toCached() = ExpensesCategoryCached(
    id = id,
    parentMonthId = parentMonthId,
    categoryTypeOrdinal = categoryType.ordinal,
    details = detailedExpenses.map { it.toCached() }
)

fun ExpensesDetailedDomain.toCached() = ExpensesDetailedCached(
    id = id,
    parentCategoryId = parentCategoryId,
    expenseName = expenseName,
    expenseAmount = expenseAmount
)
