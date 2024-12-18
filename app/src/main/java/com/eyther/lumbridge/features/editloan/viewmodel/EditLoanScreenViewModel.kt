package com.eyther.lumbridge.features.editloan.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyther.lumbridge.features.editloan.model.EditLoanFixedTypeChoice
import com.eyther.lumbridge.features.editloan.model.EditLoanScreenInputState
import com.eyther.lumbridge.features.editloan.model.EditLoanScreenViewEffect
import com.eyther.lumbridge.features.editloan.model.EditLoanScreenViewState
import com.eyther.lumbridge.features.editloan.model.EditLoanScreenViewState.Loading
import com.eyther.lumbridge.features.editloan.model.EditLoanVariableOrFixedChoice
import com.eyther.lumbridge.features.editloan.viewmodel.IEditLoanScreenViewModel.Companion.MORTGAGE_MAX_DURATION
import com.eyther.lumbridge.features.editloan.viewmodel.IEditLoanScreenViewModel.Companion.PADDING_YEARS
import com.eyther.lumbridge.features.editloan.viewmodel.delegate.EditLoanScreenInputHandler
import com.eyther.lumbridge.features.editloan.viewmodel.delegate.IEditLoanScreenInputHandler
import com.eyther.lumbridge.features.overview.navigation.OverviewNavigationItem.Loan.Companion.ARG_LOAN_ID
import com.eyther.lumbridge.model.loan.LoanCategoryUi
import com.eyther.lumbridge.model.loan.LoanInterestRateUi
import com.eyther.lumbridge.model.loan.LoanUi
import com.eyther.lumbridge.shared.time.extensions.monthsUntil
import com.eyther.lumbridge.shared.time.extensions.toLocalDate
import com.eyther.lumbridge.usecase.loan.GetLoanAndCalculationsUseCase
import com.eyther.lumbridge.usecase.loan.SaveLoanUseCase
import com.eyther.lumbridge.usecase.user.profile.GetLocaleOrDefault
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class EditLoanScreenViewModel @Inject constructor(
    private val getLocaleOrDefault: GetLocaleOrDefault,
    private val getLoanAndCalculationsUseCase: GetLoanAndCalculationsUseCase,
    private val saveLoanUseCase: SaveLoanUseCase,
    private val editLoanInputHandler: EditLoanScreenInputHandler,
    savedStateHandle: SavedStateHandle
) : ViewModel(),
    IEditLoanScreenViewModel,
    IEditLoanScreenInputHandler by editLoanInputHandler {

    override val viewState = MutableStateFlow<EditLoanScreenViewState>(Loading)
    override val viewEffects = MutableSharedFlow<EditLoanScreenViewEffect>()

    private val loanId = requireNotNull(savedStateHandle.get<Long>(ARG_LOAN_ID)) {
        "Loan ID must be provided or defaulted to -1"
    }

    private var cachedLoanUi: LoanUi? = null

    init {
        fetchLoan()
    }

    private fun fetchLoan() {
        viewModelScope.launch {
            val (initialLoanUi, _) = getLoanAndCalculationsUseCase(loanId)
            val locale = getLocaleOrDefault()
            cachedLoanUi = initialLoanUi

            updateInput { state ->
                state.copy(
                    name = state.name.copy(
                        text = initialLoanUi?.name
                    ),
                    initialAmount = state.initialAmount.copy(
                        text = initialLoanUi?.initialLoanAmount?.toString()
                    ),
                    currentAmount = state.currentAmount.copy(
                        text = initialLoanUi?.currentLoanAmount?.toString()
                    ),
                    euribor = state.euribor.copy(
                        text = initialLoanUi?.loanInterestRateUi?.tryGetEuribor()?.toString()
                    ),
                    spread = state.spread.copy(
                        text = initialLoanUi?.loanInterestRateUi?.tryGetSpread()?.toString()
                    ),
                    tanInterestRate = state.tanInterestRate.copy(
                        text = initialLoanUi?.loanInterestRateUi?.tryGetTanInterestRate()?.toString()
                    ),
                    taegInterestRate = state.taegInterestRate.copy(
                        text = initialLoanUi?.loanInterestRateUi?.tryGetTaegInterestRate()?.toString()
                    ),
                    startDate = state.startDate.copy(
                        date = initialLoanUi?.startDate
                    ),
                    endDate = state.endDate.copy(
                        date = initialLoanUi?.endDate
                    ),
                    fixedOrVariableLoanChoiceState = state.fixedOrVariableLoanChoiceState.copy(
                        selectedTab = getFixedOrVariableLoanFromInterestRate(initialLoanUi?.loanInterestRateUi)?.ordinal ?: 0,
                        tabsStringRes = EditLoanVariableOrFixedChoice.entries().map { it.label }
                    ),
                    tanOrTaegLoanChoiceState = state.tanOrTaegLoanChoiceState.copy(
                        selectedTab = getTanOrTaegFromInterestRate(initialLoanUi?.loanInterestRateUi?.asFixed())?.ordinal ?: 0,
                        tabsStringRes = EditLoanFixedTypeChoice.entries().map { it.label }
                    ),
                    categoryUi = initialLoanUi?.loanCategoryUi ?: LoanCategoryUi.House,
                    shouldNotifyWhenPaid = initialLoanUi?.shouldNotifyWhenPaid == true,
                    shouldAutoAddToExpenses = initialLoanUi?.shouldAutoAddToExpenses == true,
                    paymentDay = state.paymentDay.copy(
                        text = initialLoanUi?.paymentDay?.toString()
                    )
                )
            }

            inputState
                .onEach { inputState ->
                    viewState.update {
                        EditLoanScreenViewState.Content(
                            locale = locale,
                            shouldEnableSaveButton = shouldEnableSaveButton(inputState),
                            inputState = inputState,
                            availableLoanCategories = LoanCategoryUi.entries(),
                            isCreateLoan = cachedLoanUi == null
                        )
                    }
                }.launchIn(this)
        }
    }

    override fun saveLoan() {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            viewModelScope.launch {
                viewEffects.emit(
                    EditLoanScreenViewEffect.ShowError(throwable.message.orEmpty())
                )
            }
        }

        viewModelScope.launch(coroutineExceptionHandler) {
            val inputState = inputState.value
            val newStartDate = checkNotNull(inputState.startDate.date)
            val newEndDate = checkNotNull(inputState.endDate.date)

            val loanUi = LoanUi(
                id = loanId,
                name = checkNotNull(inputState.name.text),
                initialLoanAmount = checkNotNull(inputState.initialAmount.text?.toFloatOrNull()),
                currentLoanAmount = checkNotNull(inputState.currentAmount.text?.toFloatOrNull()),
                startDate = newStartDate,
                endDate = newEndDate,
                loanCategoryUi = LoanCategoryUi.fromOrdinal(inputState.categoryUi.ordinal),
                loanInterestRateUi = getLoanInterestRateUiFromInputState(inputState),
                shouldNotifyWhenPaid = inputState.shouldNotifyWhenPaid,
                shouldAutoAddToExpenses = inputState.shouldAutoAddToExpenses,
                paymentDay = inputState.paymentDay.text?.toIntOrNull(),
                currentPaymentDate = getCurrentPaymentDate(newStartDate)
            )

            saveLoanUseCase(loanUi)
            viewEffects.emit(EditLoanScreenViewEffect.NavigateBack)
        }
    }

    override fun getMaxSelectableYear(): Int {
        return LocalDate.now().year + MORTGAGE_MAX_DURATION + PADDING_YEARS
    }

    override fun getMinSelectableYear(): Int {
        return LocalDate.now().year - MORTGAGE_MAX_DURATION - PADDING_YEARS
    }

    override fun isSelectableEndDate(endDateInMillis: Long): Boolean {
        val startDate = inputState.value.startDate.date ?: return false
        return startDate.monthsUntil(endDateInMillis.toLocalDate()) >= 1
    }

    private fun getFixedOrVariableLoanFromInterestRate(interestRateUi: LoanInterestRateUi?): EditLoanVariableOrFixedChoice? {
        return when (interestRateUi) {
            is LoanInterestRateUi.Fixed -> EditLoanVariableOrFixedChoice.Fixed
            is LoanInterestRateUi.Variable -> EditLoanVariableOrFixedChoice.Variable
            else -> null
        }
    }

    private fun getTanOrTaegFromInterestRate(interestRateUi: LoanInterestRateUi.Fixed?): EditLoanFixedTypeChoice? {
        return when (interestRateUi) {
            is LoanInterestRateUi.Fixed.Tan -> EditLoanFixedTypeChoice.Tan
            is LoanInterestRateUi.Fixed.Taeg -> EditLoanFixedTypeChoice.Taeg
            else -> null
        }
    }

    private fun getLoanInterestRateUiFromInputState(inputState: EditLoanScreenInputState): LoanInterestRateUi {
        return when (inputState.fixedOrVariableLoanChoiceState.selectedTab) {
            EditLoanVariableOrFixedChoice.Variable.ordinal -> LoanInterestRateUi.Variable(
                euribor = checkNotNull(inputState.euribor.text?.toFloatOrNull()),
                spread = checkNotNull(inputState.spread.text?.toFloatOrNull())
            )

            EditLoanVariableOrFixedChoice.Fixed.ordinal -> {
                when (inputState.tanOrTaegLoanChoiceState.selectedTab) {
                    EditLoanFixedTypeChoice.Tan.ordinal -> LoanInterestRateUi.Fixed.Tan(
                        interestRate = checkNotNull(inputState.tanInterestRate.text?.toFloatOrNull())
                    )

                    EditLoanFixedTypeChoice.Taeg.ordinal -> LoanInterestRateUi.Fixed.Taeg(
                        interestRate = checkNotNull(inputState.taegInterestRate.text?.toFloatOrNull())
                    )

                    else -> throw IllegalStateException("Invalid fixed loan type")
                }
            }

            else -> throw IllegalStateException("Invalid loan type")
        }
    }

    /**
     * The current payment date is used to calculate the remaining months between the current date and the end date.
     *
     * There a few conditions to consider:
     * * If there's no cached loan, the current payment date will be RIGHT NOW.
     * * If there's a cached loan, and the start date has changed, the current payment date will be RIGHT NOW.
     * * If there's a cached loan, and the start date doesn't change, the current payment date will also not change.
     */
    private fun getCurrentPaymentDate(startDate: LocalDate): LocalDate {
        return if (cachedLoanUi == null || cachedLoanUi?.startDate != startDate) {
            return LocalDate.now()
        } else {
            checkNotNull(cachedLoanUi?.currentPaymentDate) {
                "Cached loan should have a current payment date if it's not null"
            }
        }
    }
}
