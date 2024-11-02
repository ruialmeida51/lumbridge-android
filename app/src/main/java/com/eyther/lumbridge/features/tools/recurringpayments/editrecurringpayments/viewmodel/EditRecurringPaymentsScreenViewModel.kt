package com.eyther.lumbridge.features.tools.recurringpayments.editrecurringpayments.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyther.lumbridge.features.expenses.model.add.ExpensesAddSurplusOrExpenseChoice
import com.eyther.lumbridge.features.tools.navigation.ToolsNavigationItem.RecurringPayments.Companion.ARG_RECURRING_PAYMENT_ID
import com.eyther.lumbridge.features.tools.recurringpayments.editrecurringpayments.model.EditRecurringPaymentScreenViewEffects
import com.eyther.lumbridge.features.tools.recurringpayments.editrecurringpayments.model.EditRecurringPaymentsScreenViewState
import com.eyther.lumbridge.features.tools.recurringpayments.editrecurringpayments.viewmodel.delegate.EditRecurringPaymentInputHandler
import com.eyther.lumbridge.features.tools.recurringpayments.editrecurringpayments.viewmodel.delegate.IEditRecurringPaymentInputHandler
import com.eyther.lumbridge.model.expenses.ExpensesCategoryTypesUi
import com.eyther.lumbridge.model.recurringpayments.PeriodicityUi
import com.eyther.lumbridge.model.recurringpayments.RecurringPaymentUi
import com.eyther.lumbridge.usecase.recurringpayments.DeleteRecurringPaymentUseCase
import com.eyther.lumbridge.usecase.recurringpayments.GetRecurringPaymentByIdUseCase
import com.eyther.lumbridge.usecase.recurringpayments.SaveRecurringPaymentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class EditRecurringPaymentsScreenViewModel @Inject constructor(
    private val editRecurringPaymentInputHandler: EditRecurringPaymentInputHandler,
    private val deleteRecurringPaymentUseCase: DeleteRecurringPaymentUseCase,
    private val saveRecurringPaymentUseCase: SaveRecurringPaymentUseCase,
    private val getRecurringPaymentByIdUseCase: GetRecurringPaymentByIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel(),
    IEditRecurringPaymentsScreenViewModel,
    IEditRecurringPaymentInputHandler by editRecurringPaymentInputHandler {

    companion object {
        private const val TAG = "EditRecurringPaymentsScreenViewModel"
        private const val MAX_YEARS_AHEAD = 5L
    }

    override val viewState: MutableStateFlow<EditRecurringPaymentsScreenViewState> =
        MutableStateFlow(EditRecurringPaymentsScreenViewState.Loading)

    override val viewEffects: MutableSharedFlow<EditRecurringPaymentScreenViewEffects> =
        MutableSharedFlow()

    private val recurringPaymentId = requireNotNull(savedStateHandle.get<Long>(ARG_RECURRING_PAYMENT_ID)) {
        "Recurring payment ID must be provided or defaulted to -1"
    }

    private var cachedRecurringPayment: RecurringPaymentUi? = null

    init {
        fetchRecurringPayment()
    }

    private fun fetchRecurringPayment() {
        viewModelScope.launch {
            val recurringPaymentUi = getRecurringPaymentByIdUseCase(recurringPaymentId)
            cachedRecurringPayment = recurringPaymentUi

            updateInput { state ->
                state.copy(
                    paymentName = state.paymentName.copy(
                        text = recurringPaymentUi?.label
                    ),
                    paymentAmount = state.paymentAmount.copy(
                        text = recurringPaymentUi?.amountToPay?.toString()
                    ),
                    paymentStartDate = state.paymentStartDate.copy(
                        date = recurringPaymentUi?.startDate
                    ),
                    categoryType = ExpensesCategoryTypesUi.of(
                        ordinal = recurringPaymentUi?.categoryTypesUi?.ordinal ?: 0
                    ),

                    surplusOrExpenseChoice = state.surplusOrExpenseChoice.copy(
                        selectedTab = if (recurringPaymentUi?.categoryTypesUi == ExpensesCategoryTypesUi.Surplus) {
                            ExpensesAddSurplusOrExpenseChoice.Surplus.ordinal
                        } else {
                            ExpensesAddSurplusOrExpenseChoice.Expense.ordinal
                        }
                    ),
                    numOfDays = state.numOfDays.copy(
                        text = (recurringPaymentUi?.periodicity as? PeriodicityUi.EveryXDays)?.numOfDays?.toString()
                    ),
                    numOfWeeks = state.numOfWeeks.copy(
                        text = (recurringPaymentUi?.periodicity as? PeriodicityUi.EveryXWeeks)?.numOfWeeks?.toString()
                    ),
                    numOfMonths = state.numOfMonths.copy(
                        text = (recurringPaymentUi?.periodicity as? PeriodicityUi.EveryXMonths)?.numOfMonth?.toString()
                    ),
                    numOfYears = state.numOfYears.copy(
                        text = (recurringPaymentUi?.periodicity as? PeriodicityUi.EveryXYears)?.numOfYear?.toString()
                    ),
                    dayOfWeek = (recurringPaymentUi?.periodicity as? PeriodicityUi.EveryXWeeks)?.dayOfWeek ?: DayOfWeek.MONDAY,
                    dayOfMonth = state.dayOfMonth.copy(
                        text = (recurringPaymentUi?.periodicity as? PeriodicityUi.EveryXMonths)?.dayOfMonth?.toString()
                    ),
                    monthOfYear = (recurringPaymentUi?.periodicity as? PeriodicityUi.EveryXYears)?.month ?: LocalDate.now().month,
                    periodicityUi = recurringPaymentUi?.periodicity ?: PeriodicityUi.defaultFromOrdinal(0),
                    shouldNotifyWhenPaid = recurringPaymentUi?.shouldNotifyWhenPaid == true,
                )
            }

            inputState
                .onEach { inputState ->
                    viewState.update {
                        EditRecurringPaymentsScreenViewState.Content(
                            inputState = inputState,
                            availableCategories = ExpensesCategoryTypesUi.get(),
                            shouldEnableSaveButton = shouldEnableSaveButton(inputState),
                            availablePeriodicity = PeriodicityUi.getDefaults()
                        )
                    }
                }.launchIn(this)
        }
    }

    override fun onDeleteRecurringPayment(recurringPaymentId: Long) {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e(TAG, "\uD83D\uDCA5 Error deleting recurring payment", throwable)
        }

        viewModelScope.launch(coroutineExceptionHandler) {
            deleteRecurringPaymentUseCase(recurringPaymentId)
            viewEffects.emit(EditRecurringPaymentScreenViewEffects.CloseScreen)
        }
    }

    override fun saveRecurringPayment() {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e(TAG, "\uD83D\uDCA5 Error saving recurring payment", throwable)
        }

        viewModelScope.launch(coroutineExceptionHandler) {
            val inputState = inputState.value

            val recurringPayment = RecurringPaymentUi(
                id = recurringPaymentId,
                startDate = checkNotNull(inputState.paymentStartDate.date),
                label = checkNotNull(inputState.paymentName.text),
                amountToPay = checkNotNull(inputState.paymentAmount.text).toFloat(),
                mostRecentPaymentDate = cachedRecurringPayment?.mostRecentPaymentDate,
                periodicity = when (inputState.periodicityUi) {
                    is PeriodicityUi.EveryXDays -> PeriodicityUi.EveryXDays(
                        numOfDays = checkNotNull(inputState.numOfDays.text?.toIntOrNull())
                    )

                    is PeriodicityUi.EveryXWeeks -> PeriodicityUi.EveryXWeeks(
                        numOfWeeks = checkNotNull(inputState.numOfWeeks.text?.toIntOrNull()),
                        dayOfWeek = inputState.dayOfWeek
                    )

                    is PeriodicityUi.EveryXMonths -> PeriodicityUi.EveryXMonths(
                        numOfMonth = checkNotNull(inputState.numOfMonths.text?.toIntOrNull()),
                        dayOfMonth = checkNotNull(inputState.dayOfMonth.text?.toIntOrNull())
                    )

                    is PeriodicityUi.EveryXYears -> PeriodicityUi.EveryXYears(
                        numOfYear = checkNotNull(inputState.numOfYears.text?.toIntOrNull()),
                        month = inputState.monthOfYear
                    )
                },
                shouldNotifyWhenPaid = inputState.shouldNotifyWhenPaid,
                categoryTypesUi = inputState.categoryType
            )

            saveRecurringPaymentUseCase(recurringPayment)
            viewEffects.emit(EditRecurringPaymentScreenViewEffects.CloseScreen)
        }
    }

    override fun getMaxSelectableYear(): Int {
        return LocalDate.now().plusYears(MAX_YEARS_AHEAD).year
    }
}
