package com.eyther.lumbridge.features.expenses.model.add

import com.eyther.lumbridge.domain.model.expenses.ExpensesCategoryTypes
import com.eyther.lumbridge.ui.common.composables.model.DateInputState
import com.eyther.lumbridge.ui.common.composables.model.TextInputState
import java.time.LocalDate

data class ExpensesAddScreenInputState(
    val categoryType: ExpensesCategoryTypes = ExpensesCategoryTypes.Food,
    val amountInput: TextInputState = TextInputState(),
    val nameInput: TextInputState = TextInputState(),
    val dateInput: DateInputState = DateInputState(date = LocalDate.now())
)
