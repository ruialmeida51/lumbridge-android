@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.eyther.lumbridge.features.expenses.screens

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.eyther.lumbridge.R
import com.eyther.lumbridge.domain.time.toLocalDate
import com.eyther.lumbridge.domain.time.toMonthYearDateString
import com.eyther.lumbridge.extensions.kotlin.capitalise
import com.eyther.lumbridge.extensions.kotlin.forceTwoDecimalsPlaces
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
import com.eyther.lumbridge.features.expenses.viewmodel.overview.ExpensesOverviewScreenViewModel
import com.eyther.lumbridge.features.expenses.viewmodel.overview.IExpensesOverviewScreenViewModel
import com.eyther.lumbridge.features.overview.components.DataOverview
import com.eyther.lumbridge.features.overview.components.TabbedDataOverview
import com.eyther.lumbridge.model.expenses.ExpensesCategoryUi
import com.eyther.lumbridge.model.expenses.ExpensesDetailedUi
import com.eyther.lumbridge.model.expenses.ExpensesMonthUi
import com.eyther.lumbridge.model.finance.NetSalaryUi
import com.eyther.lumbridge.ui.common.composables.components.buttons.LumbridgeButton
import com.eyther.lumbridge.ui.common.composables.components.card.ColumnCardWrapper
import com.eyther.lumbridge.ui.common.composables.components.datepicker.LumbridgeYearMonthPicker
import com.eyther.lumbridge.ui.common.composables.components.datepicker.LumbridgeYearMonthRangePicker
import com.eyther.lumbridge.ui.common.composables.components.defaults.EmptyComponentWithButton
import com.eyther.lumbridge.ui.common.composables.components.loading.LoadingIndicator
import com.eyther.lumbridge.ui.common.composables.components.setting.SimpleSetting
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.LumbridgeTopAppBar
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.TopAppBarVariation
import com.eyther.lumbridge.ui.navigation.NavigationItem
import com.eyther.lumbridge.ui.theme.DefaultAndAHalfPadding
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.HalfPadding
import com.eyther.lumbridge.ui.theme.QuarterPadding
import com.eyther.lumbridge.ui.theme.SmallButtonHeight
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun ExpensesOverviewScreen(
    navController: NavHostController,
    @StringRes label: Int,
    viewModel: IExpensesOverviewScreenViewModel = hiltViewModel<ExpensesOverviewScreenViewModel>()
) {
    val state = viewModel.viewState.collectAsStateWithLifecycle().value
    val lifecycleOwner = LocalLifecycleOwner.current
    val snackbarHostState = remember { SnackbarHostState() }
    val selectedMonth = remember { mutableLongStateOf(-1L) }
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
                            indication = rememberRipple(bounded = false),
                            onClick = { viewModel.navigate(ExpensesNavigationItem.AddExpense, navController) }
                        ),
                        painter = painterResource(R.drawable.ic_add),
                        contentDescription = stringResource(
                            id = R.string.expenses_overview_add_expense
                        )
                    )

                    Icon(
                        modifier = Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(bounded = false),
                            onClick = viewModel::collapseAll
                        ),
                        painter = painterResource(R.drawable.ic_unfold_less),
                        contentDescription = stringResource(id = R.string.collapse)
                    )

                    Icon(
                        modifier = Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(bounded = false),
                            onClick = viewModel::expandAll
                        ),
                        painter = painterResource(R.drawable.ic_unfold_more),
                        contentDescription = stringResource(id = R.string.expand)
                    )

                    Icon(
                        modifier = Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(bounded = false)
                        ) {
                            openSortByDialog.value = true
                        },
                        painter = painterResource(R.drawable.ic_sort_by),
                        contentDescription = stringResource(id = R.string.sort_by),
                    )

                    Icon(
                        modifier = Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(bounded = false)
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
                .padding(vertical = DefaultPadding)
                .then(
                    if (selectedMonth.longValue >= 0L) Modifier.blur(5.dp) else Modifier
                )
        ) {
            when (state) {
                is Loading -> {
                    LoadingIndicator()
                }

                is Content -> {
                    Content(
                        state = state,
                        navController = navController,
                        selectedMonth = selectedMonth,
                        openSortByDialog = openSortByDialog,
                        openFilterDialog = openFilterDialog,
                        onSelectMonth = viewModel::expandMonth,
                        onSelectCategory = viewModel::expandCategory,
                        onEditExpense = { viewModel.onEditExpense(navController, it) },
                        onDeleteExpense = viewModel::onDeleteExpense,
                        onSortBySelected = viewModel::onSortBy,
                        onFilterSelected = viewModel::onFilter,
                        onClearFilter = viewModel::onClearFilter,
                        onClearSortBy = viewModel::onClearSortBy,
                        navigate = viewModel::navigate
                    )
                }

                is Empty -> {
                    Empty(
                        state = state,
                        navController = navController,
                        navigate = viewModel::navigate,
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
    selectedMonth: MutableState<Long>,
    openSortByDialog: MutableState<Boolean>,
    openFilterDialog: MutableState<Boolean>,
    onSelectMonth: (ExpensesMonthUi) -> Unit,
    onSelectCategory: (ExpensesCategoryUi) -> Unit,
    onEditExpense: (ExpensesDetailedUi) -> Unit,
    onDeleteExpense: (ExpensesMonthUi) -> Unit,
    onSortBySelected: (Int) -> Unit,
    onFilterSelected: (ordinal: Int, startYear: Int?, startMonth: Int?, endYear: Int?, endMonth: Int?) -> Unit,
    onClearFilter: () -> Unit,
    onClearSortBy: () -> Unit,
    navigate: (NavigationItem, NavHostController) -> Unit
) {
    if (!state.hasExpenses()) {
        FinancialProfile(
            state = state,
            navigate = navigate,
            onClearFilter = onClearFilter,
            onClearSortBy = onClearSortBy,
            navController = navController
        )

        Spacer(modifier = Modifier.height(HalfPadding))

        LumbridgeButton(
            modifier = Modifier.padding(horizontal = DefaultPadding),
            label = stringResource(id = R.string.expenses_overview_add_expense),
            minHeight = SmallButtonHeight
        ) {
            navigate(ExpensesNavigationItem.AddExpense, navController)
        }

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
                    navigate = navigate,
                    onClearFilter = onClearFilter,
                    onClearSortBy = onClearSortBy,
                    navController = navController
                )

                Spacer(modifier = Modifier.height(HalfPadding))

                LumbridgeButton(
                    modifier = Modifier.padding(horizontal = DefaultPadding),
                    label = stringResource(id = R.string.expenses_overview_add_expense),
                    minHeight = SmallButtonHeight
                ) {
                    navigate(ExpensesNavigationItem.AddExpense, navController)
                }

                Spacer(modifier = Modifier.height(HalfPadding))
            }

            Spacer(modifier = Modifier.height(HalfPadding))

            MonthCard(
                expensesMonthUi = monthExpensesUi,
                onSelectMonth = onSelectMonth,
                onSelectCategory = onSelectCategory,
                selectedMonth = selectedMonth,
                currencySymbol = state.locale.getCurrencySymbol(),
                onEditExpense = onEditExpense
            )

            if (index == state.expensesMonthUi.lastIndex) {
                Spacer(modifier = Modifier.height(DefaultPadding))
            }
        }
    }

    ShowConfirmationDialog(
        expensesMonthUi = state.expensesMonthUi.find { it.id == selectedMonth.value },
        selectedMonth = selectedMonth,
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
    onClearSortBy: () -> Unit,
    navigate: (NavigationItem, NavHostController) -> Unit
) {
    FinancialProfile(
        state = state,
        navigate = navigate,
        onClearFilter = onClearFilter,
        onClearSortBy = onClearSortBy,
        navController = navController
    )

    Spacer(modifier = Modifier.height(HalfPadding))

    NoExpenses(
        navigate = navigate,
        navController = navController
    )
}

@Composable
private fun FinancialProfile(
    state: ExpensesOverviewScreenViewState,
    navController: NavHostController,
    onClearFilter: () -> Unit,
    onClearSortBy: () -> Unit,
    navigate: (NavigationItem, NavHostController) -> Unit
) {
    when (state) {
        is Empty.NoFinancialProfile -> {
            EmptyAndNoFinancialProfile(
                navController = navController,
                navigate = navigate
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
                navigate = navigate,
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
                navigate = navigate,
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
                navigate = navigate,
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
    onClearSortBy: () -> Unit,
    navigate: (NavigationItem, NavHostController) -> Unit
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
                        indication = rememberRipple(bounded = false)
                    ) { navigate(ExpensesNavigationItem.EditFinancialProfile, navController) },
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
    currencySymbol: String,
    selectedMonth: MutableState<Long>,
    onSelectMonth: (ExpensesMonthUi) -> Unit,
    onSelectCategory: (ExpensesCategoryUi) -> Unit,
    onEditExpense: (ExpensesDetailedUi) -> Unit
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
                        .padding(horizontal = HalfPadding)
                        .size(20.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(bounded = false)
                        ) {
                            selectedMonth.value = expensesMonthUi.id
                        },
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = null
                )

                Icon(
                    modifier = Modifier
                        .size(20.dp)
                        .rotate(if (expensesMonthUi.expanded) 180f else 0f),
                    imageVector = Icons.Outlined.ArrowDropDown,
                    contentDescription = null
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier.padding(bottom = QuarterPadding),
                    text = expensesMonthUi.getDateWithLocale(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.tertiary
                )

                DataOverview(
                    label = stringResource(id = R.string.spent),
                    text = "${expensesMonthUi.spent.forceTwoDecimalsPlaces()}$currencySymbol"
                )

                DataOverview(
                    label = stringResource(id = R.string.remainder),
                    text = "${expensesMonthUi.remainder.forceTwoDecimalsPlaces()}$currencySymbol"
                )

                AnimatedVisibility(
                    visible = expensesMonthUi.expanded
                ) {
                    Spacer(modifier = Modifier.height(HalfPadding))

                    CategoriesCard(
                        expensesCategories = expensesMonthUi.categoryExpenses,
                        onSelectCategory = onSelectCategory,
                        currencySymbol = currencySymbol,
                        onEditExpense = onEditExpense
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoriesCard(
    expensesCategories: List<ExpensesCategoryUi>,
    currencySymbol: String,
    onSelectCategory: (ExpensesCategoryUi) -> Unit,
    onEditExpense: (ExpensesDetailedUi) -> Unit
) {
    Column {
        expensesCategories.forEach { category ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .clickable { onSelectCategory(category) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = category.icon),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )

                Spacer(modifier = Modifier.width(QuarterPadding))

                TabbedDataOverview(
                    icon = {
                        Icon(
                            modifier = Modifier
                                .size(16.dp)
                                .rotate(if (category.expanded) 180f else 0f),
                            imageVector = Icons.Outlined.ArrowDropDown,
                            contentDescription = null
                        )
                    },
                    label = stringResource(category.categoryType.categoryRes),
                    text = "${category.spent.forceTwoDecimalsPlaces()}$currencySymbol"
                )

            }

            AnimatedVisibility(
                visible = category.expanded
            ) {
                DetailsCard(
                    expensesDetailed = category.expensesDetailedUi,
                    currencySymbol = currencySymbol,
                    onEditExpense = onEditExpense
                )
            }
        }
    }
}

@Composable
private fun DetailsCard(
    expensesDetailed: List<ExpensesDetailedUi>,
    currencySymbol: String,
    onEditExpense: (ExpensesDetailedUi) -> Unit
) {
    Column {
        expensesDetailed.forEach { detail ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
                    .clickable { onEditExpense(detail) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.height(HalfPadding))

                TabbedDataOverview(
                    startPadding = DefaultAndAHalfPadding,
                    textStyle = MaterialTheme.typography.bodySmall,
                    labelStyle = MaterialTheme.typography.bodySmall,
                    labelColour = MaterialTheme.colorScheme.onSurface,
                    textColour = MaterialTheme.colorScheme.tertiary,
                    label = detail.expenseName,
                    icon = {
                        Icon(
                            modifier = Modifier
                                .size(16.dp),
                            painter = painterResource(id = R.drawable.ic_edit_note),
                            contentDescription = stringResource(id = R.string.edit)
                        )
                    },
                    text = "${detail.expenseAmount.forceTwoDecimalsPlaces()}$currencySymbol"
                )

                Spacer(modifier = Modifier.width(HalfPadding))
            }
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
    onClearSortBy: () -> Unit,
    navigate: (NavigationItem, NavHostController) -> Unit
) {
    ColumnCardWrapper {
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
                navigate(
                    ExpensesNavigationItem.EditFinancialProfile,
                    navController
                )
            }
        )
    }
}

@Composable
private fun EmptyAndNoFinancialProfile(
    navController: NavHostController,
    navigate: (NavigationItem, NavHostController) -> Unit
) {
    ColumnCardWrapper {
        EmptyComponentWithButton(
            text = stringResource(id = R.string.expenses_overview_no_financial_profile),
            buttonText = stringResource(id = R.string.financial_overview_create_profile),
            onButtonClick = {
                navigate(
                    ExpensesNavigationItem.EditFinancialProfile,
                    navController
                )
            }
        )
    }
}

@Composable
private fun NoExpenses(navController: NavHostController, navigate: (NavigationItem, NavHostController) -> Unit) {
    ColumnCardWrapper {
        EmptyComponentWithButton(
            text = stringResource(id = R.string.expenses_overview_no_expenses),
            buttonText = stringResource(id = R.string.expenses_overview_add_expense),
            onButtonClick = {
                navigate(
                    ExpensesNavigationItem.AddExpense,
                    navController
                )
            }
        )
    }
}

@Composable
private fun ShowConfirmationDialog(
    expensesMonthUi: ExpensesMonthUi?,
    selectedMonth: MutableState<Long>,
    onDeleteExpense: (ExpensesMonthUi) -> Unit
) {
    if (selectedMonth.value >= 0L && expensesMonthUi != null) {
        AlertDialog(
            onDismissRequest = { selectedMonth.value = -1L },
            confirmButton = {
                LumbridgeButton(
                    label = stringResource(id = R.string.yes),
                    onClick = {
                        onDeleteExpense(expensesMonthUi)
                        selectedMonth.value = -1L
                    }
                )
            },
            dismissButton = {
                LumbridgeButton(
                    label = stringResource(id = R.string.no),
                    onClick = { selectedMonth.value = -1L }
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
            windowInsets = NavigationBarDefaults.windowInsets,
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













