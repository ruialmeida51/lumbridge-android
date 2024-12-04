package com.eyther.lumbridge.features.expenses.viewmodel.overview

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.features.expenses.model.overview.ExpensesOverviewFilter
import com.eyther.lumbridge.features.expenses.model.overview.ExpensesOverviewScreenViewEffect
import com.eyther.lumbridge.features.expenses.model.overview.ExpensesOverviewScreenViewState
import com.eyther.lumbridge.features.expenses.model.overview.ExpensesOverviewScreenViewState.Content.HasFinancialProfile
import com.eyther.lumbridge.features.expenses.model.overview.ExpensesOverviewScreenViewState.Content.NoFinancialProfile
import com.eyther.lumbridge.features.expenses.model.overview.ExpensesOverviewSortBy
import com.eyther.lumbridge.features.expenses.viewmodel.overview.delegate.ExpensesOverviewScreenFilterDelegate
import com.eyther.lumbridge.features.expenses.viewmodel.overview.delegate.ExpensesOverviewScreenSortByDelegate
import com.eyther.lumbridge.features.expenses.viewmodel.overview.delegate.IExpensesOverviewScreenFilterDelegate
import com.eyther.lumbridge.features.expenses.viewmodel.overview.delegate.IExpensesOverviewScreenSortByDelegate
import com.eyther.lumbridge.model.expenses.ExpenseUi
import com.eyther.lumbridge.model.expenses.ExpensesCategoryUi
import com.eyther.lumbridge.model.expenses.ExpensesMonthUi
import com.eyther.lumbridge.model.finance.NetSalaryUi
import com.eyther.lumbridge.model.snapshotsalary.SnapshotNetSalaryUi
import com.eyther.lumbridge.shared.di.model.Schedulers
import com.eyther.lumbridge.usecase.expenses.DeleteExpensesListUseCase
import com.eyther.lumbridge.usecase.expenses.GetExpensesStreamUseCase
import com.eyther.lumbridge.usecase.expenses.GroupExpensesUseCase
import com.eyther.lumbridge.usecase.finance.GetNetSalaryUseCase
import com.eyther.lumbridge.usecase.preferences.GetPreferencesFlow
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
import javax.inject.Inject

@HiltViewModel
class ExpensesOverviewScreenViewModel @Inject constructor(
    private val getExpensesStreamUseCase: GetExpensesStreamUseCase,
    private val getLocaleOrDefaultStream: GetLocaleOrDefaultStream,
    private val deleteExpensesListUseCase: DeleteExpensesListUseCase,
    private val getNetSalaryUseCase: GetNetSalaryUseCase,
    private val getUserFinancialsStreamUseCase: GetUserFinancialsFlow,
    private val groupExpensesUseCase: GroupExpensesUseCase,
    private val getPreferencesFlow: GetPreferencesFlow,
    private val getSnapshotNetSalariesFlowUseCase: GetSnapshotNetSalariesFlowUseCase,
    private val sortByDelegate: ExpensesOverviewScreenSortByDelegate,
    private val filterDelegate: ExpensesOverviewScreenFilterDelegate,
    private val schedulers: Schedulers
) : ViewModel(),
    IExpensesOverviewScreenViewModel,
    IExpensesOverviewScreenSortByDelegate by sortByDelegate,
    IExpensesOverviewScreenFilterDelegate by filterDelegate {

    companion object {
        private const val TAG = "ExpensesOverviewScreenViewModel"

        /**
         * A helper class to hold the data emitted by the expenses stream, to be used in the combine operator.
         */
        private data class ExpensesStreamData(
            val expensesData: ExpensesData,
            val sortBy: ExpensesOverviewSortBy,
            val filter: ExpensesOverviewFilter,
            val showAllocationsOnExpenses: Boolean
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
            filter,
            getPreferencesFlow()
        ) { expensesData, sortBy, filter, preferences ->
            ExpensesStreamData(
                expensesData = expensesData,
                sortBy = sortBy,
                filter = filter,
                showAllocationsOnExpenses = preferences?.showAllocationsOnExpenses == true
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
                            monthlyExpenses = groupExpensesUseCase(
                                expenses = streamData.expensesData.expenses,
                                snapshotNetSalaries = streamData.expensesData.snapshotNetSalaries,
                                showAllocationsOnExpenses = streamData.showAllocationsOnExpenses
                            ),
                            netSalaryUi = streamData.expensesData.netSalaryUi,
                            locale = streamData.expensesData.locale,
                            sortBy = streamData.sortBy,
                            filter = streamData.filter,
                            showAllocationsOnExpenses = streamData.showAllocationsOnExpenses
                        )
                    }
                }
            }
            .flowOn(schedulers.cpu)
            .launchIn(viewModelScope)
    }

    override fun selectMonth(selectedMonth: ExpensesMonthUi) {
        viewModelScope.launch {
            viewEffects.emit(
                ExpensesOverviewScreenViewEffect.NavigateToMonthDetail(
                    year = selectedMonth.year.value,
                    month = selectedMonth.month.value
                )
            )
        }
    }

    override fun onDeleteExpense(expensesMonth: ExpensesMonthUi) {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e(TAG, "💥 Error deleting expenses", throwable)

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

    /**
     * Helper function to get the content state of the view state. It assumes that the view state is of type Content.
     *
     * It will apply the filter and sort by to the list of month expenses, by making use of two delegates:
     * - [IExpensesOverviewScreenFilterDelegate] to apply the filter
     * - [IExpensesOverviewScreenSortByDelegate] to apply the sort by
     *
     * @param monthlyExpenses The list of month expenses with the selected month expanded.
     * @param netSalaryUi The net salary UI to set in the new state.
     * @param locale The locale to set in the new state.
     * @param sortBy The sort by to apply to the list of month expenses.
     * @param filter The filter to apply to the list of month expenses.
     */
    private fun getContentState(
        monthlyExpenses: List<ExpensesMonthUi>,
        netSalaryUi: NetSalaryUi?,
        showAllocationsOnExpenses: Boolean,
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
                showAllocations = showAllocationsOnExpenses,
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
                showAllocations = showAllocationsOnExpenses,
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

}
