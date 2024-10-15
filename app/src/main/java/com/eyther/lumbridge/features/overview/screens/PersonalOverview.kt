package com.eyther.lumbridge.features.overview.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.eyther.lumbridge.R
import com.eyther.lumbridge.extensions.kotlin.forceTwoDecimalsPlaces
import com.eyther.lumbridge.extensions.platform.navigate
import com.eyther.lumbridge.features.overview.components.IncomeOverview
import com.eyther.lumbridge.features.overview.components.PerCountryBreakdown
import com.eyther.lumbridge.features.overview.components.TabbedDataOverview
import com.eyther.lumbridge.features.overview.model.FinancialOverviewScreenViewState
import com.eyther.lumbridge.features.overview.navigation.FinancialOverviewNavigationItem
import com.eyther.lumbridge.model.finance.MoneyAllocationUi
import com.eyther.lumbridge.ui.common.composables.components.card.ColumnCardWrapper
import com.eyther.lumbridge.ui.common.composables.components.defaults.EmptyScreenWithButton
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.HalfPadding
import com.eyther.lumbridge.ui.theme.QuarterPadding

@Composable
fun ColumnScope.PersonalOverview(
    navController: NavHostController,
    state: FinancialOverviewScreenViewState.Content,
    currencySymbol: String
) {
    if (state.netSalary == null) {
        EmptyScreenWithButton(
            modifier = Modifier
                .padding(DefaultPadding)
                .weight(1f),
            text = stringResource(id = R.string.financial_overview_no_profile),
            buttonText = stringResource(id = R.string.financial_overview_create_profile),
            icon = {
                Icon(
                    modifier = Modifier
                        .size(32.dp)
                        .align(Alignment.CenterHorizontally),
                    painter = painterResource(id = R.drawable.ic_savings),
                    contentDescription = stringResource(id = R.string.financial_overview_create_profile)
                )
            },
            onButtonClick = {
                navController.navigate(FinancialOverviewNavigationItem.EditFinancialProfile)
            }
        )
    } else {
        Column {
            IncomeOverview(
                netSalaryUi = state.netSalary,
                currencySymbol = currencySymbol,
                onEditClick = {
                    navController.navigate(FinancialOverviewNavigationItem.EditFinancialProfile)
                }
            )

            Spacer(
                modifier = Modifier.height(DefaultPadding)
            )

            PerCountryBreakdown(
                netSalaryUi = state.netSalary,
                locale = state.locale,
                currencySymbol = currencySymbol
            )

            Spacer(
                modifier = Modifier.height(DefaultPadding)
            )

            MoneyAllocationBreakdown(
                currencySymbol = currencySymbol,
                navController = navController,
                moneyAllocation = state.netSalary.moneyAllocations
            )

            Spacer(
                modifier = Modifier.height(DefaultPadding)
            )
        }
    }
}

@Composable
private fun ColumnScope.MoneyAllocationBreakdown(
    currencySymbol: String,
    navController: NavHostController,
    moneyAllocation: List<MoneyAllocationUi>?
) {
    Text(
        modifier = Modifier
            .padding(start = DefaultPadding, end = DefaultPadding, bottom = HalfPadding)
            .align(Alignment.Start),
        text = stringResource(id = R.string.financial_overview_allocation),
        style = MaterialTheme.typography.bodyLarge
    )

    ColumnCardWrapper {
        Row {
            Text(
                modifier = Modifier.padding(bottom = QuarterPadding),
                text = stringResource(id = R.string.financial_overview_allocation_breakdown),
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
                    ) {
                        navController.navigate(FinancialOverviewNavigationItem.EditFinancialProfile)
                    },
                painter = painterResource(id = R.drawable.ic_edit),
                contentDescription = stringResource(id = R.string.edit_financial_profile)
            )
        }

        if (moneyAllocation == null) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = stringResource(id = R.string.financial_overview_no_allocation_strategy),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.align(Alignment.Start)
                )
            }
        } else {
            moneyAllocation.forEach { moneyAllocationUi ->
                TabbedDataOverview(
                    label = stringResource(id = moneyAllocationUi.label),
                    text = "${moneyAllocationUi.amount.forceTwoDecimalsPlaces()}$currencySymbol"
                )
            }
        }
    }
}
