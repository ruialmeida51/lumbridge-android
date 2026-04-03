package com.eyther.lumbridge.features.tools.recurringpayments.model.edit

import com.eyther.lumbridge.model.expenses.ExpensesCategoryTypesUi
import com.eyther.lumbridge.model.finance.MoneyAllocationTypeUi
import com.eyther.lumbridge.model.time.PeriodicityUi

sealed interface EditRecurringPaymentsScreenViewState {
    data object Loading : EditRecurringPaymentsScreenViewState

    data class Content(
        val inputState: EditRecurringPaymentScreenInputState,
        val availableCategories: List<ExpensesCategoryTypesUi>,
        val availableMoneyAllocations: List<MoneyAllocationTypeUi>,
        val availablePeriodicity: List<PeriodicityUi>,
        val shouldEnableSaveButton: Boolean
    ) : EditRecurringPaymentsScreenViewState
}
