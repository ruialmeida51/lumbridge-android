package com.eyther.lumbridge.features.editmortgageprofile.model

import com.eyther.lumbridge.domain.model.locale.SupportedLocales

sealed interface EditMortgageProfileScreenViewState {
    data object Loading : EditMortgageProfileScreenViewState

    data class Content(
        val locale: SupportedLocales,
        val inputState: EditMortgageProfileInputState,
        val shouldEnableSaveButton: Boolean = false
    ) : EditMortgageProfileScreenViewState
}
