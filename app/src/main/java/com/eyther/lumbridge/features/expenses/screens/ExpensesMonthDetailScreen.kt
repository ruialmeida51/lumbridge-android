package com.eyther.lumbridge.features.expenses.screens

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.eyther.lumbridge.R
import com.eyther.lumbridge.extensions.kotlin.forceTwoDecimalsPlaces
import com.eyther.lumbridge.extensions.platform.navigateTo
import com.eyther.lumbridge.extensions.platform.navigateToWithArgs
import com.eyther.lumbridge.features.expenses.model.monthdetails.ExpensesMonthDetailScreenViewEffect
import com.eyther.lumbridge.features.expenses.model.monthdetails.ExpensesMonthDetailScreenViewState.Content
import com.eyther.lumbridge.features.expenses.model.monthdetails.ExpensesMonthDetailScreenViewState.Error
import com.eyther.lumbridge.features.expenses.model.monthdetails.ExpensesMonthDetailScreenViewState.Loading
import com.eyther.lumbridge.features.expenses.navigation.ExpensesNavigationItem
import com.eyther.lumbridge.features.expenses.viewmodel.monthdetails.ExpensesMonthDetailScreenViewModel
import com.eyther.lumbridge.features.expenses.viewmodel.monthdetails.IExpensesMonthDetailScreenViewModel
import com.eyther.lumbridge.model.expenses.ExpensesCategoryUi
import com.eyther.lumbridge.model.expenses.ExpensesDetailedUi
import com.eyther.lumbridge.model.expenses.ExpensesMonthUi
import com.eyther.lumbridge.ui.common.composables.components.buttons.LumbridgeButton
import com.eyther.lumbridge.ui.common.composables.components.card.ColumnCardWrapper
import com.eyther.lumbridge.ui.common.composables.components.defaults.EmptyScreenWithButton
import com.eyther.lumbridge.ui.common.composables.components.loading.LoadingIndicator
import com.eyther.lumbridge.ui.common.composables.components.text.TabbedDataOverview
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.LumbridgeTopAppBar
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.TopAppBarVariation
import com.eyther.lumbridge.ui.common.model.math.MathOperator
import com.eyther.lumbridge.ui.theme.DefaultAndAHalfPadding
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.HalfPadding
import com.eyther.lumbridge.ui.theme.QuarterPadding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun ExpensesMonthDetailScreen(
    navController: NavHostController,
    @StringRes label: Int,
    viewModel: IExpensesMonthDetailScreenViewModel = hiltViewModel<ExpensesMonthDetailScreenViewModel>()
) {
    val state = viewModel.viewState.collectAsStateWithLifecycle().value
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    val snackbarHostState = remember { SnackbarHostState() }
    val monthToDelete = remember { mutableStateOf<ExpensesMonthUi?>(null) }

    val genericErrorMessage = stringResource(R.string.generic_error_message)
    val genericErrorName = stringResource(R.string.error)

    LaunchedEffect(Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.viewEffects
                .onEach { viewEffects ->
                    when (viewEffects) {
                        is ExpensesMonthDetailScreenViewEffect.ShowError -> {
                            snackbarHostState.showSnackbar(
                                message = "$genericErrorMessage ($genericErrorName: ${viewEffects.message})",
                                duration = SnackbarDuration.Short
                            )
                        }
                        is ExpensesMonthDetailScreenViewEffect.NavigateBack -> {
                            navController.popBackStack()
                        }
                    }
                }
                .collect()
        }
    }

    Scaffold(
        topBar = {
            LumbridgeTopAppBar(
                topAppBarVariation = TopAppBarVariation.TitleAndIcon(
                    title = stringResource(id = label),
                    onIconClick = { navController.popBackStack() }
                ),
                actions = {
                    Icon(
                        modifier = Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(bounded = false),
                            onClick = viewModel::collapseAll
                        ),
                        painter = painterResource(R.drawable.ic_unfold_less),
                        contentDescription = stringResource(id = R.string.collapse)
                    )

                    Icon(
                        modifier = Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(bounded = false),
                            onClick = viewModel::expandAll
                        ),
                        painter = painterResource(R.drawable.ic_unfold_more),
                        contentDescription = stringResource(id = R.string.expand)
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
                is Loading -> LoadingIndicator()
                is Error -> EmptyScreen(navController::popBackStack)
                is Content -> {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Content(
                            state = state,
                            monthToDelete = monthToDelete,
                            onSelectCategory = viewModel::expandCategory,
                            onEditExpense = { navController.navigateToWithArgs(ExpensesNavigationItem.EditExpense, it.id) }
                        )

                        ShowConfirmationDialog(
                            expensesMonthUi = monthToDelete.value,
                            monthToDelete = monthToDelete,
                            onDeleteExpense = viewModel::onDeleteExpense
                        )

                        AddFab(
                            modifier = Modifier.align(Alignment.BottomEnd),
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Content(
    state: Content,
    monthToDelete: MutableState<ExpensesMonthUi?>,
    onSelectCategory: (category: ExpensesCategoryUi) -> Unit,
    onEditExpense: (expense: ExpensesDetailedUi) -> Unit
) {
    ColumnCardWrapper(
        modifier = Modifier.animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(HalfPadding)
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
                        .padding(horizontal = HalfPadding)
                        .size(20.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(bounded = false)
                        ) {
                            monthToDelete.value = state.monthExpenses
                        },
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = null
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier.padding(bottom = HalfPadding),
                    text = state.monthExpenses.getDateWithLocale(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.tertiary
                )

//                MonthlyAllocationGraph(
//                    showAllocationForExpenses = showAllocationForExpenses,
//                    expensesMonthUi = expensesMonthUi,
//                    currencySymbol = currencySymbol
//                )

                Spacer(modifier = Modifier.height(HalfPadding))

                CategoriesCard(
                    expensesCategories = { state.monthExpenses.categoryExpenses },
                    showAllocationForExpenses = state.showAllocations,
                    onSelectCategory = onSelectCategory,
                    currencySymbol = state.locale.getCurrencySymbol(),
                    onEditExpense = onEditExpense
                )
            }
        }
    }
}

@Composable
private fun CategoriesCard(
    expensesCategories: () -> List<ExpensesCategoryUi>,
    showAllocationForExpenses: Boolean,
    currencySymbol: String,
    onSelectCategory: (ExpensesCategoryUi) -> Unit,
    onEditExpense: (ExpensesDetailedUi) -> Unit
) {
    Column {
        expensesCategories().forEach { category ->
            val mathOperatorString = when (category.categoryType.operator) {
                MathOperator.ADDITION -> "+"
                MathOperator.SUBTRACTION -> ""
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .clickable { onSelectCategory(category) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = category.categoryType.iconRes),
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
                    text = "$mathOperatorString${category.spent.forceTwoDecimalsPlaces()}$currencySymbol"
                )

            }

            AnimatedVisibility(
                visible = category.expanded
            ) {
                DetailsCard(
                    expensesDetailed = { category.expensesDetailedUi },
                    showAllocationForExpenses = showAllocationForExpenses,
                    mathOperatorString = mathOperatorString,
                    currencySymbol = currencySymbol,
                    onEditExpense = onEditExpense
                )
            }
        }
    }
}

@Composable
private fun DetailsCard(
    expensesDetailed: () -> List<ExpensesDetailedUi>,
    showAllocationForExpenses: Boolean,
    mathOperatorString: String,
    currencySymbol: String,
    onEditExpense: (ExpensesDetailedUi) -> Unit
) {
    Column {
        expensesDetailed().forEach { detail ->
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
                        Row {
                            if (showAllocationForExpenses) {
                                Spacer(
                                    modifier = Modifier.width(QuarterPadding)
                                )

                                Icon(
                                    modifier = Modifier.size(16.dp),
                                    painter = painterResource(id = detail.allocationTypeUi.iconRes),
                                    contentDescription = stringResource(id = detail.allocationTypeUi.labelRes)
                                )
                            }

                            Spacer(
                                modifier = Modifier.width(QuarterPadding)
                            )

                            Icon(
                                modifier = Modifier.size(16.dp),
                                painter = painterResource(id = R.drawable.ic_arrow_next),
                                contentDescription = stringResource(id = R.string.edit)
                            )
                        }
                    },
                    text = "$mathOperatorString${detail.expenseAmount.forceTwoDecimalsPlaces()}$currencySymbol"
                )

                Spacer(modifier = Modifier.width(HalfPadding))
            }
        }
    }
}

@Composable
private fun EmptyScreen(
    onErrorButtonClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        EmptyScreenWithButton(
            modifier = Modifier.padding(DefaultPadding),
            text = stringResource(id = R.string.expenses_month_detail_error_not_found),
            buttonText = stringResource(id = R.string.expenses_month_detail_error_not_found_button),
            icon = {
                Icon(
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(id = R.drawable.ic_error),
                    contentDescription = stringResource(id = R.string.expenses_month_detail)
                )
            },
            onButtonClick = onErrorButtonClick
        )
    }
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
            navController.navigateTo(ExpensesNavigationItem.AddExpense)
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
