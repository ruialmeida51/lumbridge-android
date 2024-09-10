@file:OptIn(ExperimentalMaterial3Api::class)

package com.eyther.lumbridge.features.expenses.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.eyther.lumbridge.R
import com.eyther.lumbridge.extensions.kotlin.capitalise
import com.eyther.lumbridge.extensions.kotlin.forceTwoDecimalsPlaces
import com.eyther.lumbridge.features.expenses.model.overview.ExpensesOverviewScreenViewEffect
import com.eyther.lumbridge.features.expenses.model.overview.ExpensesOverviewScreenViewState
import com.eyther.lumbridge.features.expenses.model.overview.ExpensesOverviewScreenViewState.Content
import com.eyther.lumbridge.features.expenses.model.overview.ExpensesOverviewScreenViewState.Empty
import com.eyther.lumbridge.features.expenses.model.overview.ExpensesOverviewScreenViewState.Loading
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
import com.eyther.lumbridge.ui.common.composables.components.card.RowCardWrapper
import com.eyther.lumbridge.ui.common.composables.components.defaults.EmptyComponentWithButton
import com.eyther.lumbridge.ui.common.composables.components.loading.LoadingIndicator
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
    val snackbarHostState = remember { SnackbarHostState() }
    val selectedMonth = remember { mutableLongStateOf(-1L) }

    LaunchedEffect(Unit) {
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

    Scaffold(
        topBar = {
            LumbridgeTopAppBar(
                TopAppBarVariation.Title(
                    title = stringResource(id = label)
                )
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
                        onSelectMonth = viewModel::expandMonth,
                        onSelectCategory = viewModel::expandCategory,
                        onEditExpense = { viewModel.onEditExpense(navController, it) },
                        onDeleteExpense = viewModel::onDeleteExpense,
                        navigate = viewModel::navigate
                    )
                }

                is Empty -> {
                    Empty(
                        state = state,
                        navController = navController,
                        navigate = viewModel::navigate
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
    onSelectMonth: (ExpensesMonthUi) -> Unit,
    onSelectCategory: (ExpensesCategoryUi) -> Unit,
    onEditExpense: (ExpensesDetailedUi) -> Unit,
    onDeleteExpense: (ExpensesMonthUi) -> Unit,
    navigate: (NavigationItem, NavHostController) -> Unit
) {
    LazyColumn {
        items(state.expensesMonthUi.count()) { index ->
            val monthExpensesUi = state.expensesMonthUi[index]

            if (index == 0) {
                FinancialProfile(
                    state = state,
                    navigate = navigate,
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
}

@Composable
private fun Empty(
    state: Empty,
    navController: NavHostController,
    navigate: (NavigationItem, NavHostController) -> Unit
) {
    FinancialProfile(
        state = state,
        navigate = navigate,
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
    navigate: (NavigationItem, NavHostController) -> Unit
) {
    when (state) {
        is Content.NoFinancialProfile,
        is Empty.NoFinancialProfile -> {
            NoFinancialProfile(navController, navigate)
        }

        is Content.HasFinancialProfile -> {
            HasFinancialProfile(
                netSalaryUi = state.netSalaryUi,
                totalSpent = state.totalExpenses,
                navController = navController,
                navigate = navigate,
                currencySymbol = state.locale.getCurrencySymbol()
            )
        }

        is Empty.HasFinancialProfile -> {
            HasFinancialProfile(
                netSalaryUi = state.netSalaryUi,
                totalSpent = null,
                navController = navController,
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
    currencySymbol: String,
    navController: NavHostController,
    navigate: (NavigationItem, NavHostController) -> Unit
) {
    RowCardWrapper {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_dollar),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )

                Spacer(modifier = Modifier.height(HalfPadding))

                DataOverview(
                    label = stringResource(id = R.string.net_monthly),
                    text = "${netSalaryUi.monthlyNetSalary.forceTwoDecimalsPlaces()}$currencySymbol"
                )
            }

            if (totalSpent != null) {
                Spacer(modifier = Modifier.height(HalfPadding))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_chart),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.height(HalfPadding))

                    DataOverview(
                        label = stringResource(id = R.string.total),
                        text = "${totalSpent.forceTwoDecimalsPlaces()}$currencySymbol"
                    )
                }
            }
        }

        Icon(
            modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(bounded = false)
                ) { navigate(ExpensesNavigationItem.EditFinancialProfile, navController) },
            painter = painterResource(id = R.drawable.ic_edit),
            contentDescription = stringResource(id = R.string.edit)
        )
    }
}

@Composable
private fun MonthCard(
    expensesMonthUi: ExpensesMonthUi,
    currencySymbol: String,
    selectedMonth: MutableState<Long>,
    onSelectMonth: (ExpensesMonthUi) -> Unit,
    onSelectCategory: (ExpensesCategoryUi) -> Unit,
    onEditExpense: (ExpensesDetailedUi) -> Unit
) {

    ColumnCardWrapper(
        onClick = { onSelectMonth(expensesMonthUi) }
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterStart
        ) {

            Row(
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { selectedMonth.value = expensesMonthUi.id },
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(DefaultPadding))

                Icon(
                    modifier = Modifier
                        .size(24.dp)
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
                    text = "${expensesMonthUi.month.name.capitalise()} ${expensesMonthUi.year}",
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

                if (expensesMonthUi.expanded) {
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

            if (category.expanded) {
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
private fun NoFinancialProfile(navController: NavHostController, navigate: (NavigationItem, NavHostController) -> Unit) {
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
