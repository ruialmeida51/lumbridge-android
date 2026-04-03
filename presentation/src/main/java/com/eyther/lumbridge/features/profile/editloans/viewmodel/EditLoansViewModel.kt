package com.eyther.lumbridge.features.profile.editloans.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyther.lumbridge.features.profile.editloans.model.EditLoansViewState
import com.eyther.lumbridge.mapper.loan.toUi
import com.eyther.lumbridge.model.loan.LoanUi
import com.eyther.lumbridge.domain.usecase.loan.DeleteLoanUseCase
import com.eyther.lumbridge.domain.usecase.loan.GetLoansFlowUseCase
import com.eyther.lumbridge.domain.usecase.user.profile.GetLocaleOrDefault
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditLoansViewModel @Inject constructor(
    private val getLoansFlowUseCase: GetLoansFlowUseCase,
    private val deleteLoanUseCase: DeleteLoanUseCase,
    private val getLocaleOrDefault: GetLocaleOrDefault
) : ViewModel(),
    IEditLoansViewModel {

    companion object {
        private const val TAG = "EditLoansViewModel"
    }

    override val viewState: MutableStateFlow<EditLoansViewState> =
        MutableStateFlow(EditLoansViewState.Loading)

    init {
        fetchLoans()
    }

    private fun fetchLoans() {
        viewModelScope.launch {
            val locale = getLocaleOrDefault()

            getLoansFlowUseCase(locale)
                .onEach { loans ->
                    viewState.update {
                        when {
                            loans.isEmpty() -> {
                                EditLoansViewState.Empty
                            }
                            else -> {
                                EditLoansViewState.Content(
                                    loansUi = loans.map { (loan, calc) -> loan.toUi() to calc.toUi() },
                                    currencySymbol = locale.getCurrencySymbol()
                                )
                            }
                        }
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
