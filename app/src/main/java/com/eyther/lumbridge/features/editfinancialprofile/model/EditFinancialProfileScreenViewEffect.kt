package com.eyther.lumbridge.features.editfinancialprofile.model

sealed interface EditFinancialProfileScreenViewEffect {
    // We need an initial value in compose.. /shrug
    data object None: EditFinancialProfileScreenViewEffect

    data class ShowError(val message: String) : EditFinancialProfileScreenViewEffect
}
