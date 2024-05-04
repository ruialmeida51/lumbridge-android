package com.eyther.lumbridge.features.editfinancialprofile.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.eyther.lumbridge.features.editfinancialprofile.components.FinancialInput
import com.eyther.lumbridge.features.editfinancialprofile.model.EditFinancialProfileScreenViewState
import com.eyther.lumbridge.features.editfinancialprofile.viewmodel.EditFinancialProfileScreenViewModel
import com.eyther.lumbridge.features.editfinancialprofile.viewmodel.EditFinancialProfileScreenViewModelInterface
import com.eyther.lumbridge.ui.common.composables.components.setting.SwitchSetting
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.LumbridgeTopAppBar
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.TopAppBarVariation
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.runescapeTypography

@Composable
fun EditFinancialProfileScreen(
    navController: NavHostController,
    label: String,
    viewModel: EditFinancialProfileScreenViewModelInterface = hiltViewModel<EditFinancialProfileScreenViewModel>()
) {
    val state = viewModel.viewState.collectAsState().value

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
    viewModel: EditFinancialProfileScreenViewModelInterface
) {
    val userData = state.currentData
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

        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = state.shouldEnableSaveButton,
            onClick = { viewModel.saveUserData(navController) }
        ) {
            Text(text = "Save Financial Profile")
        }
    }
}

@Composable
fun ColumnScope.DemographicInformation(
    state: EditFinancialProfileScreenViewState.Content,
    viewModel: EditFinancialProfileScreenViewModelInterface
) {
    val numberOfDependants = remember {
        mutableStateOf(TextFieldValue(state.currentData?.numberOfDependants?.toString().orEmpty()))
    }

    Text(
        modifier = Modifier
            .padding(bottom = DefaultPadding)
            .align(Alignment.Start),
        text = "Demographic Information",
        style = runescapeTypography.titleSmall,
        color = MaterialTheme.colorScheme.onPrimary
    )


    SwitchSetting(
        label = "Handicapped ?",
        isChecked = state.currentData?.handicapped ?: false,
        onCheckedChange = { viewModel.onHandicappedChanged(it) },
    )

    Spacer(modifier = Modifier.height(DefaultPadding))

    SwitchSetting(
        label = "Married ?",
        isChecked = state.currentData?.married ?: false,
        onCheckedChange = { viewModel.onMarriedChanged(it) }
    )

    Spacer(modifier = Modifier.height(DefaultPadding))

    SwitchSetting(
        label = "IRS with partner ?",
        isChecked = state.currentData?.irsWithPartner ?: false,
        onCheckedChange = { viewModel.onIrsWithPartnerChanged(it) }
    )

    Spacer(modifier = Modifier.height(DefaultPadding))

    FinancialInput(
        label = "Number of dependants",
        placeholder = "0",
        textFieldValue = numberOfDependants,
        onInputChanged = { input ->
            viewModel.onNumberOfDependantsChanged(input.toIntOrNull())
        }
    )
}

@Composable
fun ColumnScope.SalaryBreakdown(
    currencySymbol: String,
    state: EditFinancialProfileScreenViewState.Content,
    viewModel: EditFinancialProfileScreenViewModelInterface
) {
    val annualGrossSalary = remember {
        mutableStateOf(TextFieldValue(state.currentData?.annualGrossSalary?.toString().orEmpty()))
    }
    val foodCardPerDiem = remember {
        mutableStateOf(TextFieldValue(state.currentData?.foodCardPerDiem?.toString().orEmpty()))
    }

    Text(
        modifier = Modifier
            .padding(bottom = DefaultPadding)
            .align(Alignment.Start),
        text = "What's your current income?",
        style = runescapeTypography.titleSmall,
        color = MaterialTheme.colorScheme.onPrimary
    )

    FinancialInput(
        suffix = currencySymbol,
        label = "Annual Gross Salary",
        placeholder = "50000$currencySymbol",
        textFieldValue = annualGrossSalary,
        onInputChanged = { input ->
            viewModel.onAnnualGrossSalaryChanged(input.toFloatOrNull())
        }
    )

    Spacer(modifier = Modifier.height(DefaultPadding))

    FinancialInput(
        suffix = currencySymbol,
        label = "Per Diem for Food Card",
        placeholder = "8.60",
        textFieldValue = foodCardPerDiem,
        onInputChanged = { input ->
            viewModel.onFoodCardPerDiemChanged(input.toFloatOrNull())
        }
    )

}

@Composable
fun ColumnScope.SavingsBreakdown(
    state: EditFinancialProfileScreenViewState.Content,
    viewModel: EditFinancialProfileScreenViewModelInterface
) {
    val savingsPercentage = remember {
        mutableStateOf(TextFieldValue(state.currentData?.savingsPercentage?.toString().orEmpty()))
    }
    val necessitiesPercentage = remember {
        mutableStateOf(TextFieldValue(state.currentData?.necessitiesPercentage?.toString().orEmpty()))
    }
    val luxuriesPercentage = remember {
        mutableStateOf(TextFieldValue(state.currentData?.luxuriesPercentage?.toString().orEmpty()))
    }

    Text(
        modifier = Modifier
            .padding(bottom = DefaultPadding)
            .align(Alignment.Start),
        text = "How do you plan to allocate your earnings?",
        style = runescapeTypography.titleSmall,
        color = MaterialTheme.colorScheme.onPrimary
    )

    FinancialInput(
        suffix = "%",
        label = "Savings Percentage",
        placeholder = "Suggested: 30",
        textFieldValue = savingsPercentage,
        onInputChanged = { input ->
            viewModel.onSavingsPercentageChanged(input.toIntOrNull())
        }
    )

    Spacer(modifier = Modifier.height(DefaultPadding))

    FinancialInput(
        suffix = "%",
        label = "Necessities Percentage",
        placeholder = "Suggested: 50",
        textFieldValue = necessitiesPercentage,
        onInputChanged = { input ->
            viewModel.onNecessitiesPercentageChanged(input.toIntOrNull())
        }
    )

    Spacer(modifier = Modifier.height(DefaultPadding))

    FinancialInput(
        suffix = "%",
        label = "Luxuries Percentage",
        placeholder = "Suggested: 20",
        textFieldValue = luxuriesPercentage,
        onInputChanged = { input ->
            viewModel.onLuxuriesPercentageChanged(input.toIntOrNull())
        }
    )
}
