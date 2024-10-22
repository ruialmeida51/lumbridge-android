package com.eyther.lumbridge.features.tools.netsalary.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.eyther.lumbridge.R
import com.eyther.lumbridge.features.overview.components.IncomeOverview
import com.eyther.lumbridge.features.overview.components.PerCountryBreakdown
import com.eyther.lumbridge.features.tools.netsalary.arguments.INetSalaryScreenArgumentsCacheViewModel
import com.eyther.lumbridge.features.tools.netsalary.model.result.NetSalaryResultScreenViewState
import com.eyther.lumbridge.features.tools.netsalary.viewmodel.result.INetSalaryResultScreenViewModel
import com.eyther.lumbridge.features.tools.netsalary.viewmodel.result.NetSalaryResultScreenViewModel
import com.eyther.lumbridge.ui.common.composables.components.buttons.LumbridgeButton
import com.eyther.lumbridge.ui.common.composables.components.defaults.EmptyScreenWithButton
import com.eyther.lumbridge.ui.common.composables.components.loading.LoadingIndicator
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.LumbridgeTopAppBar
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.TopAppBarVariation
import com.eyther.lumbridge.ui.theme.DefaultPadding

@Composable
fun NetSalaryResultScreen(
    @StringRes label: Int,
    navController: NavHostController,
    argumentsCache: INetSalaryScreenArgumentsCacheViewModel,
    viewModel: INetSalaryResultScreenViewModel =
        hiltViewModel<NetSalaryResultScreenViewModel, INetSalaryResultScreenViewModel.Factory>(
            creationCallback = { factory ->
                factory.create(
                    netSalaryUi = argumentsCache.netSalaryUi,
                    locale = argumentsCache.locale
                )
            }
        )
) {
    val state = viewModel.viewState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            LumbridgeTopAppBar(
                TopAppBarVariation.TitleAndIcon(
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
                .verticalScroll(rememberScrollState())
        ) {
            when (val currentState = state.value) {
                is NetSalaryResultScreenViewState.Content -> {
                    Content(
                        state = currentState,
                        navController = navController
                    )
                }

                is NetSalaryResultScreenViewState.Error -> {
                    Error(navController::popBackStack)
                }

                is NetSalaryResultScreenViewState.Loading -> {
                    LoadingIndicator()
                }
            }
        }
    }
}

@Composable
private fun Error(
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        EmptyScreenWithButton(
            modifier = Modifier.padding(DefaultPadding),
            text = stringResource(id = R.string.tools_net_salary_calculate_error),
            buttonText = stringResource(id = R.string.retry),
            icon = {
                Icon(
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(
                        id = R.drawable.ic_savings
                    ),
                    contentDescription = stringResource(
                        id = R.string.tools_net_salary_calculate_button
                    )
                )
            },
            onButtonClick = onRetry
        )
    }
}

@Composable
private fun ColumnScope.Content(
    state: NetSalaryResultScreenViewState.Content,
    navController: NavHostController
) {
    IncomeOverview(
        netSalaryUi = state.netSalary,
        currencySymbol = state.locale.getCurrencySymbol(),
        onEditClick = { navController.popBackStack() }
    )

    Spacer(
        modifier = Modifier.height(DefaultPadding)
    )

    PerCountryBreakdown(
        netSalaryUi = state.netSalary,
        locale = state.locale,
        currencySymbol = state.locale.getCurrencySymbol()
    )

    Spacer(
        modifier = Modifier.padding(DefaultPadding)
    )

    LumbridgeButton(
        modifier = Modifier.padding(horizontal = DefaultPadding),
        label = stringResource(id = R.string.tools_net_salary_calculate_another_button),
        onClick = { navController.popBackStack() }
    )

    Spacer(
        modifier = Modifier.padding(DefaultPadding)
    )
}
