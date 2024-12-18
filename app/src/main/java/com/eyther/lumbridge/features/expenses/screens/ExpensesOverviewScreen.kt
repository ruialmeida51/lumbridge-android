@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.eyther.lumbridge.features.expenses.screens

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.eyther.lumbridge.R
import com.eyther.lumbridge.extensions.kotlin.capitalise
import com.eyther.lumbridge.extensions.kotlin.forceTwoDecimalsPlaces
import com.eyther.lumbridge.extensions.platform.navigateTo
import com.eyther.lumbridge.extensions.platform.navigateToWithArgs
import com.eyther.lumbridge.features.expenses.model.overview.ExpensesOverviewFilter
import com.eyther.lumbridge.features.expenses.model.overview.ExpensesOverviewFilter.Companion.DisplayFilter
import com.eyther.lumbridge.features.expenses.model.overview.ExpensesOverviewFilter.Companion.FILTER_DATE_RANGE_ORDINAL
import com.eyther.lumbridge.features.expenses.model.overview.ExpensesOverviewFilter.Companion.FILTER_NONE_ORDINAL
import com.eyther.lumbridge.features.expenses.model.overview.ExpensesOverviewFilter.Companion.FILTER_START_FROM_ORDINAL
import com.eyther.lumbridge.features.expenses.model.overview.ExpensesOverviewFilter.Companion.FILTER_UP_TO_ORDINAL
import com.eyther.lumbridge.features.expenses.model.overview.ExpensesOverviewFilter.Companion.toDisplayFilter
import com.eyther.lumbridge.features.expenses.model.overview.ExpensesOverviewScreenViewEffect
import com.eyther.lumbridge.features.expenses.model.overview.ExpensesOverviewScreenViewState
import com.eyther.lumbridge.features.expenses.model.overview.ExpensesOverviewScreenViewState.Content
import com.eyther.lumbridge.features.expenses.model.overview.ExpensesOverviewScreenViewState.Empty
import com.eyther.lumbridge.features.expenses.model.overview.ExpensesOverviewScreenViewState.Loading
import com.eyther.lumbridge.features.expenses.model.overview.ExpensesOverviewSortBy
import com.eyther.lumbridge.features.expenses.model.overview.ExpensesOverviewSortBy.Companion.DisplaySortBy
import com.eyther.lumbridge.features.expenses.model.overview.ExpensesOverviewSortBy.Companion.toDisplaySortBy
import com.eyther.lumbridge.features.expenses.navigation.ExpensesNavigationItem
import com.eyther.lumbridge.features.expenses.screens.components.MonthlyAllocationGraph
import com.eyther.lumbridge.features.expenses.viewmodel.overview.ExpensesOverviewScreenViewModel
import com.eyther.lumbridge.features.expenses.viewmodel.overview.IExpensesOverviewScreenViewModel
import com.eyther.lumbridge.model.expenses.ExpensesMonthUi
import com.eyther.lumbridge.model.finance.NetSalaryUi
import com.eyther.lumbridge.shared.time.extensions.toLocalDate
import com.eyther.lumbridge.shared.time.extensions.toMonthYearDateString
import com.eyther.lumbridge.ui.common.composables.components.buttons.LumbridgeButton
import com.eyther.lumbridge.ui.common.composables.components.card.ColumnCardWrapper
import com.eyther.lumbridge.ui.common.composables.components.datepicker.LumbridgeYearMonthPicker
import com.eyther.lumbridge.ui.common.composables.components.datepicker.LumbridgeYearMonthRangePicker
import com.eyther.lumbridge.ui.common.composables.components.defaults.EmptyComponentWithButton
import com.eyther.lumbridge.ui.common.composables.components.loading.LoadingIndicator
import com.eyther.lumbridge.ui.common.composables.components.setting.MovementSetting
import com.eyther.lumbridge.ui.common.composables.components.setting.SimpleSetting
import com.eyther.lumbridge.ui.common.composables.components.text.TabbedDataOverview
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.LumbridgeTopAppBar
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.TopAppBarVariation
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.HalfPadding
import com.eyther.lumbridge.ui.theme.QuarterPadding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun ExpensesOverviewScreen(
    navController: NavHostController,
    @StringRes label: Int,
    viewModel: IExpensesOverviewScreenViewModel = hiltViewModel<ExpensesOverviewScreenViewModel>()
) {
    val state = viewModel.viewState.collectAsStateWithLifecycle().value
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    val snackbarHostState = remember { SnackbarHostState() }
    val monthToDelete = remember { mutableStateOf<ExpensesMonthUi?>(null) }
    val openSortByDialog = remember { mutableStateOf(false) }
    val openFilterDialog = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.viewEffects
                .onEach { viewEffects ->
                    when (viewEffects) {
                        is ExpensesOverviewScreenViewEffect.ShowError -> {
                            snackbarHostState.showSnackbar(
                                message = viewEffects.message,
                                duration = SnackbarDuration.Short
                            )
                        }

                        is ExpensesOverviewScreenViewEffect.NavigateToMonthDetail -> {
                            navController.navigateToWithArgs(
                                navigationItem = ExpensesNavigationItem.ExpensesMonthDetail,
                                args = arrayOf(viewEffects.month, viewEffects.year)
                            )
                        }
                    }
                }
                .collect()
        }
    }

    Scaffold(
        topBar = {
            LumbridgeTopAppBar(
                topAppBarVariation = TopAppBarVariation.Title(
                    title = stringResource(id = label)
                ),
                showIcons = state.hasExpensesOrFilterApplied(),
                actions = {
                    Icon(
                        modifier = Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(bounded = false)
                        ) {
                            openSortByDialog.value = true
                        },
                        painter = painterResource(R.drawable.ic_sort_by),
                        contentDescription = stringResource(id = R.string.sort_by),
                    )

                    Icon(
                        modifier = Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(bounded = false)
                        ) {
                            openFilterDialog.value = true
                        },
                        painter = painterResource(R.drawable.ic_filter),
                        contentDescription = stringResource(id = R.string.filter)
                    )
                }
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .imePadding()
                .padding(top = DefaultPadding)
                .then(
                    if (monthToDelete.value != null) Modifier.blur(5.dp) else Modifier
                )
        ) {
            when (state) {
                is Loading -> {
                    LoadingIndicator()
                }

                is Content -> {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Content(
                            state = state,
                            navController = navController,
                            monthToDelete = monthToDelete,
                            openSortByDialog = openSortByDialog,
                            openFilterDialog = openFilterDialog,
                            onSelectMonth = viewModel::selectMonth,
                            onDeleteExpense = viewModel::onDeleteExpense,
                            onSortBySelected = viewModel::onSortBy,
                            onFilterSelected = viewModel::onFilter,
                            onClearFilter = viewModel::onClearFilter,
                            onClearSortBy = viewModel::onClearSortBy
                        )

                        AddFab(
                            modifier = Modifier.align(Alignment.BottomEnd),
                            navController = navController
                        )
                    }
                }

                is Empty -> {
                    Empty(
                        state = state,
                        navController = navController,
                        onClearFilter = viewModel::onClearFilter,
                        onClearSortBy = viewModel::onClearSortBy,
                    )
                }
            }
        }
    }
}

