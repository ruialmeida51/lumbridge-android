package com.eyther.lumbridge.features.overview.loandetails.model

import com.eyther.lumbridge.domain.model.loan.LoanCalculationUi
import com.eyther.lumbridge.domain.model.loan.LoanUi

sealed interface LoanDetailsScreenViewState {
    data object Loading: LoanDetailsScreenViewState
    data object Empty: LoanDetailsScreenViewState

    data class Content(
        val loanUi: LoanUi,
        val loanCalculationUi: LoanCalculationUi,
        val currencySymbol: String
    ): LoanDetailsScreenViewState
}
