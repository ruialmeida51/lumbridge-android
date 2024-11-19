package com.eyther.lumbridge.features.overview.breakdown.screens

import androidx.annotation.StringRes
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.eyther.lumbridge.R
import com.eyther.lumbridge.extensions.kotlin.forceTwoDecimalsPlaces
import com.eyther.lumbridge.extensions.platform.navigateTo
import com.eyther.lumbridge.extensions.platform.navigateToWithArgs
import com.eyther.lumbridge.features.overview.breakdown.model.BalanceSheetNetUi
import com.eyther.lumbridge.features.overview.breakdown.model.BreakdownScreenViewState
import com.eyther.lumbridge.features.overview.breakdown.viewmodel.BreakdownScreenViewModel
import com.eyther.lumbridge.features.overview.breakdown.viewmodel.IBreakdownScreenViewModel
import com.eyther.lumbridge.features.overview.navigation.OverviewNavigationItem
import com.eyther.lumbridge.model.finance.NetSalaryUi
import com.eyther.lumbridge.model.loan.LoanUi
import com.eyther.lumbridge.ui.common.composables.components.buttons.LumbridgeButton
import com.eyther.lumbridge.ui.common.composables.components.card.ColumnCardWrapper
import com.eyther.lumbridge.ui.common.composables.components.defaults.EmptyComponentWithButton
import com.eyther.lumbridge.ui.common.composables.components.loading.LoadingIndicator
import com.eyther.lumbridge.ui.common.composables.components.loan.PeekLoanCard
import com.eyther.lumbridge.ui.common.composables.components.progress.LineProgressIndicator
import com.eyther.lumbridge.ui.common.composables.components.setting.MovementSetting
import com.eyther.lumbridge.ui.common.composables.components.text.TabbedDataOverview
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.LumbridgeTopAppBar
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.TopAppBarVariation
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.HalfPadding
import com.eyther.lumbridge.ui.theme.QuarterPadding

@Composable
fun BreakdownScreen(
    navController: NavHostController,
    @StringRes label: Int,
    viewModel: IBreakdownScreenViewModel = hiltViewModel<BreakdownScreenViewModel>()
) {
    val state = viewModel.viewState.collectAsStateWithLifecycle().value
    val loanToDelete = remember { mutableLongStateOf(-1L) }

    Scaffold(
        topBar = {
            LumbridgeTopAppBar(
                topAppBarVariation = TopAppBarVariation.Title(title = stringResource(id = label))
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .imePadding()
                .then(
                    if (loanToDelete.longValue >= 0L) Modifier.blur(5.dp) else Modifier
                )
        ) {
            when (state) {
                is BreakdownScreenViewState.Loading -> LoadingIndicator()
                is BreakdownScreenViewState.Content -> Content(
                    state = state,
                    navController = navController,
                    loanToDelete = loanToDelete,
                    onDeleteLoan = viewModel::onDeleteLoan
                )

            }
        }
    }
}

@Composable
private fun Content(
    state: BreakdownScreenViewState.Content,
    navController: NavHostController,
    loanToDelete: MutableLongState,
    onDeleteLoan: (LoanUi) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Overview(
            state = state,
            navController = navController,
            loanToDelete = loanToDelete
        )

        if (state.loans.isNotEmpty()) {
            AddFab(
                modifier = Modifier.align(Alignment.BottomEnd),
                navController = navController
            )
        }

        ShowDeleteConfirmationDialog(
            loanUi = state.loans.find { it.first.id == loanToDelete.longValue }?.first,
            loanToDelete = loanToDelete,
            onDeleteLoan = onDeleteLoan
        )
    }
}

