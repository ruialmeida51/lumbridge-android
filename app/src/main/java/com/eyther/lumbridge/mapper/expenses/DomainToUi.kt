package com.eyther.lumbridge.mapper.expenses

import com.eyther.lumbridge.domain.model.expenses.ExpenseDomain
import com.eyther.lumbridge.domain.model.expenses.ExpensesCategoryTypes
import com.eyther.lumbridge.model.expenses.ExpenseUi
import com.eyther.lumbridge.model.expenses.ExpensesCategoryTypesUi

fun ExpenseDomain.toUi() = ExpenseUi(
    id = id,
    categoryType = categoryType.toUi(),
    expenseName = expenseName,
    expenseAmount = expenseAmount,
    date = date
)

fun ExpensesCategoryTypes.toUi() = when (this) {
    ExpensesCategoryTypes.Food -> ExpensesCategoryTypesUi.Food
    ExpensesCategoryTypes.Transportation -> ExpensesCategoryTypesUi.Transportation
    ExpensesCategoryTypes.HealthCare -> ExpensesCategoryTypesUi.HealthCare
    ExpensesCategoryTypes.Entertainment -> ExpensesCategoryTypesUi.Entertainment
    ExpensesCategoryTypes.Housing -> ExpensesCategoryTypesUi.Housing
    ExpensesCategoryTypes.Education -> ExpensesCategoryTypesUi.Education
    ExpensesCategoryTypes.Other -> ExpensesCategoryTypesUi.Other
    ExpensesCategoryTypes.Pets -> ExpensesCategoryTypesUi.Pets
    ExpensesCategoryTypes.Sports -> ExpensesCategoryTypesUi.Sports
    ExpensesCategoryTypes.Vacations -> ExpensesCategoryTypesUi.Vacations
    ExpensesCategoryTypes.Surplus -> ExpensesCategoryTypesUi.Surplus
}
