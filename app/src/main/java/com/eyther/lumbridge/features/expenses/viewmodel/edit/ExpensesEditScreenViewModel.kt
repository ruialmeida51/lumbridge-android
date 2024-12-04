package com.eyther.lumbridge.features.expenses.viewmodel.edit

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyther.lumbridge.extensions.kotlin.twoDecimalPlaces
import com.eyther.lumbridge.features.expenses.model.add.ExpensesAddSurplusOrExpenseChoice
import com.eyther.lumbridge.features.expenses.model.edit.ExpensesEditScreenViewEffect
import com.eyther.lumbridge.features.expenses.model.edit.ExpensesEditScreenViewState
import com.eyther.lumbridge.features.expenses.navigation.ExpensesNavigationItem.Companion.ARG_EXPENSE_ID
import com.eyther.lumbridge.features.expenses.viewmodel.edit.delegate.ExpensesEditScreenInputHandler
import com.eyther.lumbridge.features.expenses.viewmodel.edit.delegate.IExpensesEditScreenInputHandler
import com.eyther.lumbridge.model.expenses.ExpenseUi
import com.eyther.lumbridge.model.expenses.ExpensesCategoryTypesUi
import com.eyther.lumbridge.model.finance.MoneyAllocationTypeUi
import com.eyther.lumbridge.usecase.expenses.DeleteExpenseUseCase
import com.eyther.lumbridge.usecase.expenses.GetExpenseByIdUseCase
import com.eyther.lumbridge.usecase.expenses.UpdateExpenseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Year
import javax.inject.Inject

@HiltViewModel
class ExpensesEditScreenViewModel @Inject constructor(
    private val deleteExpenseUseCase: DeleteExpenseUseCase,
    private val getExpenseByIdUseCase: GetExpenseByIdUseCase,
    private val updateExpenseUseCase: UpdateExpenseUseCase,
    private val expensesEditScreenInputHandler: ExpensesEditScreenInputHandler,
    savedStateHandle: SavedStateHandle
) : ViewModel(),
    IExpensesEditScreenViewModel,
    IExpensesEditScreenInputHandler by expensesEditScreenInputHandler {

    companion object {
        private const val TAG = "ExpensesEditScreenViewModel"
        private const val MAX_YEARS_AHEAD = 5
        private const val MAX_YEARS_BEFORE = 5
    }

    private val expenseId = checkNotNull(savedStateHandle.get<Long>(ARG_EXPENSE_ID)) {
        "Expense ID is null"
    }

    private var cachedExpense: ExpenseUi? = null

    override val viewState: MutableStateFlow<ExpensesEditScreenViewState> =
        MutableStateFlow(ExpensesEditScreenViewState.Loading)

    override val viewEffects: MutableSharedFlow<ExpensesEditScreenViewEffect> =
        MutableSharedFlow()

    init {
        fetchExpensesDetailData()
    }

    private fun fetchExpensesDetailData() {
        viewModelScope.launch {
            val expense = getExpenseByIdUseCase(expenseId).also {
                cachedExpense = it
            }

            updateInput { state ->
                state.copy(
                    expenseName = state.expenseName.copy(
                        text = expense?.expenseName
                    ),
                    expenseAmount = state.expenseAmount.copy(
                        text = expense?.expenseAmount?.twoDecimalPlaces()
                    ),
                    categoryType = ExpensesCategoryTypesUi.of(
                        ordinal = expense?.categoryType?.ordinal ?: 0
                    ),
                    allocationTypeUi = MoneyAllocationTypeUi.toDefaultAllocationFromOrdinal(
                        ordinal = expense?.allocationTypeUi?.ordinal ?: 0
                    ),
                    dateInput = state.dateInput.copy(
                        date = expense?.date
                    ),
                    surplusOrExpenseChoice = state.surplusOrExpenseChoice.copy(
                        selectedTab = if (expense?.categoryType == ExpensesCategoryTypesUi.Surplus) {
                            ExpensesAddSurplusOrExpenseChoice.Surplus.ordinal
                        } else {
                            ExpensesAddSurplusOrExpenseChoice.Expense.ordinal
                        }
                    )
                )
            }

            inputState
                .onEach { inputState ->
                    viewState.update {
                        ExpensesEditScreenViewState.Content(
                            shouldEnableSaveButton = shouldEnableSaveButton(inputState),
                            inputState = inputState,
                            availableCategories = ExpensesCategoryTypesUi.get(),
                            availableAllocations = MoneyAllocationTypeUi.get()
                        )
                    }
                }.launchIn(this)
        }
    }

    override fun delete() {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e(TAG, "Error deleting detailed expense", throwable)

            viewModelScope.launch {
                viewEffects.emit(ExpensesEditScreenViewEffect.ShowError(throwable.message.orEmpty()))
            }
        }

        viewModelScope.launch(coroutineExceptionHandler) {
            deleteExpenseUseCase(expenseId)
            viewEffects.emit(ExpensesEditScreenViewEffect.Finish)
        }
    }

    override fun save() {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e(TAG, "Error saving detailed expense", throwable)

            viewModelScope.launch {
                viewEffects.emit(ExpensesEditScreenViewEffect.ShowError(throwable.message.orEmpty()))
            }
        }

        viewModelScope.launch(coroutineExceptionHandler) {
            updateExpenseUseCase(
                ExpenseUi(
                    id = expenseId,
                    categoryType = inputState.value.categoryType,
                    allocationTypeUi = inputState.value.allocationTypeUi,
                    expenseAmount = checkNotNull(inputState.value.expenseAmount.text?.toFloat()),
                    expenseName = checkNotNull(inputState.value.expenseName.text),
                    date = checkNotNull(inputState.value.dateInput.date)
                )
            )

            viewEffects.emit(ExpensesEditScreenViewEffect.Finish)
        }
    }

    override fun getMaxSelectableYear(): Int {
        return Year.now().plusYears(MAX_YEARS_AHEAD.toLong()).value
    }

    override fun getMinSelectableYear(): Int {
        return Year.now().minusYears(MAX_YEARS_BEFORE.toLong()).value
    }
}
