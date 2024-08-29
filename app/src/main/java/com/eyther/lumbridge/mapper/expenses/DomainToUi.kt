package com.eyther.lumbridge.mapper.expenses

import com.eyther.lumbridge.R
import com.eyther.lumbridge.domain.model.expenses.ExpensesCategoryDomain
import com.eyther.lumbridge.domain.model.expenses.ExpensesCategoryTypes
import com.eyther.lumbridge.domain.model.expenses.ExpensesDetailedDomain
import com.eyther.lumbridge.domain.model.expenses.ExpensesMonthDomain
import com.eyther.lumbridge.model.expenses.ExpensesCategoryUi
import com.eyther.lumbridge.model.expenses.ExpensesDetailedUi
import com.eyther.lumbridge.model.expenses.ExpensesMonthUi
import com.eyther.lumbridge.model.finance.NetSalaryUi

fun ExpensesMonthDomain.toUi(
    netSalaryUi: NetSalaryUi?
): ExpensesMonthUi{
    val totalSpent = categoryExpenses
        .flatMap { it.detailedExpenses }
        .sumOf { it.expenseAmount.toDouble() }
        .toFloat()

    return ExpensesMonthUi(
        id = id,
        month = month,
        year = year,
        spent = totalSpent,
        remainder = if (netSalaryUi == null) 0f else netSalaryUi.monthlyNetSalary - totalSpent,
        categoryExpenses = categoryExpenses.map { it.toUi() }
    )
}

fun ExpensesCategoryDomain.toUi(): ExpensesCategoryUi {
    val categorySpent = detailedExpenses
        .sumOf { it.expenseAmount.toDouble() }
        .toFloat()

    return ExpensesCategoryUi(
        id = id,
        parentMonthId = parentMonthId,
        categoryType = categoryType,
        icon = categoryType.getCategoryDisplayIcon(),
        spent = categorySpent,
        expensesDetailedUi = detailedExpenses.map { it.toUi() }
    )
}

fun ExpensesDetailedDomain.toUi() = ExpensesDetailedUi(
    id = id,
    parentCategoryId = parentCategoryId,
    expenseName = expenseName,
    expenseAmount = expenseAmount
)

private fun ExpensesCategoryTypes.getCategoryDisplayIcon(): Int {
    return when (this) {
        ExpensesCategoryTypes.Food -> R.drawable.ic_food
        ExpensesCategoryTypes.Transportation -> R.drawable.ic_commute
        ExpensesCategoryTypes.HealthCare -> R.drawable.ic_health
        ExpensesCategoryTypes.Entertainment -> R.drawable.ic_entertainment
        ExpensesCategoryTypes.Housing -> R.drawable.ic_home
        ExpensesCategoryTypes.Education -> R.drawable.ic_education
        ExpensesCategoryTypes.Other -> R.drawable.ic_category
    }
}
