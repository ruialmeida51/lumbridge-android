package com.eyther.lumbridge.features.overview.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.eyther.lumbridge.R
import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.features.overview.model.FinancialOverviewScreenViewState
import com.eyther.lumbridge.features.overview.navigation.FinancialOverviewNavigationItem
import com.eyther.lumbridge.features.overview.screens.portugal.FinancialOverviewPortugal
import com.eyther.lumbridge.features.overview.viewmodel.FinancialOverviewScreenViewModel
import com.eyther.lumbridge.features.overview.viewmodel.IFinancialOverviewScreenViewModel
import com.eyther.lumbridge.model.finance.MoneyAllocationUi
import com.eyther.lumbridge.ui.common.composables.components.defaults.EmptyScreenWithButton
import com.eyther.lumbridge.ui.common.composables.components.loading.LoadingIndicator
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.LumbridgeTopAppBar
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.TopAppBarVariation
import com.eyther.lumbridge.ui.common.composables.text.buildAnnotatedStringTextWithLabel
import com.eyther.lumbridge.ui.navigation.NavigationItem
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.runescapeTypography

@Composable
fun FinancialOverviewScreen(
    navController: NavHostController,
    label: String,
    viewModel: IFinancialOverviewScreenViewModel = hiltViewModel<FinancialOverviewScreenViewModel>()
) {
    val state = viewModel.viewState.collectAsState().value

    Scaffold(
        topBar = {
            LumbridgeTopAppBar(
                topAppBarVariation = TopAppBarVariation.Title(title = label)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = DefaultPadding)
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
                    text = "You don't seem have a financial profile yet.\n" +
                            "Press the button below to create one.",
                    buttonText = "Edit Financial Profile",
                    icon = {
                        Icon(
                            modifier = Modifier
                                .size(32.dp)
                                .align(Alignment.CenterHorizontally),
                            painter = painterResource(id = R.drawable.ic_savings),
                            contentDescription = "Create financial Profile"
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

    Spacer(modifier = Modifier.padding(DefaultPadding))

    Text(
        modifier = Modifier
            .padding(bottom = DefaultPadding)
            .align(Alignment.Start),
        text = "Salary Overview",
        style = runescapeTypography.titleMedium,
        color = MaterialTheme.colorScheme.onPrimary
    )

    Text(
        modifier = Modifier
            .padding(bottom = DefaultPadding)
            .align(Alignment.Start),
        text = buildAnnotatedStringTextWithLabel(
            label = "Gross Annual Salary: ",
            remainingText = "${state.annualGrossSalary}$currencySymbol"
        ),
        style = runescapeTypography.titleSmall
    )

    Text(
        modifier = Modifier
            .padding(bottom = DefaultPadding)
            .align(Alignment.Start),
        text = buildAnnotatedStringTextWithLabel(
            label = "Net Salary: ",
            remainingText = "${state.netSalary.salary}$currencySymbol"
        ),
        style = runescapeTypography.titleSmall
    )

    Text(
        modifier = Modifier
            .padding(bottom = DefaultPadding)
            .align(Alignment.Start),
        text = buildAnnotatedStringTextWithLabel(
            label = "Food Card: ",
            remainingText = "${state.netSalary.foodCard}$currencySymbol"
        ),
        style = runescapeTypography.titleSmall
    )

    Spacer(
        modifier = Modifier.height(DefaultPadding)
    )

    PerCountryBreakdown(state)

    Spacer(
        modifier = Modifier.height(DefaultPadding)
    )

    MoneyAllocationBreakdown(
        currencySymbol = currencySymbol,
        moneyAllocation = state.netSalary.moneyAllocations
    )

    Spacer(
        modifier = Modifier.height(DefaultPadding)
    )

    Button(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.Start),
        onClick = {
            navigate(
                FinancialOverviewNavigationItem.EditFinancialProfile,
                navController
            )
        }
    ) {
        Text(text = "Edit Financial Profile")
    }

    Spacer(modifier = Modifier.padding(DefaultPadding))
}

@Composable
private fun ColumnScope.PerCountryBreakdown(
    state: FinancialOverviewScreenViewState.Content.Overview
) {
    when (state.locale) {
        SupportedLocales.PORTUGAL -> FinancialOverviewPortugal(
            state = state
        )
    }
}

@Composable
private fun ColumnScope.MoneyAllocationBreakdown(
    currencySymbol: String,
    moneyAllocation: List<MoneyAllocationUi>?
) {
    Text(
        modifier = Modifier
            .padding(bottom = DefaultPadding)
            .align(Alignment.Start),
        text = "Money Allocation Strategy",
        style = runescapeTypography.titleMedium,
        color = MaterialTheme.colorScheme.onPrimary
    )

    if (moneyAllocation == null) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Start),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "You haven't set a money allocation strategy.\n" +
                        "Edit your financial profile to set one.",
                style = runescapeTypography.titleSmall,
                textAlign = TextAlign.Start,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.padding(DefaultPadding))
        }
    } else {
        moneyAllocation.forEach { moneyAllocationUi ->
            val text = buildAnnotatedStringTextWithLabel(
                label = "${moneyAllocationUi.label}: ",
                remainingText = "${moneyAllocationUi.amount}$currencySymbol"
            )

            Text(
                modifier = Modifier
                    .padding(bottom = DefaultPadding)
                    .align(Alignment.Start),
                text = text,
                style = runescapeTypography.titleSmall,
                textAlign = TextAlign.Start
            )
        }

    }
}
