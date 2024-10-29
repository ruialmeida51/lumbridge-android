package com.eyther.lumbridge.features.expenses.viewmodel.overview

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.extensions.platform.navigateWithArgs
import com.eyther.lumbridge.features.expenses.model.overview.ExpensesOverviewFilter
import com.eyther.lumbridge.features.expenses.model.overview.ExpensesOverviewScreenViewEffect
import com.eyther.lumbridge.features.expenses.model.overview.ExpensesOverviewScreenViewState
import com.eyther.lumbridge.features.expenses.model.overview.ExpensesOverviewScreenViewState.Content.HasFinancialProfile
import com.eyther.lumbridge.features.expenses.model.overview.ExpensesOverviewScreenViewState.Content.NoFinancialProfile
import com.eyther.lumbridge.features.expenses.model.overview.ExpensesOverviewSortBy
import com.eyther.lumbridge.features.expenses.navigation.ExpensesNavigationItem
import com.eyther.lumbridge.features.expenses.viewmodel.overview.delegate.ExpensesOverviewScreenFilterDelegate
import com.eyther.lumbridge.features.expenses.viewmodel.overview.delegate.ExpensesOverviewScreenSortByDelegate
import com.eyther.lumbridge.features.expenses.viewmodel.overview.delegate.IExpensesOverviewScreenFilterDelegate
import com.eyther.lumbridge.features.expenses.viewmodel.overview.delegate.IExpensesOverviewScreenSortByDelegate
import com.eyther.lumbridge.model.expenses.ExpenseUi
import com.eyther.lumbridge.model.expenses.ExpensesCategoryUi
import com.eyther.lumbridge.model.expenses.ExpensesDetailedUi
import com.eyther.lumbridge.model.expenses.ExpensesMonthUi
import com.eyther.lumbridge.model.finance.NetSalaryUi
import com.eyther.lumbridge.model.snapshotsalary.SnapshotNetSalaryUi
import com.eyther.lumbridge.shared.di.model.Schedulers
import com.eyther.lumbridge.ui.common.model.math.MathOperator
import com.eyther.lumbridge.usecase.expenses.DeleteExpensesListUseCase
import com.eyther.lumbridge.usecase.expenses.GetExpensesStreamUseCase
import com.eyther.lumbridge.usecase.finance.GetNetSalaryUseCase
import com.eyther.lumbridge.usecase.snapshotsalary.GetMostRecentSnapshotSalaryForDateUseCase
import com.eyther.lumbridge.usecase.snapshotsalary.GetSnapshotNetSalariesFlowUseCase
import com.eyther.lumbridge.usecase.user.financials.GetUserFinancialsFlow
import com.eyther.lumbridge.usecase.user.profile.GetLocaleOrDefaultStream
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Year
import javax.inject.Inject

