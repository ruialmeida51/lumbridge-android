package com.eyther.lumbridge.features.expenses.viewmodel.overview

import androidx.navigation.NavHostController
import com.eyther.lumbridge.features.expenses.model.overview.ExpensesOverviewFilter
import com.eyther.lumbridge.features.expenses.model.overview.ExpensesOverviewScreenViewEffect
import com.eyther.lumbridge.features.expenses.model.overview.ExpensesOverviewScreenViewState
import com.eyther.lumbridge.features.expenses.model.overview.ExpensesOverviewSortBy
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
     * Applies the given sort by to the expenses overview.
     *
     * @param sortByOrdinal The sort by to apply
     *
     * @see ExpensesOverviewSortBy
     */
    fun onSortBy(sortByOrdinal: Int)

    /**
     * Applies the given filter to the expenses overview.
     *
     * @param filterOrdinal The filter to apply
     * @param startYear The start year for the filter, if applicable
     * @param startMonth The start month for the filter, if applicable
     * @param endYear The end year for the filter, if applicable
     * @param endMonth The end month for the filter, if applicable
     *
     * @see ExpensesOverviewFilter
     */
    fun onFilter(
        filterOrdinal: Int,
        startYear: Int? = null,
        startMonth: Int? = null,
        endYear: Int? = null,
        endMonth: Int? = null
    )

    /**
     * Clears the applied filter - restoring it to the default value.
     */
    fun onClearFilter()

    /**
     * Clears the applied sort by - restoring it to the default value.
     */
    fun onClearSortBy()

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
