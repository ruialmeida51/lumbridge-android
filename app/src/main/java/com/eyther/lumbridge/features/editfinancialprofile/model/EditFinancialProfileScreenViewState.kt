package com.eyther.lumbridge.features.editfinancialprofile.model

import com.eyther.lumbridge.domain.model.locale.SupportedLocales

sealed interface EditFinancialProfileScreenViewState {
    data object Loading : EditFinancialProfileScreenViewState

    data class Content(
        val locale: SupportedLocales,
        val inputState: EditFinancialProfileInputState,
        val shouldEnableSaveButton: Boolean = false
    ) : EditFinancialProfileScreenViewState
}
