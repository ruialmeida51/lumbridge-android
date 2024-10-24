package com.eyther.lumbridge.features.editloan.model

import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.model.loan.LoanCategoryUi

sealed interface EditLoanScreenViewState {
    data object Loading : EditLoanScreenViewState

    data class Content(
        val locale: SupportedLocales,
        val inputState: EditLoanScreenInputState,
        val availableLoanCategories: List<LoanCategoryUi>,
        val shouldEnableSaveButton: Boolean = false
    ) : EditLoanScreenViewState
}
