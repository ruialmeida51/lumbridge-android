package com.eyther.lumbridge.features.editfinancialprofile.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.eyther.lumbridge.R
import com.eyther.lumbridge.features.editfinancialprofile.model.EditFinancialProfileScreenViewEffect
import com.eyther.lumbridge.features.editfinancialprofile.model.EditFinancialProfileScreenViewEffect.None
import com.eyther.lumbridge.features.editfinancialprofile.model.EditFinancialProfileScreenViewState
import com.eyther.lumbridge.features.editfinancialprofile.viewmodel.EditFinancialProfileScreenViewModel
import com.eyther.lumbridge.features.editfinancialprofile.viewmodel.IEditFinancialProfileScreenViewModel
import com.eyther.lumbridge.ui.common.composables.components.buttons.LumbridgeButton
import com.eyther.lumbridge.ui.common.composables.components.input.NumberInput
import com.eyther.lumbridge.ui.common.composables.components.loading.LoadingIndicator
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
        Box(
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

        SalaryBreakdown(
            currencySymbol = currencySymbol,
            state = state,
            viewModel = viewModel
        )
        Spacer(modifier = Modifier.height(DefaultPadding))

        DemographicInformation(
            state = state,
            viewModel = viewModel
        )

        Spacer(modifier = Modifier.height(DefaultPadding))

        SavingsBreakdown(
            state = state,
            viewModel = viewModel
        )

        Spacer(modifier = Modifier.height(DefaultPadding))

        LumbridgeButton(
            label = stringResource(id = R.string.edit_financial_profile_save),
            enableButton = state.shouldEnableSaveButton,
            onClick = { viewModel.saveUserData(navController) }
        )
    }
}

@Composable
private fun ColumnScope.DemographicInformation(
    state: EditFinancialProfileScreenViewState.Content,
    viewModel: IEditFinancialProfileScreenViewModel
) {
    Text(
        modifier = Modifier
            .padding(vertical = HalfPadding)
            .align(Alignment.Start),
        text = stringResource(id = R.string.edit_financial_profile_household),
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
            label = stringResource(id = R.string.handicapped),
            isChecked = state.inputState.handicapped,
            onCheckedChange = { viewModel.onHandicappedChanged(it) }
        )

        Spacer(modifier = Modifier.height(DefaultPadding))

        SwitchSetting(
            label = stringResource(id = R.string.married),
            isChecked = state.inputState.married,
            onCheckedChange = { viewModel.onMarriedChanged(it) }
        )

        Spacer(modifier = Modifier.height(DefaultPadding))

        SwitchSetting(
            label = stringResource(id = R.string.single_incomne),
            enabled = state.inputState.married,
            isChecked = state.inputState.singleIncome,
            onCheckedChange = { viewModel.onSingleIncomeChanged(it) }
        )

        Spacer(modifier = Modifier.height(DefaultPadding))

        NumberInput(
            label = stringResource(id = R.string.number_of_dependants),
            placeholder = "0",
            state = state.inputState.numberOfDependants,
            onInputChanged = { input ->
                viewModel.onNumberOfDependantsChanged(input.toIntOrNull())
            }
        )
    }
}

@Composable
private fun ColumnScope.SalaryBreakdown(
    currencySymbol: String,
    state: EditFinancialProfileScreenViewState.Content,
    viewModel: IEditFinancialProfileScreenViewModel
) {
    Text(
        modifier = Modifier
            .padding(bottom = HalfPadding)
            .align(Alignment.Start),
        text = stringResource(id = R.string.edit_financial_profile_income),
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
            label = stringResource(id = R.string.gross_annual),
            placeholder = "35000$currencySymbol",
            state = state.inputState.annualGrossSalary,
            onInputChanged = { input ->
                viewModel.onAnnualGrossSalaryChanged(input.toFloatOrNull())
            }
        )

        Spacer(modifier = Modifier.height(DefaultPadding))

        NumberInput(
            label = stringResource(id = R.string.edit_financial_profile_per_diem_food_card),
            placeholder = "8.60",
            state = state.inputState.foodCardPerDiem,
            onInputChanged = { input ->
                viewModel.onFoodCardPerDiemChanged(input.toFloatOrNull())
            }
        )
    }
}

@Composable
private fun ColumnScope.SavingsBreakdown(
    state: EditFinancialProfileScreenViewState.Content,
    viewModel: IEditFinancialProfileScreenViewModel
) {
    Text(
        modifier = Modifier
            .padding(vertical = HalfPadding)
            .align(Alignment.Start),
        text = stringResource(id = R.string.edit_financial_profile_allocate_earnings),
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
            label = stringResource(id = R.string.savings_percentage),
            placeholder = stringResource(id = R.string.edit_financial_profile_suggested, "30"),
            state = state.inputState.savingsPercentage,
            onInputChanged = { input ->
                viewModel.onSavingsPercentageChanged(input.toIntOrNull())
            }
        )

        Spacer(modifier = Modifier.height(DefaultPadding))

        NumberInput(
            label = stringResource(id = R.string.necessities_percentage),
            placeholder = stringResource(id = R.string.edit_financial_profile_suggested, "50"),
            state = state.inputState.necessitiesPercentage,
            onInputChanged = { input ->
                viewModel.onNecessitiesPercentageChanged(input.toIntOrNull())
            }
        )

        Spacer(modifier = Modifier.height(DefaultPadding))

        NumberInput(
            label = stringResource(id = R.string.luxuries_percentage),
            placeholder = stringResource(id = R.string.edit_financial_profile_suggested, "20"),
            state = state.inputState.luxuriesPercentage,
            onInputChanged = { input ->
                viewModel.onLuxuriesPercentageChanged(input.toIntOrNull())
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            )
        )
    }
}
