package com.eyther.lumbridge.features.expenses.model.add

import com.eyther.lumbridge.model.expenses.ExpensesCategoryTypesUi
import com.eyther.lumbridge.ui.common.composables.model.input.DateInputState
import com.eyther.lumbridge.ui.common.composables.model.input.TextInputState
import java.time.LocalDate

data class ExpensesAddScreenInputState(
    val categoryType: ExpensesCategoryTypesUi = ExpensesCategoryTypesUi.Food,
    val amountInput: TextInputState = TextInputState(),
    val nameInput: TextInputState = TextInputState(),
    val dateInput: DateInputState = DateInputState(date = LocalDate.now())
)
