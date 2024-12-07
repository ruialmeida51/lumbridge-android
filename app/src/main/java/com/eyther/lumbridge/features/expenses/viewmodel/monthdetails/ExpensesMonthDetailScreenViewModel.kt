package com.eyther.lumbridge.features.expenses.viewmodel.monthdetails

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.domain.model.preferences.Preferences
import com.eyther.lumbridge.features.expenses.model.monthdetails.ExpensesMonthDetailScreenViewEffect
import com.eyther.lumbridge.features.expenses.model.monthdetails.ExpensesMonthDetailScreenViewState
import com.eyther.lumbridge.features.expenses.navigation.ExpensesNavigationItem.Companion.ARG_MONTH
import com.eyther.lumbridge.features.expenses.navigation.ExpensesNavigationItem.Companion.ARG_YEAR
import com.eyther.lumbridge.features.overview.breakdown.model.BalanceSheetNetUi
import com.eyther.lumbridge.model.expenses.ExpenseUi
import com.eyther.lumbridge.model.expenses.ExpensesCategoryUi
import com.eyther.lumbridge.model.expenses.ExpensesMonthUi
import com.eyther.lumbridge.model.snapshotsalary.SnapshotNetSalaryUi
import com.eyther.lumbridge.shared.di.model.Schedulers
import com.eyther.lumbridge.usecase.expenses.DeleteExpensesListUseCase
import com.eyther.lumbridge.usecase.expenses.GetBalanceSheetUseCase
import com.eyther.lumbridge.usecase.expenses.GetExpensesStreamByDateUseCase
import com.eyther.lumbridge.usecase.expenses.GroupExpensesUseCase
import com.eyther.lumbridge.usecase.preferences.GetPreferencesStream
import com.eyther.lumbridge.usecase.snapshotsalary.GetMostRecentSnapshotSalaryForDateUseCase
import com.eyther.lumbridge.usecase.snapshotsalary.GetSnapshotNetSalariesFlowUseCase
import com.eyther.lumbridge.usecase.user.profile.GetLocaleOrDefault
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpensesMonthDetailScreenViewModel @Inject constructor(
    private val getExpensesStreamByDateUseCase: GetExpensesStreamByDateUseCase,
    private val groupExpensesUseCase: GroupExpensesUseCase,
    private val getPreferencesFlow: GetPreferencesStream,
    private val getSnapshotNetSalariesFlowUseCase: GetSnapshotNetSalariesFlowUseCase,
    private val getMostRecentSnapshotSalaryForDateUseCase: GetMostRecentSnapshotSalaryForDateUseCase,
    private val getLocaleOrDefault: GetLocaleOrDefault,
    private val deleteExpensesListUseCase: DeleteExpensesListUseCase,
    private val getBalanceSheetUseCase: GetBalanceSheetUseCase,
    private val schedulers: Schedulers,
    savedStateHandle: SavedStateHandle
) : ViewModel(),
    IExpensesMonthDetailScreenViewModel {

    companion object {
        private const val TAG = "ExpensesMonthDetailScreenViewModel"

        private data class StreamData(
            val preferences: Preferences?,
            val snapshotNetSalaries: List<SnapshotNetSalaryUi>,
            val expenses: List<ExpenseUi>
        )
    }

    override val viewState: MutableStateFlow<ExpensesMonthDetailScreenViewState> =
        MutableStateFlow(ExpensesMonthDetailScreenViewState.Loading)

    override val viewEffects: MutableSharedFlow<ExpensesMonthDetailScreenViewEffect> =
        MutableSharedFlow()

    private val year: Int = checkNotNull(savedStateHandle.get<Int>(ARG_YEAR)) { "\uD83D\uDCA5 Year is required" }
    private val month: Int = checkNotNull(savedStateHandle.get<Int>(ARG_MONTH)) { "\uD83D\uDCA5 Month is required" }

    init {
        observeExpenses()
    }

    private fun observeExpenses() {
        combine(
            getExpensesStreamByDateUseCase(year, month),
            getSnapshotNetSalariesFlowUseCase(),
            getPreferencesFlow()
        ) { expenses, snapshotNetSalaries, preferences ->
            StreamData(
                preferences = preferences,
                expenses = expenses,
                snapshotNetSalaries = snapshotNetSalaries
            )
        }
            .flowOn(schedulers.io)
            .onEach { data ->
                val snapshotSalary = getMostRecentSnapshotSalaryForDateUseCase(
                    snapshotNetSalaries = data.snapshotNetSalaries,
                    year = year,
                    month = month
                )

                val groupedExpenses = groupExpensesUseCase(
                    expenses = data.expenses,
                    snapshotNetSalaries = data.snapshotNetSalaries,
                    showAllocationsOnExpenses = data.preferences?.showAllocationsOnExpenses == true,
                    shouldAddFoodCardToNecessitiesAllocation = data.preferences?.addFoodCardToNecessitiesAllocation == true
                ).firstOrNull() ?: throw IllegalStateException("\uD83D\uDCA5 No expenses found for year $year and month $month")

                val balanceSheet = getBalanceSheetUseCase(
                    currentNetSalary = snapshotSalary?.netSalary,
                    snapshotSalaries = data.snapshotNetSalaries,
                    expenses = data.expenses,
                    addFoodCardToNecessitiesAllocation = data.preferences?.addFoodCardToNecessitiesAllocation == true
                ) ?: throw IllegalStateException("\uD83D\uDCA5 Couldn't calculate balance sheet for year $year and month $month")

                viewState.update {
                    ExpensesMonthDetailScreenViewState.Content(
                        monthExpenses = groupedExpenses,
                        showAllocations = data.preferences?.showAllocationsOnExpenses == true,
                        locale = getLocaleOrDefault(),
                        month = month,
                        year = year,
                        balanceSheetNetUi = balanceSheet
                    )
                }
            }
            .catch { throwable ->
                // Catch is just a suspending function, so we need to launch a coroutine to emit the error due to buffer and stuff
                // in the shared flow
                viewModelScope.launch {
                    Log.e(TAG, "💥 Error observing expenses", throwable)
                    viewEffects.emit(ExpensesMonthDetailScreenViewEffect.ShowError(throwable.message.orEmpty()))
                }

                viewState.update { ExpensesMonthDetailScreenViewState.Error }
            }
            .flowOn(schedulers.cpu)
            .launchIn(viewModelScope)
    }

    override fun onDeleteExpense(expensesMonth: ExpensesMonthUi) {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e(TAG, "💥 Error deleting expenses", throwable)

            viewModelScope.launch {
                viewEffects.emit(ExpensesMonthDetailScreenViewEffect.ShowError(throwable.message.orEmpty()))
            }
        }

        viewModelScope.launch(schedulers.io + exceptionHandler) {
            val expensesIdToDelete = expensesMonth.categoryExpenses
                .flatMap { category ->
                    category.expensesDetailedUi.map { detail -> detail.id }
                }

            deleteExpensesListUseCase(expensesIdToDelete)
            viewEffects.emit(ExpensesMonthDetailScreenViewEffect.NavigateBack)
        }
    }

    override fun expandCategory(category: ExpensesCategoryUi) {
        viewState.update { oldState ->
            // Get the list of month expenses. The state is guaranteed to be of type Content if we reach this point,
            // since we selected a category to expand.
            val monthExpensesUiList = oldState
                .asContent()
                .monthExpenses
                .expandOrCollapseCategory(categoryUi = category, expand = !category.expanded)

            getContentState(
                monthlyExpenses = oldState.asContent().monthExpenses.copy(categoryExpenses = monthExpensesUiList),
                showAllocations = oldState.asContent().showAllocations,
                locale = oldState.asContent().locale,
                balanceSheetNetUi = oldState.asContent().balanceSheetNetUi
            )
        }
    }

    override fun collapseAll() {
        viewState.update { oldState ->
            val monthlyExpenses = oldState.asContent().monthExpenses.expandOrCollapseCategories(expand = false)

            getContentState(
                monthlyExpenses = oldState.asContent().monthExpenses.copy(categoryExpenses = monthlyExpenses),
                showAllocations = oldState.asContent().showAllocations,
                locale = oldState.asContent().locale,
                balanceSheetNetUi = oldState.asContent().balanceSheetNetUi
            )
        }
    }

    override fun expandAll() {
        viewState.update { oldState ->
            val monthlyExpenses = oldState.asContent().monthExpenses.expandOrCollapseCategories(expand = true)

            getContentState(
                monthlyExpenses = oldState.asContent().monthExpenses.copy(categoryExpenses = monthlyExpenses),
                showAllocations = oldState.asContent().showAllocations,
                locale = oldState.asContent().locale,
                balanceSheetNetUi = oldState.asContent().balanceSheetNetUi
            )
        }
    }

    private fun ExpensesMonthUi.expandOrCollapseCategories(expand: Boolean): List<ExpensesCategoryUi> {
        return categoryExpenses.map { category ->
            category.copy(expanded = expand)
        }
    }

    private fun ExpensesMonthUi.expandOrCollapseCategory(categoryUi: ExpensesCategoryUi, expand: Boolean): List<ExpensesCategoryUi> {
        return categoryExpenses.map { category ->
            if (category.categoryType == categoryUi.categoryType) {
                category.copy(expanded = expand)
            } else {
                category
            }
        }
    }

    private fun getContentState(
        monthlyExpenses: ExpensesMonthUi,
        showAllocations: Boolean,
        locale: SupportedLocales,
        balanceSheetNetUi: BalanceSheetNetUi
    ) = ExpensesMonthDetailScreenViewState.Content(
        monthExpenses = monthlyExpenses,
        showAllocations = showAllocations,
        locale = locale,
        month = month,
        year = year,
        balanceSheetNetUi = balanceSheetNetUi
    )
}