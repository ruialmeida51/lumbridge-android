package com.eyther.lumbridge.features.overview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.eyther.lumbridge.R
import com.eyther.lumbridge.extensions.kotlin.forceTwoDecimalsPlaces
import com.eyther.lumbridge.model.loan.LoanAmortizationUi
import com.eyther.lumbridge.ui.common.composables.components.buttons.LumbridgeButton
import com.eyther.lumbridge.ui.common.composables.components.defaults.EmptyScreenWithButton
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.HalfPadding
import com.eyther.lumbridge.ui.theme.SmallButtonHeight

@Composable
fun ColumnScope.MortgageOverview(
    navController: NavHostController,
    //state: FinancialOverviewScreenViewState.Content,
    onPayment: () -> Unit,
    currencySymbol: String
) {
    val shouldShowPaymentConfirmationDialog = remember { mutableStateOf(false) }

    //if (state.loan == null) {
        EmptyScreenWithButton(
            modifier = Modifier
                .padding(DefaultPadding)
                .weight(1f),
            text = stringResource(id = R.string.financial_overview_no_loan),
            buttonText = stringResource(id = R.string.financial_overview_create_mortgage_profile),
            icon = {
                Icon(
                    modifier = Modifier
                        .size(32.dp)
                        .align(Alignment.CenterHorizontally),
                    painter = painterResource(id = R.drawable.ic_bank),
                    contentDescription = stringResource(id = R.string.financial_overview_create_mortgage_profile)
                )
            },
            onButtonClick = {
                //navController.navigate(FinancialOverviewNavigationItem.EditLoans)
            }
        )
    //} else {
        Column {
            PaymentOverview(
                //state = state,
                currencySymbol = currencySymbol,
                navController = navController
            )

            Spacer(
                modifier = Modifier.height(DefaultPadding)
            )

            LumbridgeButton(
                modifier = Modifier.padding(horizontal = DefaultPadding),
                minHeight = SmallButtonHeight,
                label = stringResource(R.string.financial_overview_mortgage_pay_a_month),
                onClick = { shouldShowPaymentConfirmationDialog.value = true }
            )

            Spacer(
                modifier = Modifier.height(DefaultPadding)
            )

            Amortizations(
                //state = state,
                currencySymbol = currencySymbol
            )

            Spacer(
                modifier = Modifier.height(DefaultPadding)
            )
        }
    }

    //PaymentConfirmationDialog(shouldShowPaymentConfirmationDialog, onPayment)
//}

@Composable
private fun ColumnScope.PaymentOverview(
   // state: FinancialOverviewScreenViewState.Content,
    currencySymbol: String,
    navController: NavHostController
) {
    // Mortgage cannot be null in the content state.
    //checkNotNull(state.loan)

    Text(
        modifier = Modifier
            .padding(start = DefaultPadding, end = DefaultPadding, bottom = HalfPadding)
            .align(Alignment.Start),
        text = stringResource(id = R.string.financial_overview_loans),
        style = MaterialTheme.typography.bodyLarge
    )

//    ColumnCardWrapper {
//        Row {
//            Text(
//                modifier = Modifier.padding(bottom = QuarterPadding),
//                text = stringResource(id = R.string.financial_overview_remaining_loan_amount),
//                style = MaterialTheme.typography.bodyLarge,
//                color = MaterialTheme.colorScheme.tertiary
//            )
//
//            Spacer(modifier = Modifier.weight(1f))
//
//            Icon(
//                modifier = Modifier
//                    .size(20.dp)
//                    .clickable(
//                        interactionSource = remember { MutableInteractionSource() },
//                        indication = rememberRipple(bounded = false)
//                    ) {
//                        navController.navigate(FinancialOverviewNavigationItem.EditLoans)
//                    },
//                painter = painterResource(id = R.drawable.ic_edit),
//                contentDescription = stringResource(id = R.string.edit_financial_profile)
//            )
//        }
//
//        TabbedDataOverview(
//            label = stringResource(id = R.string.loan_amount),
//            text = "${state.loan.loanCalculationUi.loanAmount.forceTwoDecimalsPlaces()}$currencySymbol"
//        )
//
//        TabbedDataOverview(
//            label = stringResource(id = R.string.months_left),
//            text = "${state.loan.loanCalculationUi.remainingMonths}"
//        )
//
//        when (val loanInterestRate = state.loan.loanCalculationUi.loanInterestRateUi) {
//            is LoanInterestRateUi.Fixed.Tan -> {
//                TabbedDataOverview(
//                    label = stringResource(id = state.loan.loanCalculationUi.loanInterestRateUi.label),
//                    text = "${loanInterestRate.interestRate}%"
//                )
//            }
//
//            is LoanInterestRateUi.Fixed.Taeg -> {
//                TabbedDataOverview(
//                    label = stringResource(id = state.loan.loanCalculationUi.loanInterestRateUi.label),
//                    text = "${loanInterestRate.interestRate}%"
//                )
//            }
//
//            is LoanInterestRateUi.Variable -> {
//                TabbedDataOverview(
//                    label = stringResource(id = R.string.euribor),
//                    text = "${loanInterestRate.euribor}%"
//                )
//
//                TabbedDataOverview(
//                    label = stringResource(id = R.string.spread),
//                    text = "${loanInterestRate.spread}%"
//                )
//            }
//        }
//
//        Text(
//            modifier = Modifier.padding(
//                top = DefaultPadding,
//                bottom = QuarterPadding
//            ),
//            text = stringResource(id = R.string.financial_overview_mortgage_monthly_payment),
//            style = MaterialTheme.typography.bodyLarge,
//            color = MaterialTheme.colorScheme.tertiary
//        )
//
//        TabbedDataOverview(
//            label = stringResource(id = R.string.financial_overview_loan_next_payment),
//            text = "${state.loan.loanCalculationUi.monthlyPayment.forceTwoDecimalsPlaces()}$currencySymbol"
//        )
//
//        TabbedDataOverview(
//            label = stringResource(id = R.string.financial_overview_loan_next_payment_capital),
//            text = "${state.loan.loanCalculationUi.monthlyPaymentCapital.forceTwoDecimalsPlaces()}$currencySymbol"
//        )
//
//        TabbedDataOverview(
//            label = stringResource(id = R.string.financial_overview_loan_next_payment_interest),
//            text = "${state.loan.loanCalculationUi.monthlyPaymentInterest.forceTwoDecimalsPlaces()}$currencySymbol"
//        )
    }

