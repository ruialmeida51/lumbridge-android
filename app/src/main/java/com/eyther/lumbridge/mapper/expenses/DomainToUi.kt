package com.eyther.lumbridge.mapper.expenses

import com.eyther.lumbridge.domain.model.expenses.ExpensesCategoryDomain
import com.eyther.lumbridge.domain.model.expenses.ExpensesCategoryTypes
import com.eyther.lumbridge.domain.model.expenses.ExpenseDomain
import com.eyther.lumbridge.domain.model.expenses.ExpensesMonthDomain
import com.eyther.lumbridge.domain.model.expenses.MonthAllocationDomain
import com.eyther.lumbridge.domain.model.netsalary.allocation.MoneyAllocationType
import com.eyther.lumbridge.model.expenses.ExpenseUi
import com.eyther.lumbridge.model.expenses.ExpensesCategoryTypesUi
import com.eyther.lumbridge.model.expenses.ExpensesCategoryUi
import com.eyther.lumbridge.model.expenses.ExpensesDetailedUi
import com.eyther.lumbridge.model.expenses.ExpensesMonthAllocationUi
import com.eyther.lumbridge.model.expenses.ExpensesMonthUi
import com.eyther.lumbridge.model.finance.MoneyAllocationTypeUi

fun ExpenseDomain.toUi() = ExpenseUi(
    id = id,
    categoryType = categoryType.toUi(),
    expenseName = expenseName,
    expenseAmount = expenseAmount,
    allocationTypeUi = allocation.toUi(),
    date = date
)

fun ExpensesCategoryTypes.toUi() = when (this) {
    is ExpensesCategoryTypes.Food -> ExpensesCategoryTypesUi.Food
    is ExpensesCategoryTypes.Transportation -> ExpensesCategoryTypesUi.Transportation
    is ExpensesCategoryTypes.HealthCare -> ExpensesCategoryTypesUi.HealthCare
    is ExpensesCategoryTypes.Entertainment -> ExpensesCategoryTypesUi.Entertainment
    is ExpensesCategoryTypes.Housing -> ExpensesCategoryTypesUi.Housing
    is ExpensesCategoryTypes.Education -> ExpensesCategoryTypesUi.Education
    is ExpensesCategoryTypes.Other -> ExpensesCategoryTypesUi.Other
    is ExpensesCategoryTypes.Pets -> ExpensesCategoryTypesUi.Pets
    is ExpensesCategoryTypes.Sports -> ExpensesCategoryTypesUi.Sports
    is ExpensesCategoryTypes.Vacations -> ExpensesCategoryTypesUi.Vacations
    is ExpensesCategoryTypes.Surplus -> ExpensesCategoryTypesUi.Surplus
    is ExpensesCategoryTypes.Investments -> ExpensesCategoryTypesUi.Investments
}

fun MoneyAllocationType.toUi(allocated: Float = 0f) = when (this) {
    is MoneyAllocationType.Luxuries -> MoneyAllocationTypeUi.Luxuries(allocated)
    is MoneyAllocationType.Necessities -> MoneyAllocationTypeUi.Necessities(allocated)
    is MoneyAllocationType.Savings -> MoneyAllocationTypeUi.Savings(allocated)
}

fun ExpensesMonthDomain.toUi() = ExpensesMonthUi(
    month = month,
    year = year,
    spent = spent,
    gained = gained,
    remainder = remainder,
    snapshotMonthlyNetSalary = snapshotMonthlyNetSalary,
    snapshotAllocations = snapshotAllocations.map { it.toUi() },
    categoryExpenses = categoryExpenses.map { it.toUi() }.sortedBy { it.categoryType.orderOfAppearance }
)

fun ExpensesCategoryDomain.toUi() = ExpensesCategoryUi(
    categoryType = categoryType.toUi(),
    spent = spent,
    expensesDetailedUi = expenses.map { it.toDetailedUi() }
)

fun ExpenseDomain.toDetailedUi() = ExpensesDetailedUi(
    id = id,
    date = date,
    expenseAmount = expenseAmount,
    expenseName = expenseName,
    allocationTypeUi = allocation.toUi()
)

fun MonthAllocationDomain.toUi() = ExpensesMonthAllocationUi(
    type = type.toUi(allocated),
    spent = spent,
    gained = gained
)
