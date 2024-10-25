package com.eyther.lumbridge.features.overview.breakdown.viewmodel

import com.eyther.lumbridge.features.overview.breakdown.model.BreakdownScreenViewState
import com.eyther.lumbridge.model.loan.LoanUi
import kotlinx.coroutines.flow.StateFlow

interface IBreakdownScreenViewModel{
    val viewState : StateFlow<BreakdownScreenViewState>

    /**
     * Attempts to delete a loan from the database.
     */
    fun onDeleteLoan(loanUi: LoanUi)
}
