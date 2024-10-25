@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.eyther.lumbridge.features.editloan.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.eyther.lumbridge.R
import com.eyther.lumbridge.domain.time.toLocalDate
import com.eyther.lumbridge.features.editloan.model.EditLoanFixedTypeUi
import com.eyther.lumbridge.features.editloan.model.EditLoanScreenViewEffect
import com.eyther.lumbridge.features.editloan.model.EditLoanScreenViewState
import com.eyther.lumbridge.features.editloan.model.EditLoanVariableOrFixedUi
import com.eyther.lumbridge.features.editloan.viewmodel.EditLoanScreenViewModel
import com.eyther.lumbridge.features.editloan.viewmodel.IEditLoanScreenViewModel
import com.eyther.lumbridge.ui.common.composables.components.buttons.ChoiceTab
import com.eyther.lumbridge.ui.common.composables.components.buttons.LumbridgeButton
import com.eyther.lumbridge.ui.common.composables.components.card.ColumnCardWrapper
import com.eyther.lumbridge.ui.common.composables.components.datepicker.LumbridgeDatePickerDialog
import com.eyther.lumbridge.ui.common.composables.components.input.DateInput
import com.eyther.lumbridge.ui.common.composables.components.input.DropdownInputWithIcon
import com.eyther.lumbridge.ui.common.composables.components.input.NumberInput
import com.eyther.lumbridge.ui.common.composables.components.input.TextInput
import com.eyther.lumbridge.ui.common.composables.components.loading.LoadingIndicator
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.LumbridgeTopAppBar
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.TopAppBarVariation
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.HalfPadding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import java.time.LocalDate

@Composable
fun EditLoanScreen(
    navController: NavHostController,
    @StringRes label: Int,
    viewModel: IEditLoanScreenViewModel = hiltViewModel<EditLoanScreenViewModel>()
) {
    val state = viewModel.viewState.collectAsStateWithLifecycle().value
    val snackbarHostState = remember { SnackbarHostState() }
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.viewEffects
                .onEach { viewEffects ->
                    when (viewEffects) {
                        is EditLoanScreenViewEffect.ShowError -> {
                            snackbarHostState.showSnackbar(
                                message = viewEffects.message,
                                duration = SnackbarDuration.Short
                            )
                        }
                        is EditLoanScreenViewEffect.NavigateBack -> navController.popBackStack()
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
                .verticalScroll(rememberScrollState())
        ) {
            when (state) {
                is EditLoanScreenViewState.Content -> Content(
                    state = state,
                    viewModel = viewModel
                )

                is EditLoanScreenViewState.Loading -> LoadingIndicator()
            }
        }
    }
}

@Composable
fun Content(
    state: EditLoanScreenViewState.Content,
    viewModel: IEditLoanScreenViewModel
) {
    Column {
        LoanType(
            state = state,
            viewModel = viewModel
        )

        Spacer(modifier = Modifier.height(DefaultPadding))

        RemainingAmount(
            state = state,
            viewModel = viewModel
        )

        Spacer(modifier = Modifier.height(DefaultPadding))

        MortgageType(
            state = state,
            viewModel = viewModel
        )

        LumbridgeButton(
            modifier = Modifier.padding(DefaultPadding),
            label = stringResource(id = R.string.edit_loan_profile_save),
            enableButton = state.shouldEnableSaveButton,
            onClick = { viewModel.saveLoan() }
        )
    }
}

@Composable
private fun ColumnScope.LoanType(
    state: EditLoanScreenViewState.Content,
    viewModel: IEditLoanScreenViewModel
) {
    Text(
        modifier = Modifier
            .padding(top = DefaultPadding, start = DefaultPadding, end = DefaultPadding, bottom = HalfPadding)
            .align(Alignment.Start),
        text = stringResource(id = R.string.edit_loan_profile_loan_type),
        style = MaterialTheme.typography.bodyLarge
    )

    ColumnCardWrapper {
        TextInput(
            modifier = Modifier.padding(bottom = DefaultPadding),
            state = state.inputState.name,
            label = stringResource(id = R.string.name),
            onInputChanged = { viewModel.onNameChanged(it) }
        )

        DropdownInputWithIcon(
            label = stringResource(id = R.string.edit_loan_profile_loan_category),
            selectedOption = stringResource(state.inputState.categoryUi.label),
            selectedIcon = state.inputState.categoryUi.icon,
            items = state.availableLoanCategories.map { Triple(it.icon, it.ordinal.toString(), stringResource(it.label)) },
            onItemClick = { _, ordinal, _ -> viewModel.onLoanCategoryChanged(ordinal.toIntOrNull()) }
        )
    }
}

