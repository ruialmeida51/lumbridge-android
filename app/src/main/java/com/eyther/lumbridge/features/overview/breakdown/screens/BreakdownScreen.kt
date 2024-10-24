package com.eyther.lumbridge.features.overview.breakdown.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.ripple.rememberRipple
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
import com.eyther.lumbridge.extensions.platform.navigateWithArgs
import com.eyther.lumbridge.features.overview.breakdown.model.BreakdownScreenViewState
import com.eyther.lumbridge.features.overview.breakdown.viewmodel.BreakdownScreenViewModel
import com.eyther.lumbridge.features.overview.breakdown.viewmodel.IBreakdownScreenViewModel
import com.eyther.lumbridge.features.overview.navigation.OverviewNavigationItem
import com.eyther.lumbridge.model.finance.NetSalaryUi
import com.eyther.lumbridge.model.loan.LoanCalculationUi
import com.eyther.lumbridge.model.loan.LoanInterestRateUi
import com.eyther.lumbridge.model.loan.LoanUi
import com.eyther.lumbridge.ui.common.composables.components.buttons.LumbridgeButton
import com.eyther.lumbridge.ui.common.composables.components.card.ColumnCardWrapper
import com.eyther.lumbridge.ui.common.composables.components.defaults.EmptyComponentWithButton
import com.eyther.lumbridge.ui.common.composables.components.loading.LoadingIndicator
import com.eyther.lumbridge.ui.common.composables.components.setting.MovementSetting
import com.eyther.lumbridge.ui.common.composables.components.text.TabbedDataOverview
import com.eyther.lumbridge.ui.common.composables.components.text.TabbedTextAndIcon
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
                is BreakdownScreenViewState.Content ->
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(top = DefaultPadding)
                                .verticalScroll(rememberScrollState())
                        ) {
                            Content(
                                state = state,
                                navController = navController,
                                loanToDelete = loanToDelete,
                                onDeleteLoan = viewModel::onDeleteLoan
                            )

                            if (state.loans.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(DefaultPadding * 2 + 56.dp)) // 56dp is the height of the FAB
                            }
                        }

                        if (state.loans.isNotEmpty()) {
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
private fun ColumnScope.Content(
    state: BreakdownScreenViewState.Content,
    navController: NavHostController,
    loanToDelete: MutableLongState,
    onDeleteLoan: (LoanUi) -> Unit
) {
    if (state.netSalary == null) {
        ColumnCardWrapper {
            EmptyComponentWithButton(
                text = stringResource(id = R.string.breakdown_salary_no_financial_profile),
                buttonText = stringResource(id = R.string.breakdown_salary_no_financial_profile_create),
                onButtonClick = { navController.navigate(OverviewNavigationItem.FinancialProfile.Edit.route) }
            )
        }
    } else {
        SalaryOverview(
            netSalaryUi = state.netSalary,
            currencySymbol = state.currencySymbol,
            onCardClick = { navController.navigate(OverviewNavigationItem.FinancialProfile.Details.route) }
        )
    }

    Spacer(modifier = Modifier.height(DefaultPadding))

    if (state.loans.isEmpty()) {
        ColumnCardWrapper {
            EmptyComponentWithButton(
                text = stringResource(id = R.string.breakdown_loans_no_loans),
                buttonText = stringResource(id = R.string.breakdown_loans_no_loans_create),
                onButtonClick = { navController.navigateWithArgs(OverviewNavigationItem.Loan.Edit, -1L) }
            )
        }
    } else {
        LoansOverview(
            loansUi = state.loans,
            currencySymbol = state.currencySymbol,
            onCardClick = { loanId -> navController.navigateWithArgs(OverviewNavigationItem.Loan.Details, loanId) },
            loanToDelete = loanToDelete,
            onDeleteLoan = onDeleteLoan
        )
    }
}

@Composable
private fun SalaryOverview(
    netSalaryUi: NetSalaryUi,
    currencySymbol: String,
    onCardClick: () -> Unit
) {
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

@Composable
private fun ColumnScope.LoansOverview(
    loansUi: List<Pair<LoanUi, LoanCalculationUi>>,
    loanToDelete: MutableLongState,
    currencySymbol: String,
    onCardClick: (loanId: Long) -> Unit,
    onDeleteLoan: (LoanUi) -> Unit
) {
    Text(
        modifier = Modifier
            .padding(start = DefaultPadding, end = DefaultPadding, bottom = HalfPadding)
            .align(Alignment.Start),
        text = stringResource(id = R.string.breakdown_loans_title),
        style = MaterialTheme.typography.bodyLarge
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(DefaultPadding)
    ) {
        loansUi.forEach { (loanUi, loanCalculationUi) ->
            ColumnCardWrapper(
                onClick = { onCardClick(loanUi.id) }
            ) {
                Row(
                    modifier = Modifier.padding(bottom = HalfPadding)
                ) {
                    Icon(
                        modifier = Modifier
                            .size(20.dp)
                            .align(Alignment.CenterVertically),
                        painter = painterResource(id = loanUi.loanCategoryUi.icon),
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(QuarterPadding))

                    TabbedTextAndIcon(
                        modifier = Modifier.padding(end = QuarterPadding),
                        text = loanUi.name,
                        textStyle = MaterialTheme.typography.bodyLarge,
                        textColour = MaterialTheme.colorScheme.tertiary,
                        icons = {
                            Icon(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = rememberRipple(bounded = false),
                                        onClick = { loanToDelete.longValue = loanUi.id }
                                    ),
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = stringResource(id = R.string.delete)
                            )
                        }
                    )
                }

                TabbedDataOverview(
                    label = stringResource(id = R.string.breakdown_loans_next_payment),
                    text = "${loanCalculationUi.monthlyPayment.forceTwoDecimalsPlaces()}$currencySymbol",
                )

                TabbedDataOverview(
                    label = stringResource(id = R.string.breakdown_loans_remaining_amount),
                    text = "${loanUi.loanAmount.forceTwoDecimalsPlaces()}$currencySymbol",
                )

                TabbedDataOverview(
                    label = stringResource(id = R.string.breakdown_loans_months_left),
                    text = loanCalculationUi.remainingMonths.toString(),
                )

                Text(
                    modifier = Modifier.padding(top = DefaultPadding, bottom = QuarterPadding),
                    text = stringResource(id = R.string.breakdown_loans_interest_rate),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.tertiary
                )

                when (val loanInterestRateUi = loanUi.loanInterestRateUi) {
                    is LoanInterestRateUi.Fixed -> {
                        TabbedDataOverview(
                            label = stringResource(id = loanInterestRateUi.label),
                            text = "${loanInterestRateUi.interestRate}%"
                        )
                    }

                    is LoanInterestRateUi.Variable -> {
                        TabbedDataOverview(
                            label = stringResource(id = R.string.euribor),
                            text = "${loanInterestRateUi.euribor}%"
                        )

                        TabbedDataOverview(
                            label = stringResource(id = R.string.spread),
                            text = "${loanInterestRateUi.spread}%"
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

   ShowDeleteConfirmationDialog(
        loanUi = loansUi.find { it.first.id == loanToDelete.longValue }?.first,
        loanToDelete = loanToDelete,
        onDeleteLoan = onDeleteLoan
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
            navController.navigateWithArgs(OverviewNavigationItem.Loan.Edit, -1L)
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