@Composable
private fun Content(
    state: Content,
    navController: NavHostController,
    monthToDelete: MutableState<ExpensesMonthUi?>,
    openSortByDialog: MutableState<Boolean>,
    openFilterDialog: MutableState<Boolean>,
    onSelectMonth: (ExpensesMonthUi) -> Unit,
    onDeleteExpense: (ExpensesMonthUi) -> Unit,
    onSortBySelected: (Int) -> Unit,
    onFilterSelected: (ordinal: Int, startYear: Int?, startMonth: Int?, endYear: Int?, endMonth: Int?) -> Unit,
    onClearFilter: () -> Unit,
    onClearSortBy: () -> Unit
) {
    if (!state.hasExpenses()) {
        FinancialProfile(
            state = state,
            onClearFilter = onClearFilter,
            onClearSortBy = onClearSortBy,
            navController = navController
        )

        Spacer(modifier = Modifier.height(HalfPadding))

        return
    }

    LazyColumn {
        itemsIndexed(
            items = state.expensesMonthUi
        ) { index, monthExpensesUi ->
            if (index == 0) {
                FinancialProfile(
                    state = state,
                    onClearFilter = onClearFilter,
                    onClearSortBy = onClearSortBy,
                    navController = navController
                )
            }

            Spacer(modifier = Modifier.height(HalfPadding))

            MonthCard(
                expensesMonthUi = monthExpensesUi,
                onSelectMonth = onSelectMonth,
                showAllocationForExpenses = state.showAllocations,
                monthToDelete = monthToDelete,
                currencySymbol = state.locale.getCurrencySymbol()
            )

            if (index == state.expensesMonthUi.lastIndex) {
                Spacer(modifier = Modifier.height(DefaultPadding * 2 + 56.0.dp)) // 56dp is the height of the FAB
            }
        }
    }

    ShowConfirmationDialog(
        expensesMonthUi = monthToDelete.value,
        monthToDelete = monthToDelete,
        onDeleteExpense = onDeleteExpense
    )

    ShowSortByDialog(
        availableSorts = state.availableSorts,
        selectedSort = state.selectedSort,
        openSortByDialog = openSortByDialog,
        onSortBySelected = onSortBySelected
    )

    ShowFilterDialog(
        availableFilters = state.availableFilters,
        selectedFilter = state.selectedFilter,
        openFilterDialog = openFilterDialog,
        onFilterSelected = onFilterSelected
    )
}

