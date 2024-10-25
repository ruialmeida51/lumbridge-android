package com.eyther.lumbridge.features.overview.loandetails.model

sealed interface LoanDetailsScreenViewEffect {
    data object NavigateBack : LoanDetailsScreenViewEffect
}
