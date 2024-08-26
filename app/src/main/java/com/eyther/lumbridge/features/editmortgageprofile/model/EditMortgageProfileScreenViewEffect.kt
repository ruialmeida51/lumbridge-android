package com.eyther.lumbridge.features.editmortgageprofile.model

sealed interface EditMortgageProfileScreenViewEffect {
    data class ShowError(val message: String) : EditMortgageProfileScreenViewEffect
}
