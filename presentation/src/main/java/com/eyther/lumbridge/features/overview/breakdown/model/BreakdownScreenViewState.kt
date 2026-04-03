package com.eyther.lumbridge.features.overview.breakdown.model

import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.model.finance.NetSalaryUi
import com.eyther.lumbridge.model.loan.LoanCalculationUi
import com.eyther.lumbridge.model.loan.LoanUi

sealed interface BreakdownScreenViewState {
    data object Loading : BreakdownScreenViewState

    data class Content(
        val locale: SupportedLocales,
        val netSalary: NetSalaryUi? = null,
        val loans: List<Pair<LoanUi, LoanCalculationUi>> = emptyList(),
        val balanceSheetNet: BalanceSheetNetUi? = null,
        val currencySymbol: String
    ) : BreakdownScreenViewState
}
