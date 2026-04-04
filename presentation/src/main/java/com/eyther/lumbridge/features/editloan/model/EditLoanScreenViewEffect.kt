package com.eyther.lumbridge.features.editloan.model

sealed interface EditLoanScreenViewEffect {
    data object NavigateBack : EditLoanScreenViewEffect

    data class ShowError(
        val message: String
    ) : EditLoanScreenViewEffect
}
