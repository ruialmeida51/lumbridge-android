package com.eyther.lumbridge.features.profile.editloans.viewmodel

import com.eyther.lumbridge.features.profile.editloans.model.EditLoansViewState
import com.eyther.lumbridge.model.loan.LoanUi
import kotlinx.coroutines.flow.StateFlow

interface IEditLoansViewModel {
    val viewState: StateFlow<EditLoansViewState>

    /**
     * Attempts to delete a loan from the database.
     */
    fun onDeleteLoan(loanUi: LoanUi)
}
