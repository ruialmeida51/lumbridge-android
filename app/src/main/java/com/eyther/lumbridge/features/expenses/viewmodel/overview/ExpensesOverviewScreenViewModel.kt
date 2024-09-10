package com.eyther.lumbridge.features.expenses.viewmodel.overview

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.domain.time.DAYS_IN_MONTH
import com.eyther.lumbridge.domain.time.toLocalDate
import com.eyther.lumbridge.features.expenses.model.overview.ExpensesOverviewScreenViewEffect
import com.eyther.lumbridge.features.expenses.model.overview.ExpensesOverviewScreenViewState
import com.eyther.lumbridge.features.expenses.navigation.ExpensesNavigationItem
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
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ExpensesOverviewScreenViewModel @Inject constructor(
    private val getExpensesStreamUseCase: GetExpensesStreamUseCase,
    private val getLocaleOrDefaultStream: GetLocaleOrDefaultStream,
    private val deleteMonthExpenseUseCase: DeleteMonthExpenseUseCase
) : ViewModel(),
    IExpensesOverviewScreenViewModel {

    override val viewState: MutableStateFlow<ExpensesOverviewScreenViewState> =
        MutableStateFlow(ExpensesOverviewScreenViewState.Loading)

    override val viewEffects: MutableSharedFlow<ExpensesOverviewScreenViewEffect> =
        MutableSharedFlow()

    private var cachedNetSalaryUi: NetSalaryUi? = null
    private lateinit var cachedLocale: SupportedLocales

    init {
        viewModelScope.launch {
            observeExpenses()
        }
    }

    private suspend fun observeExpenses() {
        combine(
            getExpensesStreamUseCase(),
            getLocaleOrDefaultStream()
        ) { (netSalaryUi, expenses), locale -> Triple(netSalaryUi, expenses, locale) }
            .flowOn(Dispatchers.IO)
            .onEach { (netSalaryUi, expenses, locale) ->
                cachedNetSalaryUi = netSalaryUi
                cachedLocale = locale

                viewState.update {
                    if (expenses.isEmpty()) {
                        getEmptyState(
                            netSalaryUi = netSalaryUi,
                            locale = locale
                        )
                    } else {
                        getContentState(
                            monthlyExpenses = expenses,
                            netSalaryUi = netSalaryUi,
                            locale = locale
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
                locale = cachedLocale
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
                locale = cachedLocale
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
     * Helper function that returns the monthly expenses with a selected month expanded, collapsing
     * the rest of the months.
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

        // Check if the selected month is already expanded.
        val isAlreadyExpanded = monthlyExpenses
            .find { it == selectedMonth }
            ?.expanded

        // Create a new list of month expenses where the selected month is expanded and the rest are not.
        // If the selected month is already expanded, collapse it.
        return monthlyExpenses.mapIndexed { index, monthExpensesUi ->
            monthExpensesUi.copy(
                expanded = selectedIndex == index && isAlreadyExpanded != true
            )
        }
    }

    /**
     * Helper function that returns the category expenses with a selected category expanded, collapsing
     * the rest of the categories.
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

        // Create a new list of category expenses where the selected category is expanded and the rest are not.
        // If the selected category is already expanded, collapse it.
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
     * @param monthlyExpenses The list of month expenses with the selected month expanded.
     * @param netSalaryUi The net salary UI to set in the new state.
     */
    private fun getContentState(
        monthlyExpenses: List<ExpensesMonthUi>,
        netSalaryUi: NetSalaryUi?,
        locale: SupportedLocales
    ): ExpensesOverviewScreenViewState.Content {
        return if (netSalaryUi == null) {
            ExpensesOverviewScreenViewState.Content.NoFinancialProfile(
                expensesMonthUi = monthlyExpenses,
                totalExpenses = monthlyExpenses.sumOf { it.spent.toDouble() }.toFloat(),
                locale = locale
            )
        } else {
            ExpensesOverviewScreenViewState.Content.HasFinancialProfile(
                netSalaryUi = netSalaryUi,
                totalExpenses = monthlyExpenses.sumOf { it.spent.toDouble() }.toFloat(),
                expensesMonthUi = monthlyExpenses,
                locale = locale
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

    // TODO Improvements: Add a filter function to filter the expenses by a date range.

    /**
     * Helper function to filter the expenses by a date range.
     *
     * The way this works is as follows:
     * - If the start date is null and the end date is not, we filter the expenses by the end date.
     * - If the start date is not null and the end date is, we filter the expenses by the start date.
     * - If both the start date and the end date are not null, we filter the expenses by the range between the start and end dates.
     * - If both the start date and the end date are null, we return the expenses as is.
     *
     * @param expenses The list of expenses to filter.
     * @param startDate The start date of the range.
     * @param endDate The end date of the range.
     *
     * @return The list of expenses filtered by the date range.
     */
    private fun List<ExpensesMonthUi>.filterExpensesByDateRange(
        startDate: LocalDate?,
        endDate: LocalDate?
    ): List<ExpensesMonthUi> = when {
        startDate == null && endDate != null -> {
            filter { expense ->
                val endMonth = endDate.monthValue
                val endYear = endDate.year

                expense.year.value == endYear && expense.month.value <= endMonth
            }
        }

        startDate != null && endDate == null -> {
            filter { expense ->
                val startMonth = startDate.monthValue
                val startYear = startDate.year

                expense.year.value == startYear && expense.month.value >= startMonth
            }
        }

        startDate != null && endDate != null -> {
            filter { expense ->
                val startMonth = startDate.monthValue
                val startYear = startDate.year
                val endMonth = endDate.monthValue
                val endYear = endDate.year

                expense.year.value in startYear..endYear &&
                    expense.month.value in startMonth..endMonth
            }
        }

        else -> this
    }

    /**
     * Gets the selectable dates for the date range picker.
     *
     * The way this works is as follows:
     * - If the view state is content, we get the minimum start date and the maximum end date from the expenses.
     *
     * @return The selectable dates for the date range picker.
     */
    private fun getSelectableDates(): ClosedRange<LocalDate>? {
        if (!viewState.value.isContent()) return null

        val viewStateAsContent = viewState.value.asContent()

        val dates = viewStateAsContent
            .expensesMonthUi
            .map { it.year to it.month }

        val minStartDate = dates.minOf { it.toLocalDate().withDayOfMonth(DAYS_IN_MONTH) }
        val maxEndDate = dates.maxOf { it.toLocalDate().withDayOfMonth(DAYS_IN_MONTH) }

        return minStartDate..maxEndDate
    }
}
