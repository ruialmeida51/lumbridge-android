package com.eyther.lumbridge.features.expenses.viewmodel.edit

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyther.lumbridge.features.expenses.model.edit.ExpensesEditScreenViewEffect
import com.eyther.lumbridge.features.expenses.model.edit.ExpensesEditScreenViewState
import com.eyther.lumbridge.features.expenses.viewmodel.edit.delegate.ExpensesEditScreenInputHandler
import com.eyther.lumbridge.features.expenses.viewmodel.edit.delegate.IExpensesEditScreenInputHandler
import com.eyther.lumbridge.model.expenses.ExpensesDetailedUi
import com.eyther.lumbridge.usecase.expenses.DeleteDetailedExpenseUseCase
import com.eyther.lumbridge.usecase.expenses.GetDetailedExpenseUseCase
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
    private val updateDetailedExpenseUseCase: UpdateDetailedExpenseUseCase,
    private val expensesEditScreenInputHandler: ExpensesEditScreenInputHandler,
    savedStateHandle: SavedStateHandle
) : ViewModel(),
    IExpensesEditScreenViewModel,
    IExpensesEditScreenInputHandler by expensesEditScreenInputHandler {

    private val detailedExpenseId = checkNotNull(savedStateHandle.get<Long>("expenseId")) {
        "Detailed expense ID is null"
    }

    private var cachedExpense: ExpensesDetailedUi? = null

    override val viewState: MutableStateFlow<ExpensesEditScreenViewState> =
        MutableStateFlow(ExpensesEditScreenViewState.Loading)

    override val viewEffects: MutableSharedFlow<ExpensesEditScreenViewEffect> =
        MutableSharedFlow()

    init {
        Log.d(ExpensesEditScreenViewModel::class.java.simpleName, "Initializing ExpensesEditScreenViewModel: $detailedExpenseId")
        fetchExpensesDetailData()
    }

    private fun fetchExpensesDetailData() {
        viewModelScope.launch {
            cachedExpense = getDetailedExpenseUseCase(detailedExpenseId)

            updateInput { state ->
                state.copy(
                    expenseName = state.expenseName.copy(
                        text = cachedExpense?.expenseName
                    ),
                    expenseAmount = state.expenseAmount.copy(
                        text = cachedExpense?.expenseAmount.toString()
                    )
                )
            }

            inputState
                .onEach { inputState ->
                    viewState.update {
                        ExpensesEditScreenViewState.Content(
                            shouldEnableSaveButton = shouldEnableSaveButton(inputState),
                            inputState = inputState
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
                cachedExpense!!.copy(
                    expenseName = inputState.value.expenseName.text.orEmpty(),
                    expenseAmount = inputState.value.expenseAmount.text?.toFloat() ?: 0f
                )
            )

            viewEffects.emit(ExpensesEditScreenViewEffect.Finish)
        }
    }
}
