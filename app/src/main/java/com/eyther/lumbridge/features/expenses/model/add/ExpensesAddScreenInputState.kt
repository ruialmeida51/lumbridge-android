package com.eyther.lumbridge.features.expenses.model.add

import com.eyther.lumbridge.model.expenses.ExpensesCategoryTypesUi
import com.eyther.lumbridge.ui.common.composables.model.input.ChoiceTabState
import com.eyther.lumbridge.ui.common.composables.model.input.DateInputState
import com.eyther.lumbridge.ui.common.composables.model.input.TextInputState
import java.time.LocalDate

data class ExpensesAddScreenInputState(
    val surplusOrExpenseChoice: ChoiceTabState = ChoiceTabState(
        tabsStringRes = ExpensesAddSurplusOrExpenseChoice.entries().map { it.label }
    ),
    val categoryType: ExpensesCategoryTypesUi = ExpensesCategoryTypesUi.Food,
    val amountInput: TextInputState = TextInputState(),
    val nameInput: TextInputState = TextInputState(),
    val dateInput: DateInputState = DateInputState(date = LocalDate.now())
) {
    val isSurplusSelected: Boolean
        get() = surplusOrExpenseChoice.selectedTab == ExpensesAddSurplusOrExpenseChoice.Surplus.ordinal
}