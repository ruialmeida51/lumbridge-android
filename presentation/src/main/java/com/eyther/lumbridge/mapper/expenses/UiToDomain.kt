package com.eyther.lumbridge.mapper.expenses

import com.eyther.lumbridge.domain.model.expenses.ExpenseDomain
import com.eyther.lumbridge.domain.model.expenses.ExpensesCategoryTypes
import com.eyther.lumbridge.domain.model.netsalary.allocation.MoneyAllocationType
import com.eyther.lumbridge.model.expenses.ExpenseUi
import com.eyther.lumbridge.model.expenses.ExpensesCategoryTypesUi
import com.eyther.lumbridge.model.finance.MoneyAllocationTypeUi

fun ExpenseUi.toDomain() = ExpenseDomain(
    id = id,
    categoryType = categoryType.toDomain(),
    expenseName = expenseName,
    expenseAmount = expenseAmount,
    allocation = allocationTypeUi.toDomain(),
    date = date
)

fun ExpensesCategoryTypesUi.toDomain() = when (this) {
    ExpensesCategoryTypesUi.Food -> ExpensesCategoryTypes.Food
    ExpensesCategoryTypesUi.Transportation -> ExpensesCategoryTypes.Transportation
    ExpensesCategoryTypesUi.HealthCare -> ExpensesCategoryTypes.HealthCare
    ExpensesCategoryTypesUi.Entertainment -> ExpensesCategoryTypes.Entertainment
    ExpensesCategoryTypesUi.Housing -> ExpensesCategoryTypes.Housing
    ExpensesCategoryTypesUi.Education -> ExpensesCategoryTypes.Education
    ExpensesCategoryTypesUi.Other -> ExpensesCategoryTypes.Other
    ExpensesCategoryTypesUi.Pets -> ExpensesCategoryTypes.Pets
    ExpensesCategoryTypesUi.Sports -> ExpensesCategoryTypes.Sports
    ExpensesCategoryTypesUi.Vacations -> ExpensesCategoryTypes.Vacations
    ExpensesCategoryTypesUi.Surplus -> ExpensesCategoryTypes.Surplus
    ExpensesCategoryTypesUi.Investments -> ExpensesCategoryTypes.Investments
}

fun MoneyAllocationTypeUi.toDomain() = when (this) {
    is MoneyAllocationTypeUi.Luxuries -> MoneyAllocationType.Luxuries
    is MoneyAllocationTypeUi.Necessities -> MoneyAllocationType.Necessities
    is MoneyAllocationTypeUi.Savings -> MoneyAllocationType.Savings
}
