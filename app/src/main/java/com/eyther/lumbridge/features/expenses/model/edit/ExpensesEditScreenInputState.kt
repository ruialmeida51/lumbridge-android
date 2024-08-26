package com.eyther.lumbridge.features.expenses.model.edit

import com.eyther.lumbridge.ui.common.composables.model.TextInputState

data class ExpensesEditScreenInputState(
    val expenseName: TextInputState = TextInputState(),
    val expenseAmount: TextInputState = TextInputState()
)
