package com.eyther.lumbridge.features.overview.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.eyther.lumbridge.R
import com.eyther.lumbridge.extensions.kotlin.twoDecimalPlaces
import com.eyther.lumbridge.features.overview.components.DataOverview
import com.eyther.lumbridge.features.overview.components.IncomeOverview
import com.eyther.lumbridge.features.overview.components.PerCountryBreakdown
import com.eyther.lumbridge.features.overview.model.FinancialOverviewScreenViewState
import com.eyther.lumbridge.features.overview.navigation.FinancialOverviewNavigationItem
import com.eyther.lumbridge.model.finance.MoneyAllocationUi
import com.eyther.lumbridge.ui.common.composables.components.defaults.EmptyScreenWithButton
import com.eyther.lumbridge.ui.navigation.NavigationItem
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.DefaultRoundedCorner
import com.eyther.lumbridge.ui.theme.HalfPadding
import com.eyther.lumbridge.ui.theme.QuarterPadding

@Composable
fun ColumnScope.PersonalOverview(
    navController: NavHostController,
    state: FinancialOverviewScreenViewState.Content,
    navigate: (NavigationItem, NavHostController) -> Unit,
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
                navigate(
                    FinancialOverviewNavigationItem.EditFinancialProfile,
                    navController
                )
            }
        )
    } else {
        Column(
            modifier = Modifier.padding(horizontal = DefaultPadding)
        ) {
            IncomeOverview(
                netSalaryUi = state.netSalary,
                currencySymbol = currencySymbol,
                onEditClick = {
                    navigate(
                        FinancialOverviewNavigationItem.EditFinancialProfile,
                        navController
                    )
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
                onNavigate = navigate,
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
    onNavigate: (NavigationItem, NavHostController) -> Unit,
    navController: NavHostController,
    moneyAllocation: List<MoneyAllocationUi>?
) {
    Text(
        modifier = Modifier
            .padding(
                bottom = HalfPadding
            )
            .align(Alignment.Start),
        text = stringResource(id = R.string.financial_overview_allocation),
        style = MaterialTheme.typography.bodyLarge
    )

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .clip(RoundedCornerShape(DefaultRoundedCorner))
            .shadow(elevation = QuarterPadding)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .fillMaxWidth()
            .padding(DefaultPadding)
    ) {
        Column {
            if (moneyAllocation == null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Start),
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
                    DataOverview(
                        label = stringResource(id = moneyAllocationUi.label),
                        text = "${moneyAllocationUi.amount.twoDecimalPlaces()}$currencySymbol"
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
                        FinancialOverviewNavigationItem.EditFinancialProfile,
                        navController
                    )
                },
            painter = painterResource(id = R.drawable.ic_edit),
            contentDescription = stringResource(id = R.string.edit_financial_profile)
        )
    }
}