@Composable
private fun Empty(
    state: Empty,
    navController: NavHostController,
    onClearFilter: () -> Unit,
    onClearSortBy: () -> Unit
) {
    FinancialProfile(
        state = state,
        onClearFilter = onClearFilter,
        onClearSortBy = onClearSortBy,
        navController = navController
    )

    Spacer(modifier = Modifier.height(HalfPadding))

    NoExpenses(
        navController = navController
    )
}

@Composable
private fun FinancialProfile(
    state: ExpensesOverviewScreenViewState,
    navController: NavHostController,
    onClearFilter: () -> Unit,
    onClearSortBy: () -> Unit
) {
    when (state) {
        is Empty.NoFinancialProfile -> {
            EmptyAndNoFinancialProfile(
                navController = navController
            )
        }

        is Empty.HasFinancialProfile -> {
            HasFinancialProfile(
                netSalaryUi = state.netSalaryUi,
                totalSpent = null,
                sortBy = state.getDefaultDisplaySortBy(),
                filter = state.getDefaultDisplayFilter(),
                showFilterAndSort = state.hasExpensesOrFilterApplied(),
                defaultFilter = state.getDefaultDisplayFilter(),
                defaultSortBy = state.getDefaultDisplaySortBy(),
                navController = navController,
                onClearFilter = onClearFilter,
                onClearSortBy = onClearSortBy,
                currencySymbol = state.locale.getCurrencySymbol()
            )
        }

        is Content.NoFinancialProfile -> {
            NoFinancialProfile(
                totalSpent = state.totalExpenses,
                sortBy = state.selectedSort,
                filter = state.selectedFilter,
                showFilterAndSort = state.hasExpensesOrFilterApplied(),
                defaultFilter = state.getDefaultDisplayFilter(),
                defaultSortBy = state.getDefaultDisplaySortBy(),
                navController = navController,
                onClearFilter = onClearFilter,
                onClearSortBy = onClearSortBy,
                currencySymbol = state.locale.getCurrencySymbol()

            )
        }

        is Content.HasFinancialProfile -> {
            HasFinancialProfile(
                netSalaryUi = state.netSalaryUi,
                totalSpent = state.totalExpenses,
                sortBy = state.selectedSort,
                filter = state.selectedFilter,
                showFilterAndSort = state.hasExpensesOrFilterApplied(),
                defaultFilter = state.getDefaultDisplayFilter(),
                defaultSortBy = state.getDefaultDisplaySortBy(),
                navController = navController,
                onClearFilter = onClearFilter,
                onClearSortBy = onClearSortBy,
                currencySymbol = state.locale.getCurrencySymbol()
            )
        }

        is Loading -> Unit
    }
}

