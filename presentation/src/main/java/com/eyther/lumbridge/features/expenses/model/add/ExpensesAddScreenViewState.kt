package com.eyther.lumbridge.features.expenses.model.add

import com.eyther.lumbridge.model.expenses.ExpensesCategoryTypesUi
import com.eyther.lumbridge.model.finance.MoneyAllocationTypeUi

sealed interface ExpensesAddScreenViewState {
    data object Loading : ExpensesAddScreenViewState

    data class Content(
        val inputState: ExpensesAddScreenInputState,
        val availableCategories: List<ExpensesCategoryTypesUi>,
        val availableMoneyAllocations: List<MoneyAllocationTypeUi>,
        val shouldEnableSaveButton: Boolean
    ) : ExpensesAddScreenViewState

    fun asContent(): Content = this as Content
}
