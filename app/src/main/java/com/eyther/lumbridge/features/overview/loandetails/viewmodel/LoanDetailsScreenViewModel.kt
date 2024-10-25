package com.eyther.lumbridge.features.overview.loandetails.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyther.lumbridge.features.overview.loandetails.model.LoanDetailsScreenViewEffect
import com.eyther.lumbridge.features.overview.loandetails.model.LoanDetailsScreenViewState
import com.eyther.lumbridge.features.overview.navigation.OverviewNavigationItem.Loan.Companion.ARG_LOAN_ID
import com.eyther.lumbridge.usecase.loan.AddPaymentToLoanUseCase
import com.eyther.lumbridge.usecase.loan.DeleteLoanUseCase
import com.eyther.lumbridge.usecase.loan.GetLoanAndCalculationsFlowUseCase
import com.eyther.lumbridge.usecase.user.profile.GetLocaleOrDefault
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoanDetailsScreenViewModel @Inject constructor(
    private val getLoanAndCalculationsFlowUseCase: GetLoanAndCalculationsFlowUseCase,
    private val addPaymentToLoanUseCase: AddPaymentToLoanUseCase,
    private val deleteLoanUseCase: DeleteLoanUseCase,
    private val getLocaleOrDefault: GetLocaleOrDefault,
    savedStateHandle: SavedStateHandle
) : ViewModel(),
    ILoanDetailsScreenViewModel {

    companion object {
        private const val TAG = "LoanDetailsScreenViewModel"
    }

    override val viewState: MutableStateFlow<LoanDetailsScreenViewState> =
        MutableStateFlow(LoanDetailsScreenViewState.Loading)

    override val viewEffects: MutableSharedFlow<LoanDetailsScreenViewEffect> =
        MutableSharedFlow()

    private val loanId = requireNotNull(savedStateHandle.get<Long>(ARG_LOAN_ID)) {
        "Loan ID must be provided or defaulted to -1"
    }

    init {
        fetchLoanAndCalculations()
    }

    private fun fetchLoanAndCalculations() {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e(TAG, "Error fetching loan and calculations", throwable)
        }

        viewModelScope.launch(coroutineExceptionHandler) {
            val locale = getLocaleOrDefault()

            getLoanAndCalculationsFlowUseCase(loanId)
                .onEach { (loan, loanCalculation) ->
                    viewState.update {
                        LoanDetailsScreenViewState.Content(
                            loanUi = loan,
                            loanCalculationUi = loanCalculation,
                            currencySymbol = locale.getCurrencySymbol()
                        )
                    }
                }
                .catch {
                    viewState.update {
                        LoanDetailsScreenViewState.Empty
                    }
                }
                .launchIn(this)
        }
    }

    override fun onDeleteLoan() {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e(TAG, "Error deleting loan", throwable)
        }

        viewModelScope.launch(coroutineExceptionHandler) {
            deleteLoanUseCase(loanId)
            viewEffects.emit(LoanDetailsScreenViewEffect.NavigateBack)
        }
    }

    override fun onPayment() {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e(TAG, "Error adding payment to loan", throwable)
        }

        viewModelScope.launch(coroutineExceptionHandler) {
            val currentState = viewState.value

            if (currentState is LoanDetailsScreenViewState.Content) {
                addPaymentToLoanUseCase(currentState.loanUi, currentState.loanCalculationUi)
            }
        }
    }
}
