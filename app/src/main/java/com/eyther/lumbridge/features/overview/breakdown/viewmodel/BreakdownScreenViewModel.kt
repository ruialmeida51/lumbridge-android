package com.eyther.lumbridge.features.overview.breakdown.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyther.lumbridge.features.overview.breakdown.model.BreakdownScreenViewState
import com.eyther.lumbridge.model.expenses.ExpenseUi
import com.eyther.lumbridge.model.loan.LoanCalculationUi
import com.eyther.lumbridge.model.loan.LoanUi
import com.eyther.lumbridge.model.snapshotsalary.SnapshotNetSalaryUi
import com.eyther.lumbridge.model.user.UserFinancialsUi
import com.eyther.lumbridge.usecase.expenses.GetBalanceSheetUseCase
import com.eyther.lumbridge.usecase.expenses.GetExpensesStreamUseCase
import com.eyther.lumbridge.usecase.finance.GetNetSalaryUseCase
import com.eyther.lumbridge.usecase.loan.DeleteLoanUseCase
import com.eyther.lumbridge.usecase.loan.GetLoansFlowUseCase
import com.eyther.lumbridge.usecase.snapshotsalary.GetSnapshotNetSalariesFlowUseCase
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
    private val getExpensesStreamUseCase: GetExpensesStreamUseCase,
    private val getSnapshotNetSalariesFlowUseCase: GetSnapshotNetSalariesFlowUseCase,
    private val getBalanceSheetUseCase: GetBalanceSheetUseCase,
    private val getUserFinancialsFlow: GetUserFinancialsFlow,
    private val getNetSalaryUseCase: GetNetSalaryUseCase,
    private val deleteLoanUseCase: DeleteLoanUseCase
) : ViewModel(),
    IBreakdownScreenViewModel {

    companion object {
        private const val TAG = "BreakdownScreenViewModel"

        private data class DataStream(
            val expenses: List<ExpenseUi>,
            val snapshotSalaries: List<SnapshotNetSalaryUi>,
            val loans: List<Pair<LoanUi, LoanCalculationUi>>,
            val userFinancials: UserFinancialsUi?
        )
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
                getExpensesStreamUseCase(),
                getSnapshotNetSalariesFlowUseCase(),
                getLoansFlowUseCase(locale),
                getUserFinancialsFlow()
            ) { expenses, snapshotSalaries, loanInfo, userFinancials ->
                DataStream(
                    expenses = expenses,
                    snapshotSalaries = snapshotSalaries,
                    loans = loanInfo,
                    userFinancials = userFinancials
                )
            }
                .onEach { (expenses, snapshotSalaries, loans, userFinancials) ->

                    val currentNetSalary = userFinancials?.let { getNetSalaryUseCase(it) }
                    viewState.update {
                        BreakdownScreenViewState.Content(
                            locale = locale,
                            netSalary = currentNetSalary,
                            loans = loans,
                            currencySymbol = locale.getCurrencySymbol(),
                            balanceSheetNet = getBalanceSheetUseCase(
                                currentNetSalary = currentNetSalary?.monthlyNetSalary,
                                expenses = expenses,
                                snapshotSalaries = snapshotSalaries
                            )
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