@Composable
private fun HasFinancialProfile(
    netSalaryUi: NetSalaryUi,
    totalSpent: Float?,
    defaultSortBy: ExpensesOverviewSortBy,
    defaultFilter: ExpensesOverviewFilter,
    sortBy: ExpensesOverviewSortBy,
    filter: ExpensesOverviewFilter,
    currencySymbol: String,
    showFilterAndSort: Boolean,
    navController: NavHostController,
    onClearFilter: () -> Unit,
    onClearSortBy: () -> Unit
) {
    ColumnCardWrapper(
        modifier = Modifier.animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(HalfPadding)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(bottom = QuarterPadding),
                text = stringResource(id = R.string.expenses_overview_title),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.tertiary
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                modifier = Modifier
                    .size(20.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(bounded = false)
                    ) { navController.navigateTo(ExpensesNavigationItem.EditFinancialProfile) },
                painter = painterResource(id = R.drawable.ic_edit),
                contentDescription = stringResource(id = R.string.edit)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(HalfPadding)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_dollar),
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )

            TabbedDataOverview(
                label = stringResource(id = R.string.net_monthly),
                text = "${netSalaryUi.monthlyNetSalary.forceTwoDecimalsPlaces()}$currencySymbol"
            )
        }

        AnimatedVisibility(totalSpent != null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(HalfPadding)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_chart),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )

                TabbedDataOverview(
                    label = stringResource(id = R.string.total),
                    text = "${totalSpent?.forceTwoDecimalsPlaces()}$currencySymbol"
                )
            }
        }

        AnimatedVisibility(showFilterAndSort) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(HalfPadding)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_sort_by),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )

                TabbedDataOverview(
                    label = stringResource(id = R.string.sorting_by),
                    text = stringResource(sortBy.toDisplaySortBy().nameRes),
                    icon = if (sortBy != defaultSortBy) {
                        {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_clear),
                                contentDescription = stringResource(id = R.string.clear),
                                modifier = Modifier
                                    .size(16.dp)
                                    .clickable { onClearSortBy() }
                            )
                        }
                    } else {
                        null
                    }
                )
            }
        }

        AnimatedVisibility(showFilterAndSort) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(HalfPadding)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_filter),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )

                TabbedDataOverview(
                    label = stringResource(id = R.string.filter_by),
                    text = getFilterText(filter),
                    icon = if (filter != defaultFilter) {
                        {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_clear),
                                contentDescription = stringResource(id = R.string.clear),
                                modifier = Modifier
                                    .size(16.dp)
                                    .clickable { onClearFilter() }
                            )
                        }
                    } else {
                        null
                    }
                )
            }
        }
    }
}

@Composable
private fun MonthCard(
    modifier: Modifier = Modifier,
    expensesMonthUi: ExpensesMonthUi,
    showAllocationForExpenses: Boolean,
    currencySymbol: String,
    monthToDelete: MutableState<ExpensesMonthUi?>,
    onSelectMonth: (ExpensesMonthUi) -> Unit
) {
    ColumnCardWrapper(
        modifier = modifier,
        onClick = { onSelectMonth(expensesMonthUi) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    modifier = Modifier
                        .size(20.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(bounded = false)
                        ) {
                            monthToDelete.value = expensesMonthUi
                        },
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = stringResource(R.string.delete)
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier.padding(bottom = HalfPadding),
                    text = expensesMonthUi.getDateWithLocale(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.tertiary
                )

                if (showAllocationForExpenses) {
                    MonthlyAllocationGraph(
                        expensesMonthUi = expensesMonthUi,
                        currencySymbol = currencySymbol
                    )
                }

                if (expensesMonthUi.snapshotAllocations.isNotEmpty() && showAllocationForExpenses) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = DefaultPadding)
                    )
                }

                SpentVersusRemainder(
                    expensesMonthUi = expensesMonthUi,
                    currencySymbol = currencySymbol
                )

                MovementSetting(
                    modifier = Modifier.padding(top = DefaultPadding),
                    label = stringResource(id = R.string.breakdown_tap_to_view_more)
                )
            }
        }
    }
}

