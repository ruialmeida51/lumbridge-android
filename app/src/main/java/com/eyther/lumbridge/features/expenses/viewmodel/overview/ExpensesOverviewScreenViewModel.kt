package com.eyther.lumbridge.features.expenses.viewmodel.overview

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.eyther.lumbridge.domain.model.locale.SupportedLocales
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
import com.eyther.lumbridge.model.expenses.ExpensesCategoryUi
import com.eyther.lumbridge.model.expenses.ExpensesDetailedUi
import com.eyther.lumbridge.model.expenses.ExpensesMonthUi
import com.eyther.lumbridge.model.finance.NetSalaryUi
import com.eyther.lumbridge.ui.navigation.NavigationItem
import com.eyther.lumbridge.usecase.expenses.DeleteMonthExpenseUseCase
import com.eyther.lumbridge.usecase.expenses.GetExpensesStreamUseCase
import com.eyther.lumbridge.usecase.user.profile.GetLocaleOrDefaultStream
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ExpensesOverviewScreenViewModel @Inject constructor(
    private val getExpensesStreamUseCase: GetExpensesStreamUseCase,
    private val getLocaleOrDefaultStream: GetLocaleOrDefaultStream,
    private val deleteMonthExpenseUseCase: DeleteMonthExpenseUseCase,
    private val sortByDelegate: ExpensesOverviewScreenSortByDelegate,
    private val filterDelegate: ExpensesOverviewScreenFilterDelegate
) : ViewModel(),
    IExpensesOverviewScreenViewModel,
    IExpensesOverviewScreenSortByDelegate by sortByDelegate,
    IExpensesOverviewScreenFilterDelegate by filterDelegate {

    companion object {
        /**
         * A helper class to hold the data emitted by the expenses stream, to be used in the combine operator.
         */
        private data class ExpensesStreamData(
            val netSalaryUi: NetSalaryUi?,
            val expenses: List<ExpensesMonthUi>,
            val locale: SupportedLocales,
            val sortBy: ExpensesOverviewSortBy,
            val filter: ExpensesOverviewFilter
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

    init {
        viewModelScope.launch {
            observeExpenses()
        }
    }

    private suspend fun observeExpenses() {
        combine(
            getExpensesStreamUseCase(),
            getLocaleOrDefaultStream(),
            sortBy,
            filter
        ) { (netSalaryUi, expenses), locale, sortBy, filter ->
            ExpensesStreamData(
                netSalaryUi = netSalaryUi,
                expenses = expenses,
                locale = locale,
                sortBy = sortBy,
                filter = filter
            )
        }
            .flowOn(Dispatchers.IO)
            .onEach { streamData ->
                cachedNetSalaryUi = streamData.netSalaryUi

                viewState.update {
                    if (streamData.expenses.isEmpty()) {
                        getEmptyState(
                            netSalaryUi = streamData.netSalaryUi,
                            locale = streamData.locale
                        )
                    } else {
                        getContentState(
                            monthlyExpenses = streamData.expenses,
                            netSalaryUi = streamData.netSalaryUi,
                            locale = streamData.locale,
                            sortBy = streamData.sortBy,
                            filter = streamData.filter
                        )
                    }
                }
            }
            .flowOn(Dispatchers.Default)
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
                netSalaryUi = cachedNetSalaryUi,
                locale = oldState.asContent().locale,
                sortBy = sortBy.value,
                filter = filter.value
            )
        }
    }

    override fun onDeleteExpense(expensesMonth: ExpensesMonthUi) {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e(ExpensesOverviewScreenViewModel::class.java.simpleName, "💥 Error deleting expense", throwable)

            viewModelScope.launch {
                viewEffects.emit(ExpensesOverviewScreenViewEffect.ShowError(throwable.message.orEmpty()))
            }
        }

        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            deleteMonthExpenseUseCase(expensesMonth)
        }
    }

    override fun onEditExpense(
        navController: NavHostController,
        expensesDetailed: ExpensesDetailedUi
    ) {
        navController.navigate(
            ExpensesNavigationItem.EditExpense.buildRouteWithArgs(expensesDetailed.id)
        )
    }

    override fun navigate(navigationItem: NavigationItem, navController: NavHostController) {
        navController.navigate(navigationItem.route)
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
     * @param netSalaryUi The net salary UI to set in the new state.
     */
    private fun getContentState(
        monthlyExpenses: List<ExpensesMonthUi>,
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
            val filterType = withContext(Dispatchers.Default) {
                ExpensesOverviewFilter.of(
                    ordinal = filterOrdinal,
                    startYear = startYear,
                    startMonth = startMonth,
                    endYear = endYear,
                    endMonth = endMonth
                )
            }

            withContext(Dispatchers.IO) {
                filter.value = filterType
            }
        }
    }

    override fun onSortBy(sortByOrdinal: Int) {
        viewModelScope.launch {
            val sortByType = withContext(Dispatchers.Default) {
                ExpensesOverviewSortBy.of(sortByOrdinal)
            }

            withContext(Dispatchers.IO) {
                sortBy.value = sortByType
            }
        }
    }

    override fun onClearFilter() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                filter.value = ExpensesOverviewFilter.None
            }
        }
    }

    override fun onClearSortBy() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
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
                netSalaryUi = cachedNetSalaryUi,
                locale = oldState.asContent().locale,
                sortBy = sortBy.value,
                filter = filter.value
            )
        }
    }
}
