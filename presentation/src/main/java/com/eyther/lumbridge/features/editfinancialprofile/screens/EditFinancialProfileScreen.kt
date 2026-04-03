package com.eyther.lumbridge.features.editfinancialprofile.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.eyther.lumbridge.R
import com.eyther.lumbridge.features.editfinancialprofile.components.DemographicInformationInput
import com.eyther.lumbridge.features.editfinancialprofile.components.SalaryBreakdownInput
import com.eyther.lumbridge.features.editfinancialprofile.components.SavingsBreakdownInput
import com.eyther.lumbridge.features.editfinancialprofile.model.EditFinancialProfileScreenViewEffect
import com.eyther.lumbridge.features.editfinancialprofile.model.EditFinancialProfileScreenViewState
import com.eyther.lumbridge.features.editfinancialprofile.viewmodel.EditFinancialProfileScreenViewModel
import com.eyther.lumbridge.features.editfinancialprofile.viewmodel.IEditFinancialProfileScreenViewModel
import com.eyther.lumbridge.ui.common.composables.components.buttons.LumbridgeButton
import com.eyther.lumbridge.ui.common.composables.components.loading.LoadingIndicator
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.LumbridgeTopAppBar
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.TopAppBarVariation
import com.eyther.lumbridge.ui.theme.DefaultPadding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun EditFinancialProfileScreen(
    navController: NavHostController,
    @StringRes label: Int,
    viewModel: IEditFinancialProfileScreenViewModel = hiltViewModel<EditFinancialProfileScreenViewModel>()
) {
    val state = viewModel.viewState.collectAsStateWithLifecycle().value
    val lifecycleOwner = LocalLifecycleOwner.current

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.viewEffects
                .onEach { viewEffects ->
                    when (viewEffects) {
                        is EditFinancialProfileScreenViewEffect.ShowError -> {
                            snackbarHostState.showSnackbar(
                                message = viewEffects.message,
                                duration = SnackbarDuration.Short
                            )
                        }
                        is EditFinancialProfileScreenViewEffect.CloseScreen -> {
                            navController.navigateUp()
                        }
                    }
                }
                .collect()
        }
    }

    Scaffold(
        topBar = {
            LumbridgeTopAppBar(
                topAppBarVariation = TopAppBarVariation.TitleAndIcon(
                    title = stringResource(id = label)
                ) {
                    navController.popBackStack()
                }
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
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
            when (state) {
                is EditFinancialProfileScreenViewState.Content -> Content(
                    navController = navController,
                    state = state,
                    viewModel = viewModel
                )

                is EditFinancialProfileScreenViewState.Loading -> LoadingIndicator()
            }
        }
    }
}

@Composable
private fun Content(
    navController: NavHostController,
    state: EditFinancialProfileScreenViewState.Content,
    viewModel: IEditFinancialProfileScreenViewModel
) {
    val currencySymbol = state.locale.getCurrencySymbol()

    Column {
        Spacer(modifier = Modifier.height(DefaultPadding))

        SalaryBreakdownInput(
            currencySymbol = currencySymbol,
            salaryInputChoice = state.inputState.salaryInputChoiceState,
            selectedTab = state.inputState.salaryInputChoiceState.selectedTab,
            availableDuodecimos = state.availableDuodecimos,
            currentDuodecimosTypeUi = state.inputState.duodecimosTypeUi,
            monthlyGrossSalary = state.inputState.monthlyGrossSalary,
            annualGrossSalary = state.inputState.annualGrossSalary,
            foodCardPerDiem = state.inputState.foodCardPerDiem,
            onSalaryInputTypeChanged = viewModel::onSalaryInputTypeChanged,
            onMonthlyGrossSalaryChanged = viewModel::onMonthlyGrossSalaryChanged,
            onAnnualGrossSalaryChanged = viewModel::onAnnualGrossSalaryChanged,
            onFoodCardPerDiemChanged = viewModel::onFoodCardPerDiemChanged,
            onDuodecimosTypeChanged = viewModel::onDuodecimosTypeChanged
        )

        Spacer(modifier = Modifier.height(DefaultPadding))

        DemographicInformationInput(
            handicapped = state.inputState.handicapped,
            married = state.inputState.married,
            singleIncome = state.inputState.singleIncome,
            numberOfDependants = state.inputState.numberOfDependants,
            onHandicappedChanged = viewModel::onHandicappedChanged,
            onMarriedChanged = viewModel::onMarriedChanged,
            onSingleIncomeChanged = viewModel::onSingleIncomeChanged,
            onNumberOfDependantsChanged = viewModel::onNumberOfDependantsChanged
        )

        Spacer(modifier = Modifier.height(DefaultPadding))

        SavingsBreakdownInput(
            savingsPercentage = state.inputState.savingsPercentage,
            necessitiesPercentage = state.inputState.necessitiesPercentage,
            luxuriesPercentage = state.inputState.luxuriesPercentage,
            onSavingsPercentageChanged = viewModel::onSavingsPercentageChanged,
            onNecessitiesPercentageChanged = viewModel::onNecessitiesPercentageChanged,
            onLuxuriesPercentageChanged = viewModel::onLuxuriesPercentageChanged
        )

        Spacer(modifier = Modifier.height(DefaultPadding))

        LumbridgeButton(
            modifier = Modifier.padding(horizontal = DefaultPadding),
            label = stringResource(id = R.string.edit_financial_profile_save),
            enableButton = state.shouldEnableSaveButton,
            onClick = { viewModel.saveUserData(navController) }
        )

        Spacer(modifier = Modifier.height(DefaultPadding))
    }
}