@Composable
private fun ColumnScope.RemainingAmount(
    state: EditLoanScreenViewState.Content,
    viewModel: IEditLoanScreenViewModel
) {
    val selectableYears = LocalDate.now().year..viewModel.getMaxSelectableYear()
    val isSelectableYear = { year: Int -> year >= LocalDate.now().year }

    val showStartDateDialog = remember { mutableStateOf(false) }
    val startDatePickerState = rememberDatePickerState(
        yearRange = selectableYears,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis.toLocalDate() >= LocalDate.now()
            }

            override fun isSelectableYear(year: Int): Boolean {
                return isSelectableYear(year)
            }
        }
    )

    val showEndDateDialog = remember { mutableStateOf(false) }
    val endDatePickerState = rememberDatePickerState(
        yearRange = selectableYears,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return viewModel.isSelectableEndDate(utcTimeMillis)
            }

            override fun isSelectableYear(year: Int): Boolean {
                return isSelectableYear(year)
            }
        }
    )

    Text(
        modifier = Modifier
            .padding(start = DefaultPadding, end = DefaultPadding, bottom = HalfPadding)
            .align(Alignment.Start),
        text = stringResource(id = R.string.edit_loan_profile_owe),
        style = MaterialTheme.typography.bodyLarge
    )

    ColumnCardWrapper {
        DateInput(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = DefaultPadding),
            state = state.inputState.startDate,
            label = stringResource(id = R.string.start_date),
            placeholder = stringResource(id = R.string.edit_loan_profile_invalid_start_date),
            onClick = { showStartDateDialog.value = true }
        )

        LumbridgeDatePickerDialog(
            showDialog = showStartDateDialog,
            datePickerState = startDatePickerState,
            onSaveDate = { viewModel.onStartDateChanged(it) }
        )

        DateInput(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = DefaultPadding),
            state = state.inputState.endDate,
            label = stringResource(id = R.string.end_date),
            placeholder = stringResource(id = R.string.edit_loan_profile_invalid_end_date),
            onClick = { showEndDateDialog.value = true }
        )

        LumbridgeDatePickerDialog(
            showDialog = showEndDateDialog,
            datePickerState = endDatePickerState,
            onSaveDate = { viewModel.onEndDateChanged(it) }
        )

        NumberInput(
            modifier = Modifier.padding(bottom = DefaultPadding),
            state = state.inputState.loanAmount,
            label = stringResource(id =  if (state.isCreateLoan) R.string.initial_loan_amount else R.string.current_loan_amount),
            onInputChanged = { viewModel.onMortgageAmountChanged(it.toFloatOrNull()) }
        )
    }
}

@Composable
private fun ColumnScope.MortgageType(
    state: EditLoanScreenViewState.Content,
    viewModel: IEditLoanScreenViewModel
) {
    Text(
        modifier = Modifier
            .padding(start = DefaultPadding, end = DefaultPadding, bottom = HalfPadding)
            .align(Alignment.Start),
        text = stringResource(id = R.string.edit_loan_profile_loan),
        style = MaterialTheme.typography.bodyLarge
    )

    ColumnCardWrapper {
        ChoiceTab(
            title = stringResource(id = R.string.edit_loan_profile_loan_interest_rate),
            choiceTabState = state.inputState.fixedOrVariableLoanChoiceState,
            onOptionClicked = { viewModel.onFixedOrVariableLoanChanged(it) }
        )

        Spacer(modifier = Modifier.height(DefaultPadding))

        when (state.inputState.fixedOrVariableLoanChoiceState.selectedTab) {
            EditLoanVariableOrFixedUi.Variable.ordinal -> VariableMortgageInput(state, viewModel)
            EditLoanVariableOrFixedUi.Fixed.ordinal -> FixedMortgageInput(state, viewModel)
        }
    }
}

@Composable
private fun VariableMortgageInput(
    state: EditLoanScreenViewState.Content,
    viewModel: IEditLoanScreenViewModel
) {
    NumberInput(
        modifier = Modifier.padding(bottom = DefaultPadding),
        state = state.inputState.euribor,
        label = stringResource(id = R.string.euribor),
        onInputChanged = { viewModel.onEuriborRateChanged(it.toFloatOrNull()) }
    )

    NumberInput(
        modifier = Modifier.padding(bottom = DefaultPadding),
        state = state.inputState.spread,
        label = stringResource(id = R.string.spread),
        onInputChanged = { viewModel.onSpreadChanged(it.toFloatOrNull()) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        )
    )
}

@Composable
private fun FixedMortgageInput(
    state: EditLoanScreenViewState.Content,
    viewModel: IEditLoanScreenViewModel
) {
    ChoiceTab(
        title = stringResource(id = R.string.edit_loan_profile_loan_fixed_interest_rate),
        choiceTabState = state.inputState.tanOrTaegLoanChoiceState,
        onOptionClicked = { viewModel.onTanOrTaegLoanChanged(it) }
    )

    Spacer(modifier = Modifier.height(DefaultPadding))

    when (state.inputState.tanOrTaegLoanChoiceState.selectedTab) {
        EditLoanFixedTypeUi.Tan.ordinal -> {
            NumberInput(
                modifier = Modifier.padding(bottom = DefaultPadding),
                state = state.inputState.tanInterestRate,
                label = stringResource(id = R.string.interest_rate),
                onInputChanged = { viewModel.onTanInterestRateChanged(it.toFloatOrNull()) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
            )
        }

        EditLoanFixedTypeUi.Taeg.ordinal -> {
            NumberInput(
                modifier = Modifier.padding(bottom = DefaultPadding),
                state = state.inputState.taegInterestRate,
                label = stringResource(id = R.string.interest_rate),
                onInputChanged = { viewModel.onTaegInterestRateChanged(it.toFloatOrNull()) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
            )
        }
    }
}
