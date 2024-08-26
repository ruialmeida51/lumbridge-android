package com.eyther.lumbridge.features.expenses.viewmodel.overview

import androidx.navigation.NavHostController
import com.eyther.lumbridge.features.expenses.model.overview.ExpensesOverviewScreenViewEffect
import com.eyther.lumbridge.features.expenses.model.overview.ExpensesOverviewScreenViewState
import com.eyther.lumbridge.features.expenses.navigation.ExpensesNavigationItem
import com.eyther.lumbridge.model.expenses.ExpensesCategoryUi
import com.eyther.lumbridge.model.expenses.ExpensesDetailedUi
import com.eyther.lumbridge.model.expenses.ExpensesMonthUi
import com.eyther.lumbridge.ui.navigation.NavigationItem
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface IExpensesOverviewScreenViewModel {
    val viewState: StateFlow<ExpensesOverviewScreenViewState>
    val viewEffects: SharedFlow<ExpensesOverviewScreenViewEffect>

    /**
     * Expand the month at the given index. This will show a detailed view of the month's expenses.
     * If the month is already expanded, it will collapse it. Otherwise, it will expand it.
     * If there is another month already expanded, it will collapse it.
     *
     * @param selectedMonth The month to expand
     */
    fun expandMonth(selectedMonth: ExpensesMonthUi)

    /**
     * Expand the given category inside the selected month. This will show a detailed view of the category's expenses.
     * If the category is already expanded, it will collapse it.
     * If there is another category already expanded, it will NOT collapse it.
     *
     * @param category The category to expand
     */
    fun expandCategory(category: ExpensesCategoryUi)

    /**
     * Removes the given expense from the list of expenses.
     *
     * @param expensesMonth The expense to remove
     */
    fun onDeleteExpense(expensesMonth: ExpensesMonthUi)

    /**
     * Edit the given expense.
     *
     * @param navController The navigation controller
     * @param expensesDetailed The expense to edit
     */
    fun onEditExpense(
        navController: NavHostController,
        expensesDetailed: ExpensesDetailedUi
    )

    /**
     * Navigate to the selected expenses screen, based on the selected navigation item.
     *
     * @param navigationItem The selected navigation item
     * @param navController The navigation controller
     *
     * @see ExpensesNavigationItem
     */
    fun navigate(navigationItem: NavigationItem, navController: NavHostController)
}
