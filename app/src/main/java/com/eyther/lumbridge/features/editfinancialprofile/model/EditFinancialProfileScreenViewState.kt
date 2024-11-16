package com.eyther.lumbridge.features.editfinancialprofile.model

import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.model.finance.DuodecimosTypeUi

sealed interface EditFinancialProfileScreenViewState {
    data object Loading : EditFinancialProfileScreenViewState

    data class Content(
        val locale: SupportedLocales,
        val inputState: EditFinancialProfileInputState,
        val availableDuodecimos: List<DuodecimosTypeUi>,
        val shouldEnableSaveButton: Boolean = false
    ) : EditFinancialProfileScreenViewState
}
