package com.eyther.lumbridge.features.expenses.viewmodel.add

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyther.lumbridge.features.expenses.model.add.ExpensesAddScreenViewEffect
import com.eyther.lumbridge.features.expenses.model.add.ExpensesAddScreenViewState
import com.eyther.lumbridge.features.expenses.navigation.ExpensesNavigationItem.Companion.ARG_MONTH
import com.eyther.lumbridge.features.expenses.navigation.ExpensesNavigationItem.Companion.ARG_YEAR
import com.eyther.lumbridge.features.expenses.viewmodel.add.delegate.ExpensesAddScreenInputHandler
import com.eyther.lumbridge.features.expenses.viewmodel.add.delegate.IExpensesAddScreenInputHandler
import com.eyther.lumbridge.model.expenses.ExpenseUi
import com.eyther.lumbridge.model.expenses.ExpensesCategoryTypesUi
import com.eyther.lumbridge.model.finance.MoneyAllocationTypeUi
import com.eyther.lumbridge.usecase.expenses.SaveExpenseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Year
import javax.inject.Inject

@HiltViewModel
class ExpensesAddScreenViewModel @Inject constructor(
    private val expensesAddScreenInputHandler: ExpensesAddScreenInputHandler,
    private val saveExpenseUseCase: SaveExpenseUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel(),
    IExpensesAddScreenViewModel,
    IExpensesAddScreenInputHandler by expensesAddScreenInputHandler {

    companion object {
        private const val TAG = "ExpensesAddScreenViewModel"
        private const val MAX_YEARS_AHEAD = 5
        private const val MAX_YEARS_BEFORE = 5
    }

    override val viewState: MutableStateFlow<ExpensesAddScreenViewState> =
        MutableStateFlow(ExpensesAddScreenViewState.Loading)

    override val viewEffects: MutableSharedFlow<ExpensesAddScreenViewEffect> =
        MutableSharedFlow()

    private val year: Int = checkNotNull(savedStateHandle.get<Int>(ARG_YEAR)) { "\uD83D\uDCA5 Year is required" }
    private val month: Int = checkNotNull(savedStateHandle.get<Int>(ARG_MONTH)) { "\uD83D\uDCA5 Month is required" }

    init {
        viewModelScope.launch {
            viewState.update {
                ExpensesAddScreenViewState.Content(
                    inputState = expensesAddScreenInputHandler.inputState.value,
                    availableCategories = ExpensesCategoryTypesUi.get(),
                    availableMoneyAllocations = MoneyAllocationTypeUi.get(),
                    shouldEnableSaveButton = false
                )
            }

            if (year != -1 && month != -1) {
                val currentDate = LocalDate.now()

                updateInput { inputState ->
                    inputState.copy(
                        dateInput = inputState.dateInput.copy(
                            date = Year.of(year).atMonth(month).atDay(
                                if (currentDate.year == year && currentDate.monthValue == month) currentDate.dayOfMonth else 1
                            )
                        )
                    )
                }
            }

            inputState
                .onEach { inputState ->
                    viewState.update { state ->
                        state.asContent().copy(
                            inputState = inputState,
                            shouldEnableSaveButton = shouldEnableSaveButton(inputState)
                        )
                    }
                }
                .launchIn(this)
        }
    }

    override fun getMaxSelectableYear(): Int {
        return Year.now().plusYears(MAX_YEARS_AHEAD.toLong()).value
    }

    override fun getMinSelectableYear(): Int {
        return Year.now().minusYears(MAX_YEARS_BEFORE.toLong()).value
    }

    override fun onAddExpense() {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e(TAG, "Error saving expense", throwable)

            viewModelScope.launch {
                viewEffects.emit(
                    ExpensesAddScreenViewEffect.ShowError(throwable.message ?: "Error saving expense")
                )
            }
        }

        viewModelScope.launch(coroutineExceptionHandler) {
            val inputState = viewState.value.asContent().inputState

            saveExpenseUseCase(
                ExpenseUi(
                    categoryType = inputState.categoryType,
                    allocationTypeUi = inputState.allocationTypeUi,
                    expenseAmount = checkNotNull(inputState.amountInput.text?.toFloat()),
                    expenseName = checkNotNull(inputState.nameInput.text),
                    date = checkNotNull(inputState.dateInput.date)
                )
            )

            viewEffects.emit(ExpensesAddScreenViewEffect.Finish)

        }
    }
}
