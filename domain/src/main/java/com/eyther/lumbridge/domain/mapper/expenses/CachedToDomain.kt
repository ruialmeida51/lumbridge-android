package com.eyther.lumbridge.domain.mapper.expenses

import com.eyther.lumbridge.data.model.expenses.local.ExpensesCategoryCached
import com.eyther.lumbridge.data.model.expenses.local.ExpensesDetailedCached
import com.eyther.lumbridge.data.model.expenses.local.ExpensesMonthCached
import com.eyther.lumbridge.domain.model.expenses.ExpensesCategoryDomain
import com.eyther.lumbridge.domain.model.expenses.ExpensesCategoryTypes
import com.eyther.lumbridge.domain.model.expenses.ExpensesDetailedDomain
import com.eyther.lumbridge.domain.model.expenses.ExpensesMonthDomain
import java.time.Month
import java.time.Year

fun ExpensesMonthCached.toDomain() = ExpensesMonthDomain(
    id = id,
    month = Month.of(month),
    year = Year.of(year),
    day = day,
    snapshotMonthlyNetSalary = snapshotMonthlyNetSalary,
    categoryExpenses = categories.map { it.toDomain() }
)

fun ExpensesCategoryCached.toDomain() = ExpensesCategoryDomain(
    id = id,
    parentMonthId = parentMonthId,
    categoryType = ExpensesCategoryTypes.of(categoryTypeOrdinal),
    detailedExpenses = details.map { it.toDomain() }
)

fun ExpensesDetailedCached.toDomain() = ExpensesDetailedDomain(
    id = id,
    parentCategoryId = parentCategoryId,
    expenseName = expenseName,
    expenseAmount = expenseAmount
)
