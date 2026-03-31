package com.eyther.lumbridge.features.expenses.model.edit

import com.eyther.lumbridge.domain.model.expenses.ExpensesCategoryTypesUi
import com.eyther.lumbridge.domain.model.finance.MoneyAllocationTypeUi

sealed interface ExpensesEditScreenViewState {
    data object Loading : ExpensesEditScreenViewState

    data class Content(
        val inputState: ExpensesEditScreenInputState,
        val availableCategories: List<ExpensesCategoryTypesUi>,
        val availableAllocations: List<MoneyAllocationTypeUi>,
        val shouldEnableSaveButton: Boolean
    ) : ExpensesEditScreenViewState
}
