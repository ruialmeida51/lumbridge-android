package com.eyther.lumbridge.mapper.expenses

import com.eyther.lumbridge.domain.model.expenses.ExpensesCategoryDomain
import com.eyther.lumbridge.domain.model.expenses.ExpensesDetailedDomain
import com.eyther.lumbridge.domain.model.expenses.ExpensesMonthDomain
import com.eyther.lumbridge.model.expenses.ExpensesCategoryUi
import com.eyther.lumbridge.model.expenses.ExpensesDetailedUi
import com.eyther.lumbridge.model.expenses.ExpensesMonthUi

fun ExpensesMonthUi.toDomain(): ExpensesMonthDomain {
    return ExpensesMonthDomain(
        id = id,
        month = month,
        year = year,
        day = day,
        snapshotMonthlyNetSalary = snapshotMonthlyNetSalary,
        categoryExpenses = categoryExpenses.map { it.toDomain() }
    )
}

fun ExpensesCategoryUi.toDomain() = ExpensesCategoryDomain(
    id = id,
    parentMonthId = parentMonthId,
    categoryType = categoryType,
    detailedExpenses = expensesDetailedUi.map { it.toDomain() }
)

fun ExpensesDetailedUi.toDomain() = ExpensesDetailedDomain(
    id = id,
    parentCategoryId = parentCategoryId,
    expenseName = expenseName,
    expenseAmount = expenseAmount
)
