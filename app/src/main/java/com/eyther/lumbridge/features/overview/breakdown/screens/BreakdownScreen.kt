package com.eyther.lumbridge.features.overview.breakdown.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.eyther.lumbridge.R
import com.eyther.lumbridge.extensions.kotlin.forceTwoDecimalsPlaces
import com.eyther.lumbridge.extensions.platform.navigateTo
import com.eyther.lumbridge.extensions.platform.navigateToWithArgs
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
            SalaryOverview(
                netSalaryUi = state.netSalary,
                currencySymbol = state.currencySymbol,
                onCardClick = { navController.navigateTo(OverviewNavigationItem.FinancialProfile.Details) },
                onCreateFinancialProfile = { navController.navigateTo(OverviewNavigationItem.FinancialProfile.Edit) }
            )

            Spacer(modifier = Modifier.height(DefaultPadding))
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
        } else {
            item {
                Text(
                    modifier = Modifier
                        .padding(start = DefaultPadding, end = DefaultPadding, bottom = HalfPadding),
                    text = stringResource(id = R.string.breakdown_loans_title),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

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
private fun SalaryOverview(
    netSalaryUi: NetSalaryUi?,
    currencySymbol: String,
    onCardClick: () -> Unit,
    onCreateFinancialProfile: () -> Unit
) {
    if (netSalaryUi == null) {
        ColumnCardWrapper {
            EmptyComponentWithButton(
                text = stringResource(id = R.string.breakdown_salary_no_financial_profile),
                buttonText = stringResource(id = R.string.breakdown_salary_no_financial_profile_create),
                onButtonClick = onCreateFinancialProfile
            )
        }
    } else {
        Text(
            modifier = Modifier
                .padding(start = DefaultPadding, end = DefaultPadding, bottom = HalfPadding),
            text = stringResource(id = R.string.breakdown_salary_title),
            style = MaterialTheme.typography.bodyLarge
        )

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
                    TabbedDataOverview(
                        label = stringResource(id = moneyAllocationUi.label),
                        text = "${moneyAllocationUi.amount.forceTwoDecimalsPlaces()}$currencySymbol"
                    )
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
