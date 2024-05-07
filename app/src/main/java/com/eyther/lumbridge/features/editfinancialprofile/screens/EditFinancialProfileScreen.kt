package com.eyther.lumbridge.features.editfinancialprofile.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.eyther.lumbridge.features.editfinancialprofile.model.EditFinancialProfileScreenViewEffect
import com.eyther.lumbridge.features.editfinancialprofile.model.EditFinancialProfileScreenViewEffect.None
import com.eyther.lumbridge.features.editfinancialprofile.model.EditFinancialProfileScreenViewState
import com.eyther.lumbridge.features.editfinancialprofile.viewmodel.EditFinancialProfileScreenViewModel
import com.eyther.lumbridge.features.editfinancialprofile.viewmodel.IEditFinancialProfileScreenViewModel
import com.eyther.lumbridge.ui.common.composables.components.components.LumbridgeButton
import com.eyther.lumbridge.ui.common.composables.components.components.NumberInput
import com.eyther.lumbridge.ui.common.composables.components.setting.SwitchSetting
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.LumbridgeTopAppBar
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.TopAppBarVariation
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.HalfPadding
import com.eyther.lumbridge.ui.theme.QuarterPadding
import com.eyther.lumbridge.ui.theme.runescapeTypography

@Composable
fun EditFinancialProfileScreen(
    navController: NavHostController,
    label: String,
    viewModel: IEditFinancialProfileScreenViewModel = hiltViewModel<EditFinancialProfileScreenViewModel>()
) {
    val state = viewModel.viewState.collectAsStateWithLifecycle().value
    val sideEffects = viewModel.viewEffect.collectAsStateWithLifecycle(None).value

    Scaffold(
        topBar = {
            LumbridgeTopAppBar(
                topAppBarVariation = TopAppBarVariation.TitleAndIcon(title = label) {
                    navController.popBackStack()
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .height(IntrinsicSize.Max)
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

                is EditFinancialProfileScreenViewState.Loading -> Unit
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

        SalaryBreakdown(
            currencySymbol = currencySymbol,
            state = state,
            viewModel = viewModel
        )

        DemographicInformation(
            state = state,
            viewModel = viewModel
        )

        SavingsBreakdown(
            state = state,
            viewModel = viewModel
        )

        Spacer(modifier = Modifier.height(DefaultPadding))

        LumbridgeButton(
            label = "Save Financial Profile",
            enableButton = state.shouldEnableSaveButton,
            onClick = { viewModel.saveUserData(navController) }
        )
    }
}

@Composable
fun ColumnScope.DemographicInformation(
    state: EditFinancialProfileScreenViewState.Content,
    viewModel: IEditFinancialProfileScreenViewModel
) {
    Text(
        modifier = Modifier
            .padding(vertical = HalfPadding)
            .align(Alignment.Start),
        text = "What does your household look like?",
        style = runescapeTypography.bodyLarge
    )

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .shadow(elevation = QuarterPadding)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(DefaultPadding)
    ) {
        SwitchSetting(
            label = "Handicapped",
            isChecked = state.inputState.handicapped,
            onCheckedChange = { viewModel.onHandicappedChanged(it) },
        )

        Spacer(modifier = Modifier.height(DefaultPadding))

        SwitchSetting(
            label = "Married",
            isChecked = state.inputState.married,
            onCheckedChange = { viewModel.onMarriedChanged(it) }
        )

        Spacer(modifier = Modifier.height(DefaultPadding))

        SwitchSetting(
            label = "Single income",
            enabled = state.inputState.married,
            isChecked = state.inputState.singleIncome,
            onCheckedChange = { viewModel.onSingleIncomeChanged(it) }
        )

        Spacer(modifier = Modifier.height(DefaultPadding))

        NumberInput(
            label = "Number of dependants",
            placeholder = "0",
            state = state.inputState.numberOfDependants,
            onInputChanged = { input ->
                viewModel.onNumberOfDependantsChanged(input.toIntOrNull())
            }
        )
    }
}

@Composable
fun ColumnScope.SalaryBreakdown(
    currencySymbol: String,
    state: EditFinancialProfileScreenViewState.Content,
    viewModel: IEditFinancialProfileScreenViewModel
) {
    Text(
        modifier = Modifier
            .padding(bottom = HalfPadding)
            .align(Alignment.Start),
        text = "What's your current income?",
        style = runescapeTypography.bodyLarge
    )

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .shadow(elevation = QuarterPadding)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(DefaultPadding)
    ) {
        NumberInput(
            label = "Annual Gross Salary",
            placeholder = "35000$currencySymbol",
            state = state.inputState.annualGrossSalary,
            onInputChanged = { input ->
                viewModel.onAnnualGrossSalaryChanged(input.toFloatOrNull())
            }
        )

        Spacer(modifier = Modifier.height(DefaultPadding))

        NumberInput(
            label = "Per Diem for Food Card",
            placeholder = "8.60",
            state = state.inputState.foodCardPerDiem,
            onInputChanged = { input ->
                viewModel.onFoodCardPerDiemChanged(input.toFloatOrNull())
            }
        )
    }
}

@Composable
fun ColumnScope.SavingsBreakdown(
    state: EditFinancialProfileScreenViewState.Content,
    viewModel: IEditFinancialProfileScreenViewModel
) {
    Text(
        modifier = Modifier
            .padding(vertical = HalfPadding)
            .align(Alignment.Start),
        text = "How do you plan to allocate your earnings?",
        style = runescapeTypography.bodyLarge
    )

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .shadow(elevation = QuarterPadding)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(DefaultPadding)
    ) {
        NumberInput(
            label = "Savings Percentage",
            placeholder = "Suggested: 30",
            state = state.inputState.savingsPercentage,
            onInputChanged = { input ->
                viewModel.onSavingsPercentageChanged(input.toIntOrNull())
            }
        )

        Spacer(modifier = Modifier.height(DefaultPadding))

        NumberInput(
            label = "Necessities Percentage",
            placeholder = "Suggested: 50",
            state = state.inputState.necessitiesPercentage,
            onInputChanged = { input ->
                viewModel.onNecessitiesPercentageChanged(input.toIntOrNull())
            }
        )

        Spacer(modifier = Modifier.height(DefaultPadding))

        NumberInput(
            label = "Luxuries Percentage",
            placeholder = "Suggested: 20",
            state = state.inputState.luxuriesPercentage,
            onInputChanged = { input ->
                viewModel.onLuxuriesPercentageChanged(input.toIntOrNull())
            }
        )
    }
}
