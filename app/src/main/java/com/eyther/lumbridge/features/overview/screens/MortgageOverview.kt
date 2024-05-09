package com.eyther.lumbridge.features.overview.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.eyther.lumbridge.R
import com.eyther.lumbridge.features.overview.components.DataOverview
import com.eyther.lumbridge.features.overview.model.FinancialOverviewScreenViewState
import com.eyther.lumbridge.features.overview.navigation.FinancialOverviewNavigationItem
import com.eyther.lumbridge.model.mortgage.MortgageTypeUi
import com.eyther.lumbridge.ui.common.composables.components.defaults.EmptyScreenWithButton
import com.eyther.lumbridge.ui.navigation.NavigationItem
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.HalfPadding
import com.eyther.lumbridge.ui.theme.QuarterPadding
import com.eyther.lumbridge.ui.theme.runescapeTypography


@Composable
fun ColumnScope.MortgageOverview(
    navController: NavHostController,
    state: FinancialOverviewScreenViewState.Content,
    navigate: (NavigationItem, NavHostController) -> Unit,
    currencySymbol: String
) {
    if (state.mortgage == null) {
        EmptyScreenWithButton(
            modifier = Modifier
                .padding(DefaultPadding)
                .weight(1f),
            text = stringResource(id = R.string.financial_overview_no_mortgage_profile),
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
                navigate(
                    FinancialOverviewNavigationItem.EditMortgageProfile,
                    navController
                )
            }
        )
    } else {
        PaymentOverview(
            state = state,
            currencySymbol = currencySymbol,
            onNavigate = navigate,
            navController = navController
        )
    }
}

@Composable
private fun ColumnScope.PaymentOverview(
    state: FinancialOverviewScreenViewState.Content,
    currencySymbol: String,
    onNavigate: (FinancialOverviewNavigationItem, NavHostController) -> Unit,
    navController: NavHostController
) {
    // Mortgage cannot be null in the content state.
    checkNotNull(state.mortgage)

    Text(
        modifier = Modifier
            .padding(
                top = DefaultPadding,
                bottom = HalfPadding,
                start = DefaultPadding,
                end = DefaultPadding
            )
            .align(Alignment.Start),
        text = stringResource(id = R.string.financial_overview_mortgage),
        style = runescapeTypography.bodyLarge
    )

    Row(
        modifier = Modifier
            .padding(horizontal = DefaultPadding)
            .clip(RoundedCornerShape(8.dp))
            .shadow(elevation = QuarterPadding)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .fillMaxWidth()
            .padding(DefaultPadding),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {

            Text(
                modifier = Modifier.padding(
                    bottom = QuarterPadding
                ),
                text = stringResource(id = R.string.financial_overview_mortgage_mortgage_amount),
                style = runescapeTypography.bodyLarge,
                color = MaterialTheme.colorScheme.tertiary
            )

            DataOverview(
                label = stringResource(id = R.string.loan_amount),
                text = "${state.mortgage.loanAmount}$currencySymbol"
            )

            DataOverview(
                label = stringResource(id = R.string.months_left),
                text = "${state.mortgage.monthsLeft}"
            )

            DataOverview(
                label = stringResource(id = R.string.financial_overview_mortgage_next_payment),
                text = "${state.mortgage.monthlyPayment}$currencySymbol"
            )

            Text(
                modifier = Modifier.padding(
                    top = DefaultPadding,
                    bottom = QuarterPadding
                ),
                text = stringResource(id = R.string.remaining),
                style = runescapeTypography.bodyLarge,
                color = MaterialTheme.colorScheme.tertiary
            )

            DataOverview(
                label = stringResource(id = R.string.financial_overview_mortgage_remaining_amount),
                text = "${state.mortgage.remainingAmount}$currencySymbol"
            )

            DataOverview(
                label = stringResource(id = R.string.financial_overview_mortgage_total_paid),
                text = "${state.mortgage.totalPaid}$currencySymbol"
            )

            Text(
                modifier = Modifier.padding(
                    top = DefaultPadding,
                    bottom = QuarterPadding
                ),
                text = stringResource(id = R.string.interest_rate),
                style = runescapeTypography.bodyLarge,
                color = MaterialTheme.colorScheme.tertiary
            )

            when (state.mortgage.mortgageTypeUi) {
                MortgageTypeUi.Fixed -> {
                    DataOverview(
                        label = stringResource(id = R.string.interest_rate),
                        text = "${state.mortgage.fixedInterestRate}%"
                    )
                }

                MortgageTypeUi.Variable -> {
                    DataOverview(
                        label = stringResource(id = R.string.euribor),
                        text = "${state.mortgage.euribor}%"
                    )

                    DataOverview(
                        label = stringResource(id = R.string.spread),
                        text = "${state.mortgage.spread}%"
                    )
                }
            }
        }

        Icon(
            modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(bounded = false)
                ) {
                    onNavigate(
                        FinancialOverviewNavigationItem.EditMortgageProfile,
                        navController
                    )
                },
            painter = painterResource(id = R.drawable.ic_edit),
            contentDescription = stringResource(id = R.string.edit_financial_profile)
        )
    }

}