@Composable
private fun ColumnScope.Amortizations(
    //state: FinancialOverviewScreenViewState.Content,
    currencySymbol: String
) {
    // Mortgage cannot be null in the content state.
//    checkNotNull(state.loan)
//
//    Text(
//        modifier = Modifier
//            .padding(start = DefaultPadding, end = DefaultPadding, bottom = HalfPadding)
//            .align(Alignment.Start),
//        text = stringResource(id = R.string.financial_overview_mortgage_amortization_simulator),
//        style = MaterialTheme.typography.bodyLarge
//    )
//
//    RowCardWrapper {
//        Column {
//            if (state.loan.loanCalculationUi.amortizationUi.isEmpty()) {
//                Text(
//                    text = stringResource(id = R.string.financial_overview_mortgage_payment_almost_complete),
//                    style = MaterialTheme.typography.bodyMedium,
//                    textAlign = TextAlign.Start,
//                    modifier = Modifier.align(Alignment.Start)
//                )
//            } else {
//                AmortizationsTable(
//                    currencySymbol = currencySymbol,
//                    amortizations = state.loan.loanCalculationUi.amortizationUi
//                )
//            }
//        }
//    }
}

@Composable
private fun AmortizationsTable(
    currencySymbol: String,
    amortizations: List<LoanAmortizationUi>
) {
    if (amortizations.isEmpty()) return

    val nColumns = amortizations.first().numberOfElements
    val labels = listOf(
        stringResource(id = R.string.remainder),
        stringResource(id = R.string.amortization),
        stringResource(id = R.string.financial_overview_loan_next_payment)
    )

    Column {
        Row {
            labels.forEachIndexed { _, label ->
                TableItem(
                    content = {
                        Text(
                            textAlign = TextAlign.Center,
                            text = label,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                )
            }
        }
    }

    amortizations.forEach { amortization ->
        Row {
            repeat(nColumns) {
                val text = when (it) {
                    0 -> amortization.remainder.forceTwoDecimalsPlaces()
                    1 -> amortization.amortization.forceTwoDecimalsPlaces()
                    2 -> amortization.nextPayment.forceTwoDecimalsPlaces()
                    else -> throw IllegalArgumentException("💥 Column index out of bounds")
                }

                TableItem(
                    content = {
                        Text(
                            textAlign = TextAlign.Center,
                            text = text.plus(currencySymbol),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun RowScope.TableItem(
    content: @Composable BoxScope.() -> Unit
) {
    val strokeWidth = 1.dp
    val color = MaterialTheme.colorScheme.onSurface

    Box(
        modifier = Modifier
            .drawBehind {
                val strokePx = strokeWidth.toPx()
                drawLine(
                    color = color,
                    start = Offset.Zero,
                    end = Offset(size.width, 0f),
                    strokeWidth = strokePx
                )
                drawLine(
                    color = color,
                    start = Offset.Zero,
                    end = Offset(0f, size.height),
                    strokeWidth = strokePx
                )
                drawLine(
                    color = color,
                    start = Offset(size.width, 0f),
                    end = Offset(size.width, size.height),
                    strokeWidth = strokePx
                )
                drawLine(
                    color = color,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = strokePx
                )
            }
            .heightIn(min = 50.dp)
            .weight(1f),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
fun PaymentConfirmationDialog(
    shouldShowDialog: MutableState<Boolean>,
    onPayment: () -> Unit
) {
    if (shouldShowDialog.value) {
        AlertDialog(
            onDismissRequest = { shouldShowDialog.value = false },
            confirmButton = {
                LumbridgeButton(
                    label = stringResource(id = R.string.register),
                    onClick = {
                        onPayment()
                        shouldShowDialog.value = false
                    }
                )
            },
            dismissButton = {
                LumbridgeButton(
                    label = stringResource(id = R.string.cancel),
                    onClick = { shouldShowDialog.value = false }
                )
            },
            title = {
                Text(
                    text = stringResource(id = R.string.financial_overview_loan_payment_dialog_confirmation_title),
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            text = {
                Text(
                    text = stringResource(id = R.string.financial_overview_loan_payment_dialog_confirmation_message),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        )
    }
}
