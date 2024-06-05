package com.eyther.lumbridge.features.editfinancialprofile.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.eyther.lumbridge.R
import com.eyther.lumbridge.features.editfinancialprofile.components.DemographicInformationInput
import com.eyther.lumbridge.features.editfinancialprofile.components.SalaryBreakdownInput
import com.eyther.lumbridge.features.editfinancialprofile.components.SavingsBreakdownInput
import com.eyther.lumbridge.features.editfinancialprofile.model.EditFinancialProfileScreenViewEffect
import com.eyther.lumbridge.features.editfinancialprofile.model.EditFinancialProfileScreenViewEffect.None
import com.eyther.lumbridge.features.editfinancialprofile.model.EditFinancialProfileScreenViewState
import com.eyther.lumbridge.features.editfinancialprofile.viewmodel.EditFinancialProfileScreenViewModel
import com.eyther.lumbridge.features.editfinancialprofile.viewmodel.IEditFinancialProfileScreenViewModel
import com.eyther.lumbridge.ui.common.composables.components.buttons.LumbridgeButton
import com.eyther.lumbridge.ui.common.composables.components.loading.LoadingIndicator
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.LumbridgeTopAppBar
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.TopAppBarVariation
import com.eyther.lumbridge.ui.theme.DefaultPadding

@Composable
fun EditFinancialProfileScreen(
    navController: NavHostController,
    @StringRes label: Int,
    viewModel: IEditFinancialProfileScreenViewModel = hiltViewModel<EditFinancialProfileScreenViewModel>()
) {
    val state = viewModel.viewState.collectAsStateWithLifecycle().value
    val sideEffects = viewModel.viewEffect.collectAsStateWithLifecycle(None).value

    Scaffold(
        topBar = {
            LumbridgeTopAppBar(
                topAppBarVariation = TopAppBarVariation.TitleAndIcon(
                    title = stringResource(id = label)
                ) {
                    navController.popBackStack()
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            when (sideEffects) {
                is EditFinancialProfileScreenViewEffect.ShowError -> Snackbar {
                    Text(text = sideEffects.message)
                }

                else -> Unit
            }

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

    Column(
        Modifier.padding(DefaultPadding)
    ) {

        SalaryBreakdownInput(
            currencySymbol = currencySymbol,
            salaryInputChoice = state.inputState.salaryInputChoiceState,
            selectedTab = state.inputState.salaryInputChoiceState.selectedTab,
            monthlyGrossSalary = state.inputState.monthlyGrossSalary,
            annualGrossSalary = state.inputState.annualGrossSalary,
            foodCardPerDiem = state.inputState.foodCardPerDiem,
            onSalaryInputTypeChanged = viewModel::onSalaryInputTypeChanged,
            onMonthlyGrossSalaryChanged = viewModel::onMonthlyGrossSalaryChanged,
            onAnnualGrossSalaryChanged = viewModel::onAnnualGrossSalaryChanged,
            onFoodCardPerDiemChanged = viewModel::onFoodCardPerDiemChanged
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
            label = stringResource(id = R.string.edit_financial_profile_save),
            enableButton = state.shouldEnableSaveButton,
            onClick = { viewModel.saveUserData(navController) }
        )
    }
}
