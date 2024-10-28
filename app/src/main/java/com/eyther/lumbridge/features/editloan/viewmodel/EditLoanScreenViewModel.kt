package com.eyther.lumbridge.features.editloan.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyther.lumbridge.shared.time.monthsUntil
import com.eyther.lumbridge.shared.time.toLocalDate
import com.eyther.lumbridge.features.editloan.model.EditLoanFixedTypeUi
import com.eyther.lumbridge.features.editloan.model.EditLoanScreenInputState
import com.eyther.lumbridge.features.editloan.model.EditLoanScreenViewEffect
import com.eyther.lumbridge.features.editloan.model.EditLoanScreenViewState
import com.eyther.lumbridge.features.editloan.model.EditLoanScreenViewState.Loading
import com.eyther.lumbridge.features.editloan.model.EditLoanVariableOrFixedUi
import com.eyther.lumbridge.features.editloan.viewmodel.IEditLoanScreenViewModel.Companion.MORTGAGE_MAX_DURATION
import com.eyther.lumbridge.features.editloan.viewmodel.IEditLoanScreenViewModel.Companion.PADDING_YEARS
import com.eyther.lumbridge.features.editloan.viewmodel.delegate.EditLoanScreenInputHandler
import com.eyther.lumbridge.features.editloan.viewmodel.delegate.IEditLoanScreenInputHandler
import com.eyther.lumbridge.features.overview.navigation.OverviewNavigationItem.Loan.Companion.ARG_LOAN_ID
import com.eyther.lumbridge.model.loan.LoanCategoryUi
import com.eyther.lumbridge.model.loan.LoanInterestRateUi
import com.eyther.lumbridge.model.loan.LoanUi
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
        fetchMortgageProfile()
    }

    private fun fetchMortgageProfile() {
        viewModelScope.launch {
            val (initialLoanUi, _) = getLoanAndCalculationsUseCase(loanId)
            val locale = getLocaleOrDefault()
            cachedLoanUi = initialLoanUi

            updateInput { state ->
                state.copy(
                    name = state.name.copy(
                        text = initialLoanUi?.name
                    ),
                    loanAmount = state.loanAmount.copy(
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
                        tabsStringRes = EditLoanVariableOrFixedUi.entries().map { it.label }
                    ),
                    tanOrTaegLoanChoiceState = state.tanOrTaegLoanChoiceState.copy(
                        selectedTab = getTanOrTaegFromInterestRate(initialLoanUi?.loanInterestRateUi?.asFixed())?.ordinal ?: 0,
                        tabsStringRes = EditLoanFixedTypeUi.entries().map { it.label }
                    ),
                    categoryUi = initialLoanUi?.loanCategoryUi ?: LoanCategoryUi.House
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

            val loanUi = LoanUi(
                id = loanId,
                name = checkNotNull(inputState.name.text),
                currentLoanAmount = checkNotNull(inputState.loanAmount.text?.toFloatOrNull()),
                startDate = checkNotNull(inputState.startDate.date),
                endDate = checkNotNull(inputState.endDate.date),
                loanCategoryUi = LoanCategoryUi.fromOrdinal(inputState.categoryUi.ordinal),
                loanInterestRateUi = getLoanInterestRateUiFromInputState(inputState),
                initialLoanAmount = cachedLoanUi?.initialLoanAmount ?: checkNotNull(inputState.loanAmount.text?.toFloatOrNull())
            )

            saveLoanUseCase(loanUi)
            viewEffects.emit(EditLoanScreenViewEffect.NavigateBack)
        }
    }

    override fun getMaxSelectableYear(): Int {
        return LocalDate.now().year + MORTGAGE_MAX_DURATION + PADDING_YEARS
    }

    override fun isSelectableEndDate(endDateInMillis: Long): Boolean {
        val startDate = inputState.value.startDate.date ?: return false
        return startDate.monthsUntil(endDateInMillis.toLocalDate()) >= 1
    }

    private fun getFixedOrVariableLoanFromInterestRate(interestRateUi: LoanInterestRateUi?): EditLoanVariableOrFixedUi? {
        return when (interestRateUi) {
            is LoanInterestRateUi.Fixed -> EditLoanVariableOrFixedUi.Fixed
            is LoanInterestRateUi.Variable -> EditLoanVariableOrFixedUi.Variable
            else -> null
        }
    }

    private fun getTanOrTaegFromInterestRate(interestRateUi: LoanInterestRateUi.Fixed?): EditLoanFixedTypeUi? {
        return when (interestRateUi) {
            is LoanInterestRateUi.Fixed.Tan -> EditLoanFixedTypeUi.Tan
            is LoanInterestRateUi.Fixed.Taeg -> EditLoanFixedTypeUi.Taeg
            else -> null
        }
    }

    private fun getLoanInterestRateUiFromInputState(inputState: EditLoanScreenInputState): LoanInterestRateUi {
        return when (inputState.fixedOrVariableLoanChoiceState.selectedTab) {
            EditLoanVariableOrFixedUi.Variable.ordinal -> LoanInterestRateUi.Variable(
                euribor = checkNotNull(inputState.euribor.text?.toFloatOrNull()),
                spread = checkNotNull(inputState.spread.text?.toFloatOrNull())
            )

            EditLoanVariableOrFixedUi.Fixed.ordinal -> {
                when (inputState.tanOrTaegLoanChoiceState.selectedTab) {
                    EditLoanFixedTypeUi.Tan.ordinal -> LoanInterestRateUi.Fixed.Tan(
                        interestRate = checkNotNull(inputState.tanInterestRate.text?.toFloatOrNull())
                    )

                    EditLoanFixedTypeUi.Taeg.ordinal -> LoanInterestRateUi.Fixed.Taeg(
                        interestRate = checkNotNull(inputState.taegInterestRate.text?.toFloatOrNull())
                    )

                    else -> throw IllegalStateException("Invalid fixed loan type")
                }
            }

            else -> throw IllegalStateException("Invalid loan type")
        }
    }
}
