package com.eyther.lumbridge.features.profile.editloans.model

import com.eyther.lumbridge.domain.model.loan.LoanCalculationUi
import com.eyther.lumbridge.domain.model.loan.LoanUi

sealed interface EditLoansViewState {
    data object Loading : EditLoansViewState
    data object Empty : EditLoansViewState
    data class Content(
        val loansUi: List<Pair<LoanUi, LoanCalculationUi>>,
        val currencySymbol: String
    ) : EditLoansViewState
}
