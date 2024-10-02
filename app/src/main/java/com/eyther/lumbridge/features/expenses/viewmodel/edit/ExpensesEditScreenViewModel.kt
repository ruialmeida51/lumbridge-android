package com.eyther.lumbridge.features.expenses.viewmodel.edit

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyther.lumbridge.domain.model.expenses.ExpensesCategoryTypes
import com.eyther.lumbridge.extensions.kotlin.twoDecimalPlaces
import com.eyther.lumbridge.features.expenses.model.edit.ExpensesEditScreenViewEffect
import com.eyther.lumbridge.features.expenses.model.edit.ExpensesEditScreenViewState
import com.eyther.lumbridge.features.expenses.navigation.ExpensesNavigationItem.Companion.ARG_EXPENSE_ID
import com.eyther.lumbridge.features.expenses.viewmodel.edit.delegate.ExpensesEditScreenInputHandler
import com.eyther.lumbridge.features.expenses.viewmodel.edit.delegate.IExpensesEditScreenInputHandler
import com.eyther.lumbridge.model.expenses.ExpensesDetailedUi
import com.eyther.lumbridge.usecase.expenses.DeleteDetailedExpenseUseCase
import com.eyther.lumbridge.usecase.expenses.GetDetailedExpenseUseCase
import com.eyther.lumbridge.usecase.expenses.GetExpenseCategoryTypeUseCase
import com.eyther.lumbridge.usecase.expenses.UpdateDetailedExpenseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpensesEditScreenViewModel @Inject constructor(
    private val deleteDetailedExpenseUseCase: DeleteDetailedExpenseUseCase,
    private val getDetailedExpenseUseCase: GetDetailedExpenseUseCase,
    private val getExpenseCategoryTypeUseCase: GetExpenseCategoryTypeUseCase,
    private val updateDetailedExpenseUseCase: UpdateDetailedExpenseUseCase,
    private val expensesEditScreenInputHandler: ExpensesEditScreenInputHandler,
    savedStateHandle: SavedStateHandle
) : ViewModel(),
    IExpensesEditScreenViewModel,
    IExpensesEditScreenInputHandler by expensesEditScreenInputHandler {

    private val detailedExpenseId = checkNotNull(savedStateHandle.get<Long>(ARG_EXPENSE_ID)) {
        "Detailed expense ID is null"
    }

    private var cachedExpense: ExpensesDetailedUi? = null

    override val viewState: MutableStateFlow<ExpensesEditScreenViewState> =
        MutableStateFlow(ExpensesEditScreenViewState.Loading)

    override val viewEffects: MutableSharedFlow<ExpensesEditScreenViewEffect> =
        MutableSharedFlow()

    init {
        fetchExpensesDetailData()
    }

    private fun fetchExpensesDetailData() {
        viewModelScope.launch {
            cachedExpense = getDetailedExpenseUseCase(detailedExpenseId)
            val cachedCategory = getExpenseCategoryTypeUseCase(cachedExpense!!.parentCategoryId)

            updateInput { state ->
                state.copy(
                    expenseName = state.expenseName.copy(
                        text = cachedExpense?.expenseName
                    ),
                    expenseAmount = state.expenseAmount.copy(
                        text = cachedExpense?.expenseAmount?.twoDecimalPlaces()
                    ),
                    categoryType = ExpensesCategoryTypes.of(
                        ordinal = cachedCategory.ordinal
                    )
                )
            }

            inputState
                .onEach { inputState ->
                    viewState.update {
                        ExpensesEditScreenViewState.Content(
                            shouldEnableSaveButton = shouldEnableSaveButton(inputState),
                            inputState = inputState,
                            availableCategories = ExpensesCategoryTypes.get()
                        )
                    }
                }.launchIn(this)
        }
    }

    override fun delete() {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e(ExpensesEditScreenViewModel::class.java.simpleName, "Error deleting detailed expense", throwable)

            viewModelScope.launch {
                viewEffects.emit(ExpensesEditScreenViewEffect.ShowError(throwable.message.orEmpty()))
            }
        }

        viewModelScope.launch(coroutineExceptionHandler) {
            deleteDetailedExpenseUseCase(detailedExpenseId)
            viewEffects.emit(ExpensesEditScreenViewEffect.Finish)
        }
    }

    override fun save() {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e(ExpensesEditScreenViewModel::class.java.simpleName, "Error saving detailed expense", throwable)

            viewModelScope.launch {
                viewEffects.emit(ExpensesEditScreenViewEffect.ShowError(throwable.message.orEmpty()))
            }
        }

        viewModelScope.launch(coroutineExceptionHandler) {
            updateDetailedExpenseUseCase(
                expensesDetailedUi = cachedExpense!!.copy(
                    expenseName = inputState.value.expenseName.text.orEmpty(),
                    expenseAmount = inputState.value.expenseAmount.text?.toFloat() ?: 0f
                ),
                categoryType = inputState.value.categoryType
            )

            viewEffects.emit(ExpensesEditScreenViewEffect.Finish)
        }
    }
}