@Composable
private fun Overview(
    state: BreakdownScreenViewState.Content,
    navController: NavHostController,
    loanToDelete: MutableLongState
) {
    LazyColumn(
        modifier = Modifier
            .padding(top = DefaultPadding)
    ) {
        item {
            BalanceSheet(
                currencySymbol = state.currencySymbol,
                state = state
            )

            Spacer(modifier = Modifier.height(HalfPadding))
        }

        item {
            SalaryOverview(
                netSalaryUi = state.netSalary,
                currencySymbol = state.currencySymbol,
                onCardClick = { navController.navigateTo(OverviewNavigationItem.FinancialProfile.Details) },
                onCreateFinancialProfile = { navController.navigateTo(OverviewNavigationItem.FinancialProfile.Edit) }
            )

            Spacer(modifier = Modifier.height(HalfPadding))
        }

        item {
            Text(
                modifier = Modifier
                    .padding(start = DefaultPadding, end = DefaultPadding, bottom = HalfPadding),
                text = stringResource(id = R.string.breakdown_loans_title),
                style = MaterialTheme.typography.bodyLarge
            )
        }

        if (state.loans.isEmpty()) {
            item {
                ColumnCardWrapper {
                    EmptyComponentWithButton(
                        text = stringResource(id = R.string.breakdown_loans_no_loans),
                        buttonText = stringResource(id = R.string.breakdown_loans_no_loans_create),
                        onButtonClick = { navController.navigateToWithArgs(OverviewNavigationItem.Loan.Edit, -1L) }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(DefaultPadding))
            }
        } else {
            items(state.loans) { (loanUi, loanCalculationUi) ->
                PeekLoanCard(
                    loanUi = loanUi,
                    loanCalculationUi = loanCalculationUi,
                    currencySymbol = state.currencySymbol,
                    onDelete = { loanToDelete.longValue = it },
                    onCardClick = { navController.navigateToWithArgs(OverviewNavigationItem.Loan.Details, loanUi.id) }
                )

                Spacer(modifier = Modifier.height(DefaultPadding))
            }

            item {
                Spacer(modifier = Modifier.height(DefaultPadding + 56.dp)) // 56dp is the height of the FAB
            }
        }
    }
}

@Composable
private fun BalanceSheet(
    state: BreakdownScreenViewState.Content,
    currencySymbol: String
) {
    Text(
        modifier = Modifier
            .padding(start = DefaultPadding, end = DefaultPadding, bottom = HalfPadding),
        text = stringResource(id = R.string.breakdown_balance_sheet),
        style = MaterialTheme.typography.bodyLarge
    )

    ColumnCardWrapper {
        if (state.balanceSheetNet != null) {
            BalanceSheetNet(
                balanceSheetNetUi = state.balanceSheetNet,
                currencySymbol = currencySymbol
            )
        } else {
            Text(
                text = stringResource(id = R.string.breakdown_balance_sheet_no_data),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
private fun BalanceSheetNet(
    balanceSheetNetUi: BalanceSheetNetUi,
    currencySymbol: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    modifier = Modifier
                        .size(16.dp)
                        .align(Alignment.CenterVertically),
                    painter = painterResource(id = R.drawable.ic_outward),
                    tint = MaterialTheme.colorScheme.tertiary,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(QuarterPadding))

                Text(
                    text = stringResource(id = R.string.breakdown_balance_sheet_money_in),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(HalfPadding))

            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.Center
            ) {

                Text(
                    text = stringResource(id = R.string.breakdown_balance_sheet_money_out),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.width(QuarterPadding))

                Icon(
                    modifier = Modifier
                        .size(16.dp)
                        .align(Alignment.CenterVertically),
                    painter = painterResource(id = R.drawable.ic_downward),
                    tint = MaterialTheme.colorScheme.error,
                    contentDescription = null
                )
            }
        }

        Spacer(modifier = Modifier.height(HalfPadding))

        Row(
            horizontalArrangement = Arrangement.spacedBy(HalfPadding)
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = "+${balanceSheetNetUi.moneyIn.forceTwoDecimalsPlaces()}$currencySymbol",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.tertiary,
                textAlign = TextAlign.Center
            )

            Text(
                modifier = Modifier.weight(1f),
                text = "-${balanceSheetNetUi.moneyOut.forceTwoDecimalsPlaces()}$currencySymbol",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(DefaultPadding))

        LineProgressIndicator(
            modifier = Modifier
                .fillMaxWidth(fraction = 0.75f)
                .height(8.dp)
                .align(Alignment.CenterHorizontally),
            backgroundColor = MaterialTheme.colorScheme.error,
            progressColor = MaterialTheme.colorScheme.tertiary,
            progress = 1 - balanceSheetNetUi.percentageSpent // Invert the percentage spent to show the money in as the progress
        )

        Spacer(modifier = Modifier.height(DefaultPadding))

        HorizontalDivider()

        Spacer(modifier = Modifier.height(DefaultPadding))

        Row {
            Icon(
                painter = painterResource(id = R.drawable.ic_payments),
                modifier = Modifier
                    .size(16.dp)
                    .align(Alignment.CenterVertically),
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(HalfPadding))

            TabbedDataOverview(
                modifier = Modifier.weight(1f),
                label = stringResource(id = R.string.breakdown_balance_sheet_net),
                text = "${balanceSheetNetUi.net.forceTwoDecimalsPlaces()}$currencySymbol"
            )
        }
    }
}

@Composable
private fun SalaryOverview(
    netSalaryUi: NetSalaryUi?,
    currencySymbol: String,
    onCardClick: () -> Unit,
    onCreateFinancialProfile: () -> Unit
) {
    Text(
        modifier = Modifier
            .padding(start = DefaultPadding, end = DefaultPadding, bottom = HalfPadding),
        text = stringResource(id = R.string.breakdown_salary_title),
        style = MaterialTheme.typography.bodyLarge
    )

    if (netSalaryUi == null) {
        ColumnCardWrapper {
            EmptyComponentWithButton(
                text = stringResource(id = R.string.breakdown_salary_no_financial_profile),
                buttonText = stringResource(id = R.string.breakdown_salary_no_financial_profile_create),
                onButtonClick = onCreateFinancialProfile
            )
        }
    } else {
        ColumnCardWrapper(
            onClick = onCardClick
        ) {
            Text(
                modifier = Modifier.padding(bottom = QuarterPadding),
                text = stringResource(id = R.string.breakdown_salary_card_income),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.tertiary
            )

            TabbedDataOverview(
                label = stringResource(id = R.string.gross_annual),
                text = "${netSalaryUi.annualGrossSalary.forceTwoDecimalsPlaces()}$currencySymbol"
            )

            TabbedDataOverview(
                label = stringResource(id = R.string.breakdown_salary_net_monthly),
                text = "${netSalaryUi.monthlyNetSalary.forceTwoDecimalsPlaces()}$currencySymbol"
            )

            TabbedDataOverview(
                label = stringResource(id = R.string.breakdown_salary_food_card_monthly),
                text = "${netSalaryUi.monthlyFoodCard.forceTwoDecimalsPlaces()}$currencySymbol"
            )

            if (!netSalaryUi.moneyAllocations.isNullOrEmpty()) {
                Text(
                    modifier = Modifier.padding(top = DefaultPadding, bottom = QuarterPadding),
                    text = stringResource(id = R.string.breakdown_salary_card_allocations),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.tertiary
                )

                netSalaryUi.moneyAllocations.forEach { moneyAllocationUi ->
                    Row {
                        Icon(
                            painter = painterResource(id = moneyAllocationUi.iconRes),
                            modifier = Modifier
                                .size(16.dp)
                                .align(Alignment.CenterVertically),
                            contentDescription = null
                        )

                        Spacer(modifier = Modifier.width(HalfPadding))

                        TabbedDataOverview(
                            modifier = Modifier.weight(1f),
                            label = stringResource(id = moneyAllocationUi.labelRes),
                            text = "${moneyAllocationUi.allocated.forceTwoDecimalsPlaces()}$currencySymbol"
                        )
                    }
                }
            }

            MovementSetting(
                modifier = Modifier.padding(top = DefaultPadding),
                label = stringResource(id = R.string.breakdown_tap_to_view_more)
            )
        }
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
            navController.navigateToWithArgs(OverviewNavigationItem.Loan.Edit, -1L)
        }
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_add),
            contentDescription = stringResource(
                id = R.string.tools_shopping_list
            )
        )
    }
}

@Composable
private fun ShowDeleteConfirmationDialog(
    loanUi: LoanUi?,
    loanToDelete: MutableLongState,
    onDeleteLoan: (LoanUi) -> Unit
) {
    if (loanToDelete.longValue >= 0L && loanUi != null) {
        AlertDialog(
            onDismissRequest = { loanToDelete.longValue = -1L },
            confirmButton = {
                LumbridgeButton(
                    label = stringResource(id = R.string.yes),
                    onClick = {
                        onDeleteLoan(loanUi)
                        loanToDelete.longValue = -1L
                    }
                )
            },
            dismissButton = {
                LumbridgeButton(
                    label = stringResource(id = R.string.no),
                    onClick = { loanToDelete.longValue = -1L }
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
                    text = stringResource(id = R.string.breakdown_delete_loan_confirmation_message, loanUi.name),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        )
    }
}
