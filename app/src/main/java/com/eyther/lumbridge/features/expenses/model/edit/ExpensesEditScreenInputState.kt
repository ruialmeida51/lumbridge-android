package com.eyther.lumbridge.features.expenses.model.edit

import com.eyther.lumbridge.domain.model.expenses.ExpensesCategoryTypes
import com.eyther.lumbridge.ui.common.composables.model.input.TextInputState

data class ExpensesEditScreenInputState(
    val expenseName: TextInputState = TextInputState(),
    val categoryType: ExpensesCategoryTypes = ExpensesCategoryTypes.Food,
    val expenseAmount: TextInputState = TextInputState()
)
