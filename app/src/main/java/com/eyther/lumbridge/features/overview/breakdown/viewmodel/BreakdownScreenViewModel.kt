package com.eyther.lumbridge.features.overview.breakdown.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyther.lumbridge.features.overview.breakdown.model.BreakdownScreenViewState
import com.eyther.lumbridge.model.loan.LoanUi
import com.eyther.lumbridge.usecase.finance.GetNetSalaryUseCase
import com.eyther.lumbridge.usecase.loan.DeleteLoanUseCase
import com.eyther.lumbridge.usecase.loan.GetLoansFlowUseCase
import com.eyther.lumbridge.usecase.user.financials.GetUserFinancialsFlow
import com.eyther.lumbridge.usecase.user.profile.GetLocaleOrDefault
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BreakdownScreenViewModel @Inject constructor(
    private val getLocaleOrDefault: GetLocaleOrDefault,
    private val getLoansFlowUseCase: GetLoansFlowUseCase,
    private val getUserFinancialsFlow: GetUserFinancialsFlow,
    private val getNetSalaryUseCase: GetNetSalaryUseCase,
    private val deleteLoanUseCase: DeleteLoanUseCase
) : ViewModel(),
    IBreakdownScreenViewModel {

    companion object {
        private const val TAG = "BreakdownScreenViewModel"
    }

    override val viewState: MutableStateFlow<BreakdownScreenViewState> =
        MutableStateFlow(BreakdownScreenViewState.Loading)

    init {
        fetchSalaryAndLoans()
    }

    private fun fetchSalaryAndLoans() {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e(TAG, "Error fetching loans", throwable)
        }

        viewModelScope.launch(coroutineExceptionHandler) {
            val locale = getLocaleOrDefault()

            combine(
                getLoansFlowUseCase(locale),
                getUserFinancialsFlow()
            ) { loanInfo, userFinancials -> loanInfo to userFinancials }
                .onEach { (loans, userFinancials) ->

                    viewState.update {
                        BreakdownScreenViewState.Content(
                            locale = locale,
                            netSalary = userFinancials?.let { getNetSalaryUseCase(it) },
                            loans = loans,
                            currencySymbol = locale.getCurrencySymbol()
                        )
                    }
                }
                .launchIn(this)
        }
    }

    override fun onDeleteLoan(loanUi: LoanUi) {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e(TAG, "💥 There was a problem deleting the loan", throwable)
        }

        viewModelScope.launch(coroutineExceptionHandler) {
            deleteLoanUseCase(loanUi.id)
        }
    }
}