@Composable
private fun ColumnScope.SpentVersusRemainder(
    expensesMonthUi: ExpensesMonthUi,
    currencySymbol: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(HalfPadding)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_payments),
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )

            Spacer(modifier = Modifier.width(HalfPadding))

            TabbedDataOverview(
                label = stringResource(id = R.string.spent),
                text = "${expensesMonthUi.spent.forceTwoDecimalsPlaces()}$currencySymbol"
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_savings),
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )

            Spacer(modifier = Modifier.width(HalfPadding))

            TabbedDataOverview(
                label = stringResource(id = R.string.remainder),
                text = "${expensesMonthUi.remainder.forceTwoDecimalsPlaces()}$currencySymbol"
            )
        }
    }
}

@Composable
private fun NoFinancialProfile(
    navController: NavHostController,
    totalSpent: Float?,
    defaultSortBy: ExpensesOverviewSortBy,
    defaultFilter: ExpensesOverviewFilter,
    sortBy: ExpensesOverviewSortBy,
    filter: ExpensesOverviewFilter,
    currencySymbol: String,
    showFilterAndSort: Boolean,
    onClearFilter: () -> Unit,
    onClearSortBy: () -> Unit
) {
    ColumnCardWrapper(
        modifier = Modifier.animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(HalfPadding)
    ) {
        AnimatedVisibility(totalSpent != null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(HalfPadding)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_chart),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )

                TabbedDataOverview(
                    label = stringResource(id = R.string.total),
                    text = "${totalSpent?.forceTwoDecimalsPlaces()}$currencySymbol"
                )
            }
        }

        AnimatedVisibility(showFilterAndSort) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(HalfPadding)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_sort_by),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )

                TabbedDataOverview(
                    label = stringResource(id = R.string.sorting_by),
                    text = stringResource(sortBy.toDisplaySortBy().nameRes),
                    icon = if (sortBy != defaultSortBy) {
                        {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_clear),
                                contentDescription = stringResource(id = R.string.clear),
                                modifier = Modifier
                                    .size(16.dp)
                                    .clickable { onClearSortBy() }
                            )
                        }
                    } else {
                        null
                    }
                )
            }
        }

        AnimatedVisibility(showFilterAndSort) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(HalfPadding)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_filter),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )

                TabbedDataOverview(
                    label = stringResource(id = R.string.filter_by),
                    text = getFilterText(filter),
                    icon = if (filter != defaultFilter) {
                        {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_clear),
                                contentDescription = stringResource(id = R.string.clear),
                                modifier = Modifier
                                    .size(16.dp)
                                    .clickable { onClearFilter() }
                            )
                        }
                    } else {
                        null
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(DefaultPadding))

        EmptyComponentWithButton(
            text = stringResource(id = R.string.expenses_overview_no_financial_profile),
            buttonText = stringResource(id = R.string.financial_overview_create_profile),
            onButtonClick = {
                navController.navigateTo(ExpensesNavigationItem.EditFinancialProfile)
            }
        )
    }
}

@Composable
private fun EmptyAndNoFinancialProfile(
    navController: NavHostController
) {
    ColumnCardWrapper {
        EmptyComponentWithButton(
            text = stringResource(id = R.string.expenses_overview_no_financial_profile),
            buttonText = stringResource(id = R.string.financial_overview_create_profile),
            onButtonClick = {
                navController.navigateTo(ExpensesNavigationItem.EditFinancialProfile)
            }
        )
    }
}

