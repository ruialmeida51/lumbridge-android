package com.eyther.lumbridge.features.editmortgageprofile.model

sealed interface EditMortgageProfileScreenViewEffect {
    // We need an initial value in compose.. /shrug
    data object None: EditMortgageProfileScreenViewEffect

    data class ShowError(val message: String) : EditMortgageProfileScreenViewEffect
}
