package com.eyther.lumbridge.data.mappers.expenses

import com.eyther.lumbridge.data.model.expenses.entity.ExpensesCategoryEntity
import com.eyther.lumbridge.data.model.expenses.entity.ExpensesDetailedEntity
import com.eyther.lumbridge.data.model.expenses.local.ExpensesCategoryCached
import com.eyther.lumbridge.data.model.expenses.local.ExpensesDetailedCached
import com.eyther.lumbridge.data.model.expenses.local.ExpensesMonthCached
import com.eyther.lumbridge.data.model.expenses.query.ExpensesForMonthWithCategoryAndDetails

fun ExpensesForMonthWithCategoryAndDetails.toCached(): ExpensesMonthCached {
    return ExpensesMonthCached(
        id = expensesMonthEntity.monthId,
        month = expensesMonthEntity.month,
        year = expensesMonthEntity.year,
        day = expensesMonthEntity.day,
        snapshotMonthlyNetSalary = expensesMonthEntity.snapshotMonthlyNetSalary,
        categories = categories.map { it.expensesCategoryEntity.toCached(it.detailedExpenses) }
    )
}

fun ExpensesCategoryEntity.toCached(details: List<ExpensesDetailedEntity>): ExpensesCategoryCached {
    return ExpensesCategoryCached(
        id = categoryId,
        parentMonthId = parentMonthId,
        categoryTypeOrdinal = categoryTypeOrdinal,
        details = details.map { it.toCached() }
    )
}

fun ExpensesDetailedEntity.toCached(): ExpensesDetailedCached {
    return ExpensesDetailedCached(
        id = detailId,
        parentCategoryId = parentCategoryId,
        expenseName = expenseName,
        expenseAmount = expenseAmount
    )
}
