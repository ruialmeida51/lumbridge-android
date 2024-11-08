package com.eyther.lumbridge.features.overview.financialprofiledetails.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.eyther.lumbridge.R
import com.eyther.lumbridge.extensions.kotlin.forceTwoDecimalsPlaces
import com.eyther.lumbridge.extensions.platform.navigateTo
import com.eyther.lumbridge.features.overview.financialprofiledetails.model.DetailsFinancialProfileScreenViewState
import com.eyther.lumbridge.features.overview.financialprofiledetails.viewmodel.DetailsFinancialProfileScreenViewModel
import com.eyther.lumbridge.features.overview.financialprofiledetails.viewmodel.IDetailsFinancialProfileScreenViewModel
import com.eyther.lumbridge.features.overview.navigation.OverviewNavigationItem
import com.eyther.lumbridge.features.overview.shared.components.IncomeOverview
import com.eyther.lumbridge.features.overview.shared.components.PerCountryBreakdown
import com.eyther.lumbridge.model.finance.MoneyAllocationUi
import com.eyther.lumbridge.ui.common.composables.components.card.ColumnCardWrapper
import com.eyther.lumbridge.ui.common.composables.components.defaults.EmptyScreenWithButton
import com.eyther.lumbridge.ui.common.composables.components.loading.LoadingIndicator
import com.eyther.lumbridge.ui.common.composables.components.text.TabbedDataOverview
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.LumbridgeTopAppBar
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.TopAppBarVariation
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.HalfPadding
import com.eyther.lumbridge.ui.theme.QuarterPadding

@Composable
fun DetailsFinancialProfileScreen(
    navController: NavHostController,
    @StringRes label: Int,
    viewModel: IDetailsFinancialProfileScreenViewModel = hiltViewModel<DetailsFinancialProfileScreenViewModel>()
) {
    val viewState = viewModel.viewState.collectAsStateWithLifecycle().value

    Scaffold(
        topBar = {
            LumbridgeTopAppBar(
                topAppBarVariation = TopAppBarVariation.TitleAndIcon(
                    title = stringResource(id = label),
                    onIconClick = { navController.popBackStack() }
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .imePadding()
                .verticalScroll(rememberScrollState())
        ) {
            when (viewState) {
                is DetailsFinancialProfileScreenViewState.Loading -> {
                    LoadingIndicator()
                }

                is DetailsFinancialProfileScreenViewState.Empty -> EmptyScreen(
                    onCtaClick = { navController.navigateTo(OverviewNavigationItem.FinancialProfile.Edit) }
                )

                is DetailsFinancialProfileScreenViewState.Content -> Content(
                    state = viewState,
                    navController = navController
                )
            }
        }
    }
}

@Composable
private fun ColumnScope.Content(
    state: DetailsFinancialProfileScreenViewState.Content,
    navController: NavHostController
) {
    val currencySymbol = remember { state.locale.getCurrencySymbol() }

    IncomeOverview(
        netSalaryUi = state.salaryDetails,
        currencySymbol = currencySymbol,
        onEditClick = {
            navController.navigateTo(OverviewNavigationItem.FinancialProfile.Edit)
        }
    )

    Spacer(
        modifier = Modifier.height(DefaultPadding)
    )

    PerCountryBreakdown(
        netSalaryUi = state.salaryDetails,
        locale = state.locale,
        currencySymbol = currencySymbol
    )

    Spacer(
        modifier = Modifier.height(DefaultPadding)
    )

    MoneyAllocationBreakdown(
        currencySymbol = currencySymbol,
        navController = navController,
        moneyAllocation = state.salaryDetails.moneyAllocations
    )

    Spacer(
        modifier = Modifier.height(DefaultPadding)
    )
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
                        indication = ripple(bounded = false)
                    ) {
                        navController.navigateTo(OverviewNavigationItem.FinancialProfile.Edit)
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

@Composable
private fun ColumnScope.EmptyScreen(
    onCtaClick: () -> Unit
) {
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
            onCtaClick()
        }
    )
}

