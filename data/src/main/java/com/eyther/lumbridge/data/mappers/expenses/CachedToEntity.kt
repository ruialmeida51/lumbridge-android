package com.eyther.lumbridge.data.mappers.expenses

import com.eyther.lumbridge.data.model.expenses.entity.ExpensesCategoryEntity
import com.eyther.lumbridge.data.model.expenses.entity.ExpensesDetailedEntity
import com.eyther.lumbridge.data.model.expenses.entity.ExpensesMonthEntity
import com.eyther.lumbridge.data.model.expenses.local.ExpensesCategoryCached
import com.eyther.lumbridge.data.model.expenses.local.ExpensesDetailedCached
import com.eyther.lumbridge.data.model.expenses.local.ExpensesMonthCached

fun ExpensesMonthCached.toEntity() = ExpensesMonthEntity(
    month = month,
    year = year
)

fun ExpensesCategoryCached.toEntity(parentMonthId: Long) = ExpensesCategoryEntity(
    parentMonthId = parentMonthId,
    categoryTypeOrdinal = categoryTypeOrdinal
)

fun ExpensesDetailedCached.toEntity(parentCategoryId: Long) = ExpensesDetailedEntity(
    parentCategoryId = parentCategoryId,
    expenseName = expenseName,
    expenseAmount = expenseAmount
)
