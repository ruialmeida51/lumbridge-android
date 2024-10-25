package com.eyther.lumbridge.features.overview.loandetails.viewmodel

import com.eyther.lumbridge.features.overview.loandetails.model.LoanDetailsScreenViewEffect
import com.eyther.lumbridge.features.overview.loandetails.model.LoanDetailsScreenViewState
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface ILoanDetailsScreenViewModel {
    val viewState: StateFlow<LoanDetailsScreenViewState>
    val viewEffects: SharedFlow<LoanDetailsScreenViewEffect>

    /**
     * Attempts to delete the current loan from the database.
     */
    fun onDeleteLoan()

    /**
     * Tries to add a payment to the loan.
     */
    fun onPayment()
}
