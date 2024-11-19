package com.eyther.lumbridge.mapper.expenses

import com.eyther.lumbridge.domain.model.expenses.ExpenseDomain
import com.eyther.lumbridge.domain.model.expenses.ExpensesCategoryTypes
import com.eyther.lumbridge.domain.model.netsalary.allocation.MoneyAllocationType
import com.eyther.lumbridge.model.expenses.ExpenseUi
import com.eyther.lumbridge.model.expenses.ExpensesCategoryTypesUi
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

fun MoneyAllocationType.toUi() = when (this) {
    is MoneyAllocationType.Luxuries -> MoneyAllocationTypeUi.Luxuries()
    is MoneyAllocationType.Necessities -> MoneyAllocationTypeUi.Necessities()
    is MoneyAllocationType.Savings -> MoneyAllocationTypeUi.Savings()
}