@Composable
private fun NoExpenses(navController: NavHostController) {
    ColumnCardWrapper {
        EmptyComponentWithButton(
            text = stringResource(id = R.string.expenses_overview_no_expenses),
            buttonText = stringResource(id = R.string.expenses_overview_add_expense),
            onButtonClick = {
                navController.navigateToWithArgs(ExpensesNavigationItem.AddExpense, -1, -1)
            }
        )
    }
}

@Composable
private fun ShowConfirmationDialog(
    expensesMonthUi: ExpensesMonthUi?,
    monthToDelete: MutableState<ExpensesMonthUi?>,
    onDeleteExpense: (ExpensesMonthUi) -> Unit
) {
    if (expensesMonthUi != null) {
        AlertDialog(
            onDismissRequest = { monthToDelete.value = null },
            confirmButton = {
                LumbridgeButton(
                    label = stringResource(id = R.string.yes),
                    onClick = {
                        onDeleteExpense(expensesMonthUi)
                        monthToDelete.value = null
                    }
                )
            },
            dismissButton = {
                LumbridgeButton(
                    label = stringResource(id = R.string.no),
                    onClick = { monthToDelete.value = null }
                )
            },
            title = {
                Text(
                    text = stringResource(id = R.string.delete),
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            text = {
                Text(
                    text = stringResource(id = R.string.expense_delete_confirmation),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        )
    }
}

@Composable
private fun ShowSortByDialog(
    availableSorts: List<DisplaySortBy>,
    selectedSort: ExpensesOverviewSortBy,
    openSortByDialog: MutableState<Boolean>,
    onSortBySelected: (Int) -> Unit
) {
    if (openSortByDialog.value) {
        val modalBottomSheetState = rememberModalBottomSheetState()

        ModalBottomSheet(
            sheetState = modalBottomSheetState,
            onDismissRequest = { openSortByDialog.value = false }
        ) {
            LazyColumn(
                modifier = Modifier.padding(DefaultPadding),
                verticalArrangement = Arrangement.spacedBy(DefaultPadding)
            ) {
                items(availableSorts.size) { index ->
                    val sortItem = availableSorts[index]
                    Row {
                        SimpleSetting(
                            label = stringResource(sortItem.nameRes),
                            onClick = {
                                onSortBySelected(sortItem.ordinal)
                                openSortByDialog.value = false
                            },
                            composableRight = {
                                if (selectedSort.ordinal == sortItem.ordinal) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_check),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .padding(horizontal = DefaultPadding)
                                            .size(16.dp)
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ShowFilterDialog(
    availableFilters: List<DisplayFilter>,
    selectedFilter: ExpensesOverviewFilter,
    openFilterDialog: MutableState<Boolean>,
    onFilterSelected: (ordinal: Int, startYear: Int?, startMonth: Int?, endYear: Int?, endMonth: Int?) -> Unit
) {
    if (openFilterDialog.value) {
        val modalBottomSheetState = rememberModalBottomSheetState()
        val startDatePickerState = rememberDatePickerState()
        val endDatePickerState = rememberDatePickerState()

        val showStartFromFilterDialog = remember { mutableStateOf(false) }
        val showUpToFilterDialog = remember { mutableStateOf(false) }
        val showDateRangeFilterDialog = remember { mutableStateOf(false) }

        ModalBottomSheet(
            sheetState = modalBottomSheetState,
            contentWindowInsets = { NavigationBarDefaults.windowInsets },
            onDismissRequest = { openFilterDialog.value = false }
        ) {
            LazyColumn(
                modifier = Modifier.padding(DefaultPadding),
                verticalArrangement = Arrangement.spacedBy(DefaultPadding)
            ) {
                items(availableFilters.size) { index ->
                    val filterItem = availableFilters[index]

                    Row {
                        SimpleSetting(
                            label = stringResource(filterItem.nameRes),
                            onClick = {
                                when (filterItem.ordinal) {
                                    FILTER_NONE_ORDINAL -> {
                                        onFilterSelected(FILTER_NONE_ORDINAL, null, null, null, null)
                                        openFilterDialog.value = false
                                    }

                                    FILTER_START_FROM_ORDINAL -> showStartFromFilterDialog.value = true
                                    FILTER_UP_TO_ORDINAL -> showUpToFilterDialog.value = true
                                    FILTER_DATE_RANGE_ORDINAL -> showDateRangeFilterDialog.value = true
                                }
                            },
                            composableRight = {
                                if (selectedFilter.ordinal == filterItem.ordinal) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_check),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .padding(horizontal = DefaultPadding)
                                            .size(16.dp)
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }

        LumbridgeYearMonthPicker(
            shouldShowDialog = showStartFromFilterDialog,
            title = R.string.filter_date_dialog_title,
            label = R.string.filter_date_start_from_dialog,
            datePickerState = startDatePickerState,
            onSaveDate = { year, month ->
                onFilterSelected(FILTER_START_FROM_ORDINAL, year, month, null, null)
                showStartFromFilterDialog.value = false
                openFilterDialog.value = false
            }
        )

        LumbridgeYearMonthPicker(
            shouldShowDialog = showUpToFilterDialog,
            title = R.string.filter_date_dialog_title,
            label = R.string.filter_date_up_to_dialog,
            datePickerState = endDatePickerState,
            onSaveDate = { year, month ->
                onFilterSelected(FILTER_UP_TO_ORDINAL, null, null, year, month)
                showUpToFilterDialog.value = false
                openFilterDialog.value = false
            }
        )

        LumbridgeYearMonthRangePicker(
            shouldShowDialog = showDateRangeFilterDialog,
            title = R.string.filter_date_dialog_title,
            label = R.string.filter_date_range_dialog,
            startDatePickerState = startDatePickerState,
            endDatePickerState = endDatePickerState,
            onSaveDate = { startYear, startMonth, endYear, endMonth ->
                onFilterSelected(FILTER_DATE_RANGE_ORDINAL, startYear, startMonth, endYear, endMonth)
                showStartFromFilterDialog.value = false
                openFilterDialog.value = false
            }
        )
    }
}

@Composable
private fun getFilterText(selectedFilter: ExpensesOverviewFilter): String {
    val filterName = selectedFilter.toDisplayFilter().nameRes

    return stringResource(filterName)
        .plus(
            when (selectedFilter) {
                is ExpensesOverviewFilter.None -> {
                    String()
                }

                is ExpensesOverviewFilter.DateRange -> {
                    " "
                        .plus((selectedFilter.startYear to selectedFilter.startMonth).toLocalDate().toMonthYearDateString().capitalise())
                        .plus(" - ")
                        .plus((selectedFilter.endYear to selectedFilter.endMonth).toLocalDate().toMonthYearDateString().capitalise())
                }

                is ExpensesOverviewFilter.StartingFrom -> {
                    " "
                        .plus((selectedFilter.year to selectedFilter.month).toLocalDate().toMonthYearDateString().capitalise())
                }

                is ExpensesOverviewFilter.UpTo -> {
                    " "
                        .plus((selectedFilter.year to selectedFilter.month).toLocalDate().toMonthYearDateString().capitalise())
                }
            }
        )
}

@Composable
private fun AddFab(
    modifier: Modifier,
    navController: NavHostController
) {
    FloatingActionButton(
        modifier = modifier.then(
            Modifier.padding(DefaultPadding)
        ),
        onClick = {
            navController.navigateToWithArgs(ExpensesNavigationItem.AddExpense, -1, -1)
        }
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_add),
            contentDescription = stringResource(
                id = R.string.expenses_overview_add_expense
            )
        )
    }
}
