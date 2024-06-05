package com.eyther.lumbridge.features.tools.netsalary.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.eyther.lumbridge.R
import com.eyther.lumbridge.extensions.kotlin.capitalise
import com.eyther.lumbridge.features.editfinancialprofile.components.DemographicInformationInput
import com.eyther.lumbridge.features.editfinancialprofile.components.SalaryBreakdownInput
import com.eyther.lumbridge.features.tools.netsalary.arguments.INetSalaryScreenArgumentsCacheViewModel
import com.eyther.lumbridge.features.tools.netsalary.model.input.NetSalaryInputScreenViewState
import com.eyther.lumbridge.features.tools.netsalary.viewmodel.input.INetSalaryInputScreenViewModel
import com.eyther.lumbridge.features.tools.netsalary.viewmodel.input.NetSalaryInputScreenViewModel
import com.eyther.lumbridge.ui.common.composables.components.buttons.LumbridgeButton
import com.eyther.lumbridge.ui.common.composables.components.defaults.EmptyScreenWithButton
import com.eyther.lumbridge.ui.common.composables.components.input.DropdownInput
import com.eyther.lumbridge.ui.common.composables.components.loading.LoadingIndicator
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.LumbridgeTopAppBar
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.TopAppBarVariation
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.DefaultRoundedCorner
import com.eyther.lumbridge.ui.theme.QuarterPadding

@Composable
fun NetSalaryInputScreen(
    @StringRes label: Int,
    navController: NavHostController,
    argumentsCache: INetSalaryScreenArgumentsCacheViewModel,
    viewModel: INetSalaryInputScreenViewModel = hiltViewModel<NetSalaryInputScreenViewModel>()
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
                .padding(paddingValues)
                .fillMaxSize()
                .padding(DefaultPadding)
                .verticalScroll(rememberScrollState())
        ) {
            when (val currentState = state.value) {
                is NetSalaryInputScreenViewState.Content -> {
                    Content(
                        navController = navController,
                        viewModel = viewModel,
                        argumentsCache = argumentsCache,
                        state = currentState
                    )
                }

                is NetSalaryInputScreenViewState.Loading -> {
                    LoadingIndicator()
                }

                is NetSalaryInputScreenViewState.Error -> {
                    Error(viewModel::onRetryInput)
                }
            }
        }
    }
}

@Composable
private fun ColumnScope.Error(
    onRetry: () -> Unit
) {
    EmptyScreenWithButton(
        modifier = Modifier
            .weight(1f)
            .fillMaxSize()
            .padding(DefaultPadding),
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

@Composable
private fun ColumnScope.Content(
    state: NetSalaryInputScreenViewState.Content,
    viewModel: INetSalaryInputScreenViewModel,
    argumentsCache: INetSalaryScreenArgumentsCacheViewModel,
    navController: NavHostController
) {
    val currencySymbol = state.locale.getCurrencySymbol()

    SalaryBreakdownInput(
        currencySymbol = currencySymbol,
        salaryInputChoice = state.inputState.salaryInputChoiceState,
        selectedTab = state.inputState.salaryInputChoiceState.selectedTab,
        monthlyGrossSalary = state.inputState.monthlyGrossSalary,
        annualGrossSalary = state.inputState.annualGrossSalary,
        foodCardPerDiem = state.inputState.foodCardPerDiem,
        onMonthlyGrossSalaryChanged = viewModel::onMonthlyGrossSalaryChanged,
        onAnnualGrossSalaryChanged = viewModel::onAnnualGrossSalaryChanged,
        onFoodCardPerDiemChanged = viewModel::onFoodCardPerDiemChanged,
        onSalaryInputTypeChanged = viewModel::onSalaryInputTypeChanged
    )

    Spacer(modifier = Modifier.height(DefaultPadding))

    DemographicInformationInput(
        handicapped = state.inputState.handicapped,
        married = state.inputState.married,
        numberOfDependants = state.inputState.numberOfDependants,
        singleIncome = state.inputState.singleIncome,
        onHandicappedChanged = viewModel::onHandicappedChanged,
        onMarriedChanged = viewModel::onMarriedChanged,
        onNumberOfDependantsChanged = viewModel::onNumberOfDependantsChanged,
        onSingleIncomeChanged = viewModel::onSingleIncomeChanged
    )

    Spacer(modifier = Modifier.height(DefaultPadding))

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(DefaultRoundedCorner))
            .shadow(elevation = QuarterPadding)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(DefaultPadding)
    ) {
        DropdownInput(
            label = stringResource(id = R.string.edit_profile_select_country),
            selectedOption = state.inputState.locale.name.capitalise(),
            items = state.availableLocales.map { it.countryCode to it.name.capitalise() },
            onItemClick = { identifier, _ -> viewModel.onLocaleChanged(identifier) }
        )
    }

    Spacer(modifier = Modifier.height(DefaultPadding))

    LumbridgeButton(
        label = stringResource(id = R.string.tools_net_salary_calculate_button),
        enableButton = state.shouldEnableSaveButton,
        onClick = {
            viewModel.onCalculateNetSalary(
                navController,
                argumentsCache::cacheArguments
            )
        }
    )

    Spacer(modifier = Modifier.height(DefaultPadding))
}