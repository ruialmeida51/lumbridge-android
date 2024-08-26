package com.eyther.lumbridge.features.expenses.viewmodel.overview

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.eyther.lumbridge.domain.model.locale.SupportedLocales
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
                            monthlyExpensesWithSelectedMonth = expenses,
                            netSalaryUi = netSalaryUi,
                            locale = locale
                        )
                    }
                }
            }
            .onEach {
                Log.d(ExpensesOverviewScreenViewModel::class.java.simpleName, "Expenses updated ${viewState.value}")
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
                monthlyExpensesWithSelectedMonth = monthExpensesUiList,
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
                monthlyExpensesWithSelectedMonth = monthExpensesUiList,
                netSalaryUi = cachedNetSalaryUi,
                locale = cachedLocale
            )
        }
    }

    override fun onDeleteExpense(expensesMonth: ExpensesMonthUi) {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e(ExpensesOverviewScreenViewModel::class.java.simpleName, "\uD83D\uDCA5 Error deleting expense", throwable)

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
     * @param monthlyExpensesWithSelectedMonth The list of month expenses with the selected month expanded.
     * @param netSalaryUi The net salary UI to set in the new state.
     */
    private fun getContentState(
        monthlyExpensesWithSelectedMonth: List<ExpensesMonthUi>,
        netSalaryUi: NetSalaryUi?,
        locale: SupportedLocales
    ): ExpensesOverviewScreenViewState.Content {
        return if (netSalaryUi == null) {
            ExpensesOverviewScreenViewState.Content.NoFinancialProfile(
                expensesMonthUi = monthlyExpensesWithSelectedMonth,
                // 🔨 TODO Improvements: sumOf only accepts Double, so we need to convert to Double and then to Float. Not ideal.
                //      See if can find a better way to sum Floats.
                totalExpenses = monthlyExpensesWithSelectedMonth.sumOf { it.spent.toDouble() }.toFloat(),
                locale = locale
            )
        } else {
            ExpensesOverviewScreenViewState.Content.HasFinancialProfile(
                netSalaryUi = netSalaryUi,
                // 🔨 TODO Improvements: sumOf only accepts Double, so we need to convert to Double and then to Float. Not ideal.
                //      See if can find a better way to sum Floats.
                totalExpenses = monthlyExpensesWithSelectedMonth.sumOf { it.spent.toDouble() }.toFloat(),
                expensesMonthUi = monthlyExpensesWithSelectedMonth,
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
}