@HiltViewModel
class ExpensesOverviewScreenViewModel @Inject constructor(
    private val getExpensesStreamUseCase: GetExpensesStreamUseCase,
    private val getLocaleOrDefaultStream: GetLocaleOrDefaultStream,
    private val deleteExpensesListUseCase: DeleteExpensesListUseCase,
    private val getNetSalaryUseCase: GetNetSalaryUseCase,
    private val getUserFinancialsStreamUseCase: GetUserFinancialsFlow,
    private val getMostRecentSnapshotSalaryForDateUseCase: GetMostRecentSnapshotSalaryForDateUseCase,
    private val getSnapshotNetSalariesFlowUseCase: GetSnapshotNetSalariesFlowUseCase,
    private val sortByDelegate: ExpensesOverviewScreenSortByDelegate,
    private val filterDelegate: ExpensesOverviewScreenFilterDelegate,
    private val schedulers: Schedulers
) : ViewModel(),
    IExpensesOverviewScreenViewModel,
    IExpensesOverviewScreenSortByDelegate by sortByDelegate,
    IExpensesOverviewScreenFilterDelegate by filterDelegate {

    companion object {
        /**
         * A helper class to hold the data emitted by the expenses stream, to be used in the combine operator.
         */
        private data class ExpensesStreamData(
            val expensesData: ExpensesData,
            val sortBy: ExpensesOverviewSortBy,
            val filter: ExpensesOverviewFilter
        )

        private data class ExpensesData(
            val expenses: List<ExpenseUi>,
            val locale: SupportedLocales,
            val netSalaryUi: NetSalaryUi?,
            val snapshotNetSalaries: List<SnapshotNetSalaryUi>
        )
    }

    override val viewState: MutableStateFlow<ExpensesOverviewScreenViewState> =
        MutableStateFlow(ExpensesOverviewScreenViewState.Loading)

    override val viewEffects: MutableSharedFlow<ExpensesOverviewScreenViewEffect> =
        MutableSharedFlow()

    private val sortBy: MutableStateFlow<ExpensesOverviewSortBy> =
        MutableStateFlow(viewState.value.getDefaultDisplaySortBy())

    private val filter: MutableStateFlow<ExpensesOverviewFilter> =
        MutableStateFlow(viewState.value.getDefaultDisplayFilter())

    private var cachedNetSalaryUi: NetSalaryUi? = null
    private var cachedSnapshotNetSalaries: List<SnapshotNetSalaryUi> = emptyList()

    init {
        viewModelScope.launch {
            observeExpenses()
        }
    }

    private fun observeExpenses() {
        val dataFlow = combine(
            getExpensesStreamUseCase(),
            getLocaleOrDefaultStream(),
            getUserFinancialsStreamUseCase(),
            getSnapshotNetSalariesFlowUseCase()
        ) { expenses, locale, userFinancials, snapshotNetSalaries ->
            ExpensesData(
                expenses = expenses,
                locale = locale,
                netSalaryUi = userFinancials?.let { getNetSalaryUseCase(it) },
                snapshotNetSalaries = snapshotNetSalaries
            )
        }

        combine(
            dataFlow,
            sortBy,
            filter
        ) { expensesData, sortBy, filter ->
            ExpensesStreamData(
                expensesData = expensesData,
                sortBy = sortBy,
                filter = filter
            )
        }
            .flowOn(schedulers.io)
            .onEach { streamData ->
                cachedNetSalaryUi = streamData.expensesData.netSalaryUi
                cachedSnapshotNetSalaries = streamData.expensesData.snapshotNetSalaries

                viewState.update {
                    if (streamData.expensesData.expenses.isEmpty()) {
                        getEmptyState(
                            netSalaryUi = streamData.expensesData.netSalaryUi,
                            locale = streamData.expensesData.locale
                        )
                    } else {
                        getContentState(
                            monthlyExpenses = streamData.expensesData.expenses.createExpensesPerMonth(),
                            snapshotNetSalaries = streamData.expensesData.snapshotNetSalaries,
                            netSalaryUi = streamData.expensesData.netSalaryUi,
                            locale = streamData.expensesData.locale,
                            sortBy = streamData.sortBy,
                            filter = streamData.filter
                        )
                    }
                }
            }
            .flowOn(schedulers.cpu)
            .launchIn(viewModelScope)
    }

    override fun expandMonth(selectedMonth: ExpensesMonthUi) {
        viewState.update { oldState ->
            // Get the list of month expenses. The state is guaranteed to be of type Content if we reach this point,
            // since we selected a month to expand.
            val monthExpensesUiList = oldState
                .asContent()
                .let { expandMonthAndCollapseOthers(it.expensesMonthUi, selectedMonth) }

            // Update the state with the new list of month expenses.
            getContentState(
                monthlyExpenses = monthExpensesUiList,
                snapshotNetSalaries = cachedSnapshotNetSalaries,
                netSalaryUi = cachedNetSalaryUi,
                locale = oldState.asContent().locale,
                sortBy = sortBy.value,
                filter = filter.value
            )
        }
    }

    override fun expandCategory(category: ExpensesCategoryUi) {
        viewState.update { oldState ->
            // Get the list of month expenses. The state is guaranteed to be of type Content if we reach this point,
            // since we selected a category to expand.
            val monthExpensesUiList = oldState
                .asContent()
                .expensesMonthUi
                .map { monthExpensesUi ->
                    monthExpensesUi.copy(
                        categoryExpenses = expandCategory(monthExpensesUi.categoryExpenses, category)
                    )
                }

            // Update the state with the new list of month expenses.
            getContentState(
                monthlyExpenses = monthExpensesUiList,
                snapshotNetSalaries = cachedSnapshotNetSalaries,
                netSalaryUi = cachedNetSalaryUi,
                locale = oldState.asContent().locale,
                sortBy = sortBy.value,
                filter = filter.value
            )
        }
    }

    override fun onDeleteExpense(expensesMonth: ExpensesMonthUi) {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e(ExpensesOverviewScreenViewModel::class.java.simpleName, "💥 Error deleting expenses", throwable)

            viewModelScope.launch {
                viewEffects.emit(ExpensesOverviewScreenViewEffect.ShowError(throwable.message.orEmpty()))
            }
        }

        viewModelScope.launch(schedulers.io + exceptionHandler) {
            val expensesIdToDelete = expensesMonth.categoryExpenses
                .flatMap { category ->
                    category.expensesDetailedUi.map { detail -> detail.id }
                }

            deleteExpensesListUseCase(expensesIdToDelete)
        }
    }

    override fun onEditExpense(
        navController: NavHostController,
        expensesDetailed: ExpensesDetailedUi
    ) {
        navController.navigateWithArgs(ExpensesNavigationItem.EditExpense, expensesDetailed.id)
    }

    /**
     * Helper function that returns the monthly expenses with a selected month expanded, maintaining whatever expanded state
     * the other months have.
     *
     * @param monthlyExpenses The list of month expenses.
     * @param selectedMonth The month to expand.
     */
    private fun expandMonthAndCollapseOthers(
        monthlyExpenses: List<ExpensesMonthUi>,
        selectedMonth: ExpensesMonthUi
    ): List<ExpensesMonthUi> {
        // Find the index of the month we want to expand.
        val selectedIndex = monthlyExpenses
            .indexOf(selectedMonth)
            .takeIf { indexOf -> indexOf >= 0 }

        if (selectedIndex == null) {
            return monthlyExpenses
        }

        return monthlyExpenses.mapIndexed { index, monthExpensesUi ->
            if (index == selectedIndex) {
                monthExpensesUi.copy(
                    expanded = !monthExpensesUi.expanded
                )
            } else {
                monthExpensesUi.copy(
                    expanded = monthExpensesUi.expanded
                )
            }
        }
    }

    /**
     * Helper function that returns the category expenses with a selected category expanded, maintaining whatever expanded state
     * the other categories have.
     *
     * @param categoryExpenses The list of category expenses.
     * @param selectedCategory The category to expand.
     */
    private fun expandCategory(
        categoryExpenses: List<ExpensesCategoryUi>,
        selectedCategory: ExpensesCategoryUi
    ): List<ExpensesCategoryUi> {
        // Find the index of the category we want to expand.
        val selectedIndex = categoryExpenses
            .indexOf(selectedCategory)
            .takeIf { indexOf -> indexOf >= 0 }

        if (selectedIndex == null) {
            return categoryExpenses
        }

        return categoryExpenses.mapIndexed { index, categoryExpensesUi ->
            if (index == selectedIndex) {
                categoryExpensesUi.copy(
                    expanded = !categoryExpensesUi.expanded
                )
            } else {
                categoryExpensesUi.copy(
                    expanded = categoryExpensesUi.expanded
                )
            }
        }
    }

    /**
     * Helper function to get the content state of the view state. It assumes that the view state is of type Content.
     *
     * It will apply the filter and sort by to the list of month expenses, by making use of two delegates:
     * - [IExpensesOverviewScreenFilterDelegate] to apply the filter
     * - [IExpensesOverviewScreenSortByDelegate] to apply the sort by
     *
     * @param monthlyExpenses The list of month expenses with the selected month expanded.
     * @param snapshotNetSalaries The list of snapshot net salaries, used to get the most recent snapshot salary.
     * @param netSalaryUi The net salary UI to set in the new state.
     * @param locale The locale to set in the new state.
     * @param sortBy The sort by to apply to the list of month expenses.
     * @param filter The filter to apply to the list of month expenses.
     */
    private fun getContentState(
        monthlyExpenses: List<ExpensesMonthUi>,
        snapshotNetSalaries: List<SnapshotNetSalaryUi>,
        netSalaryUi: NetSalaryUi?,
        locale: SupportedLocales,
        sortBy: ExpensesOverviewSortBy,
        filter: ExpensesOverviewFilter
    ): ExpensesOverviewScreenViewState.Content {
        val filteredExpenses = filterDelegate.applyFilter(monthlyExpenses, filter)
        val sortedExpenses = sortByDelegate.applySortBy(filteredExpenses, sortBy)
        val totalExpenses = sortedExpenses.sumOf { it.spent.toDouble() }.toFloat()
        val availableFilters = ExpensesOverviewFilter.get()
        val availableSorts = ExpensesOverviewSortBy.get()

        return if (netSalaryUi == null) {
            NoFinancialProfile(
                expensesMonthUi = sortedExpenses,
                totalExpenses = totalExpenses,
                locale = locale,
                availableSorts = ExpensesOverviewSortBy.get(),
                availableFilters = availableFilters,
                selectedSort = sortBy,
                selectedFilter = filter
            )
        } else {
            HasFinancialProfile(
                netSalaryUi = netSalaryUi,
                totalExpenses = totalExpenses,
                expensesMonthUi = sortedExpenses,
                locale = locale,
                availableSorts = availableSorts,
                availableFilters = availableFilters,
                selectedSort = sortBy,
                selectedFilter = filter
            )
        }
    }

    /**
     * Helper function to get the empty state of the view state.
     *
     * @param netSalaryUi The net salary UI to set in the new state.
     */
    private fun getEmptyState(
        netSalaryUi: NetSalaryUi?,
        locale: SupportedLocales
    ): ExpensesOverviewScreenViewState.Empty {
        return if (netSalaryUi == null) {
            ExpensesOverviewScreenViewState.Empty.NoFinancialProfile
        } else {
            ExpensesOverviewScreenViewState.Empty.HasFinancialProfile(
                netSalaryUi = netSalaryUi,
                locale = locale
            )
        }
    }

    override fun onFilter(filterOrdinal: Int, startYear: Int?, startMonth: Int?, endYear: Int?, endMonth: Int?) {
        viewModelScope.launch {
            val filterType = withContext(schedulers.cpu) {
                ExpensesOverviewFilter.of(
                    ordinal = filterOrdinal,
                    startYear = startYear,
                    startMonth = startMonth,
                    endYear = endYear,
                    endMonth = endMonth
                )
            }

            withContext(schedulers.io) {
                filter.value = filterType
            }
        }
    }

    override fun onSortBy(sortByOrdinal: Int) {
        viewModelScope.launch {
            val sortByType = withContext(schedulers.cpu) {
                ExpensesOverviewSortBy.of(sortByOrdinal)
            }

            withContext(schedulers.io) {
                sortBy.value = sortByType
            }
        }
    }

    override fun onClearFilter() {
        viewModelScope.launch {
            withContext(schedulers.io) {
                filter.value = ExpensesOverviewFilter.None
            }
        }
    }

    override fun onClearSortBy() {
        viewModelScope.launch {
            withContext(schedulers.io) {
                sortBy.value = ExpensesOverviewSortBy.DateDescending
            }
        }
    }

    override fun collapseAll() {
        viewState.update { oldState ->
            val monthlyExpenses = oldState
                .asContent()
                .expensesMonthUi
                .map { monthExpensesUi ->
                    monthExpensesUi.copy(
                        expanded = false,
                        categoryExpenses = monthExpensesUi.categoryExpenses.map { categoryExpensesUi ->
                            categoryExpensesUi.copy(expanded = false)
                        }
                    )
                }

            getContentState(
                monthlyExpenses = monthlyExpenses,
                snapshotNetSalaries = cachedSnapshotNetSalaries,
                netSalaryUi = cachedNetSalaryUi,
                locale = oldState.asContent().locale,
                sortBy = sortBy.value,
                filter = filter.value
            )
        }
    }

    override fun expandAll() {
        viewState.update { oldState ->
            val monthlyExpenses = oldState
                .asContent()
                .expensesMonthUi
                .map { monthExpensesUi ->
                    monthExpensesUi.copy(
                        expanded = true,
                        categoryExpenses = monthExpensesUi.categoryExpenses.map { categoryExpensesUi ->
                            categoryExpensesUi.copy(expanded = true)
                        }
                    )
                }

            getContentState(
                monthlyExpenses = monthlyExpenses,
                snapshotNetSalaries = cachedSnapshotNetSalaries,
                netSalaryUi = cachedNetSalaryUi,
                locale = oldState.asContent().locale,
                sortBy = sortBy.value,
                filter = filter.value
            )
        }
    }

    private suspend fun List<ExpenseUi>.createExpensesPerMonth(): List<ExpensesMonthUi> {
        return groupBy { it.date.year to it.date.month }
            .map { (yearMonth, expenses) ->
                val spent = expenses
                    .filter { it.categoryType.operator == MathOperator.SUBTRACTION }
                    .sumOf { it.expenseAmount.toDouble() }.toFloat()

                val gained = expenses
                    .filter { it.categoryType.operator == MathOperator.ADDITION }
                    .sumOf { it.expenseAmount.toDouble() }.toFloat()

                val snapshotSalary = getMostRecentSnapshotSalaryForDateUseCase(
                    snapshotNetSalaries = cachedSnapshotNetSalaries,
                    year = yearMonth.first,
                    month = yearMonth.second.value
                )

                ExpensesMonthUi(
                    month = yearMonth.second,
                    year = Year.of(yearMonth.first),
                    spent = spent,
                    expanded = false,
                    remainder = snapshotSalary - spent + gained,
                    snapshotMonthlyNetSalary = snapshotSalary,
                    categoryExpenses = expenses.toCategoryExpenses()
                )
            }
    }

    private fun List<ExpenseUi>.toCategoryExpenses() =
        groupBy { it.categoryType }
            .map { (type, expenses) ->
                val categoryExpenseSpent = expenses.sumOf { it.expenseAmount.toDouble() }.toFloat()

                ExpensesCategoryUi(
                    categoryType = type,
                    spent = categoryExpenseSpent,
                    expensesDetailedUi = expenses.toDetailedExpense()
                )
            }

    private fun List<ExpenseUi>.toDetailedExpense() = map {
        ExpensesDetailedUi(
            id = it.id,
            date = it.date,
            expenseAmount = it.expenseAmount,
            expenseName = it.expenseName
        )
    }

}
