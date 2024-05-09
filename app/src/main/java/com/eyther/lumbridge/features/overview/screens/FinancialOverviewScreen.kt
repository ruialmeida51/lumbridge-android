package com.eyther.lumbridge.features.overview.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.eyther.lumbridge.R
import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.features.overview.components.Salary
import com.eyther.lumbridge.features.overview.model.FinancialOverviewScreenViewState
import com.eyther.lumbridge.features.overview.navigation.FinancialOverviewNavigationItem
import com.eyther.lumbridge.features.overview.screens.portugal.FinancialOverviewPortugal
import com.eyther.lumbridge.features.overview.viewmodel.FinancialOverviewScreenViewModel
import com.eyther.lumbridge.features.overview.viewmodel.IFinancialOverviewScreenViewModel
import com.eyther.lumbridge.model.finance.DeductionUi
import com.eyther.lumbridge.model.finance.MoneyAllocationUi
import com.eyther.lumbridge.model.finance.NetSalaryUi
import com.eyther.lumbridge.ui.common.composables.components.defaults.EmptyScreenWithButton
import com.eyther.lumbridge.ui.common.composables.components.loading.LoadingIndicator
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.LumbridgeTopAppBar
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.TopAppBarVariation
import com.eyther.lumbridge.ui.navigation.NavigationItem
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.HalfPadding
import com.eyther.lumbridge.ui.theme.LumbridgeTheme
import com.eyther.lumbridge.ui.theme.QuarterPadding
import com.eyther.lumbridge.ui.theme.runescapeTypography

@Composable
fun FinancialOverviewScreen(
    navController: NavHostController,
    @StringRes label: Int,
    viewModel: IFinancialOverviewScreenViewModel = hiltViewModel<FinancialOverviewScreenViewModel>()
) {
    val state = viewModel.viewState.collectAsState().value

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
                .verticalScroll(rememberScrollState())
                .height(IntrinsicSize.Max)
        ) {
            when (state) {
                is FinancialOverviewScreenViewState.Content.Overview -> Overview(
                    navController = navController,
                    state = state,
                    navigate = viewModel::navigate
                )

                is FinancialOverviewScreenViewState.Content.Input -> EmptyScreenWithButton(
                    modifier = Modifier.padding(DefaultPadding),
                    text = stringResource(id = R.string.financial_overview_no_profile),
                    buttonText = stringResource(id = R.string.edit_financial_profile),
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
                        viewModel.navigate(
                            FinancialOverviewNavigationItem.EditFinancialProfile,
                            navController
                        )
                    }
                )

                is FinancialOverviewScreenViewState.Loading -> LoadingIndicator()
            }
        }
    }
}

@Composable
private fun ColumnScope.Overview(
    navController: NavHostController,
    state: FinancialOverviewScreenViewState.Content.Overview,
    navigate: (NavigationItem, NavHostController) -> Unit
) {
    val currencySymbol = remember { state.locale.getCurrencySymbol() }

    IncomeOverview(
        state = state,
        navController = navController,
        onNavigate = navigate,
        currencySymbol = currencySymbol
    )

    PerCountryBreakdown(state)

    MoneyAllocationBreakdown(
        currencySymbol = currencySymbol,
        onNavigate = navigate,
        navController = navController,
        moneyAllocation = state.netSalary.moneyAllocations
    )

    Spacer(
        modifier = Modifier.height(DefaultPadding)
    )

    Spacer(modifier = Modifier.padding(DefaultPadding))
}

@Composable
private fun ColumnScope.IncomeOverview(
    state: FinancialOverviewScreenViewState.Content.Overview,
    navController: NavHostController,
    onNavigate: (NavigationItem, NavHostController) -> Unit,
    currencySymbol: String
) {
    Text(
        modifier = Modifier
            .padding(
                top = DefaultPadding,
                bottom = HalfPadding,
                start = DefaultPadding,
                end = DefaultPadding
            )
            .align(Alignment.Start),
        text = stringResource(id = R.string.financial_overview_salary),
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
                modifier = Modifier.padding(bottom = QuarterPadding),
                text = stringResource(id = R.string.financial_overview_annual_income),
                style = runescapeTypography.bodyLarge,
                color = MaterialTheme.colorScheme.tertiary
            )

            Salary(
                leftLabel = stringResource(id = R.string.net_annual).plus(": "),
                leftText = "${state.netSalary.annualNetSalary}$currencySymbol",
                rightLabel = stringResource(id = R.string.gross_annual).plus(": "),
                rightText = "${state.netSalary.annualGrossSalary}$currencySymbol"
            )

            Text(
                modifier = Modifier.padding(top = DefaultPadding, bottom = QuarterPadding),
                text = stringResource(id = R.string.financial_overview_monthly_income),
                style = runescapeTypography.bodyLarge,
                color = MaterialTheme.colorScheme.tertiary
            )

            Salary(
                leftLabel = stringResource(id = R.string.net_monthly).plus(": "),
                leftText = "${state.netSalary.monthlyNetSalary}$currencySymbol",
                rightLabel = stringResource(id = R.string.gross_monthly).plus(": "),
                rightText = "${state.netSalary.monthlyGrossSalary}$currencySymbol"
            )

            Text(
                modifier = Modifier.padding(top = DefaultPadding, bottom = QuarterPadding),
                text = stringResource(id = R.string.food_card),
                style = runescapeTypography.bodyLarge,
                color = MaterialTheme.colorScheme.tertiary
            )

            Salary(
                leftLabel = stringResource(id = R.string.financial_overview_food_card_monthly).plus(": "),
                leftText = "${state.netSalary.monthlyFoodCard}$currencySymbol",
                rightLabel = stringResource(id = R.string.financial_overview_food_card_daily).plus(": "),
                rightText = "${state.netSalary.dailyFoodCard}$currencySymbol"
            )
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

@Composable
private fun ColumnScope.PerCountryBreakdown(
    state: FinancialOverviewScreenViewState.Content.Overview
) {
    Text(
        modifier = Modifier
            .padding(
                start = DefaultPadding,
                end = DefaultPadding,
                top = DefaultPadding,
                bottom = HalfPadding
            )
            .align(Alignment.Start),
        text = stringResource(id = R.string.financial_overview_deductions),
        style = runescapeTypography.bodyLarge
    )

    when (state.locale) {
        SupportedLocales.PORTUGAL -> FinancialOverviewPortugal(
            state = state
        )
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
                start = DefaultPadding,
                end = DefaultPadding,
                top = DefaultPadding,
                bottom = HalfPadding
            )
            .align(Alignment.Start),
        text = stringResource(id = R.string.financial_overview_allocation),
        style = runescapeTypography.bodyLarge
    )


    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(horizontal = DefaultPadding)
            .clip(RoundedCornerShape(8.dp))
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
                        style = runescapeTypography.bodyMedium,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.align(Alignment.Start)
                    )
                }
            } else {
                moneyAllocation.forEach { moneyAllocationUi ->
                    Salary(
                        leftLabel = stringResource(id = moneyAllocationUi.label).plus(": "),
                        leftText = "${moneyAllocationUi.amount}$currencySymbol"
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
