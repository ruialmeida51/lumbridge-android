package com.eyther.lumbridge.features.editfinancialprofile.model

sealed interface EditFinancialProfileScreenViewEffect {
    data class ShowError(val message: String) : EditFinancialProfileScreenViewEffect
}
