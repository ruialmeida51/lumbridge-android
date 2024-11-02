@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)

package com.eyther.lumbridge.features.editloan.screens

import android.os.Build
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
import androidx.compose.runtime.MutableState
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
import com.eyther.lumbridge.shared.time.extensions.toLocalDate
import com.eyther.lumbridge.features.editloan.model.EditLoanFixedTypeChoice
import com.eyther.lumbridge.features.editloan.model.EditLoanScreenViewEffect
import com.eyther.lumbridge.features.editloan.model.EditLoanScreenViewState
import com.eyther.lumbridge.features.editloan.model.EditLoanVariableOrFixedChoice
import com.eyther.lumbridge.features.editloan.viewmodel.EditLoanScreenViewModel
import com.eyther.lumbridge.features.editloan.viewmodel.IEditLoanScreenViewModel
import com.eyther.lumbridge.launcher.model.permissions.NeededPermission
import com.eyther.lumbridge.ui.common.composables.components.buttons.ChoiceTab
import com.eyther.lumbridge.ui.common.composables.components.buttons.LumbridgeButton
import com.eyther.lumbridge.ui.common.composables.components.card.ColumnCardWrapper
import com.eyther.lumbridge.ui.common.composables.components.datepicker.LumbridgeDatePickerDialog
import com.eyther.lumbridge.ui.common.composables.components.input.DateInput
import com.eyther.lumbridge.ui.common.composables.components.input.DropdownInputWithIcon
import com.eyther.lumbridge.ui.common.composables.components.input.NumberInput
import com.eyther.lumbridge.ui.common.composables.components.input.TextInput
import com.eyther.lumbridge.ui.common.composables.components.loading.LoadingIndicator
import com.eyther.lumbridge.ui.common.composables.components.permissions.TryRequestPermission
import com.eyther.lumbridge.ui.common.composables.components.setting.SwitchSetting
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.LumbridgeTopAppBar
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.TopAppBarVariation
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.HalfPadding
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
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
    val neededPermission = remember { NeededPermission.Notifications }
    val notificationsPermissionState = rememberPermissionState(neededPermission.permission)
    val askForNotificationsPermission = remember { mutableStateOf(false) }

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
                    viewModel = viewModel,
                    neededPermission = neededPermission,
                    notificationsPermissionState = notificationsPermissionState,
                    askForNotificationsPermission = askForNotificationsPermission
                )

                is EditLoanScreenViewState.Loading -> LoadingIndicator()
            }
        }
    }
}

@Composable
fun Content(
    state: EditLoanScreenViewState.Content,
    viewModel: IEditLoanScreenViewModel,
    neededPermission: NeededPermission,
    notificationsPermissionState: PermissionState,
    askForNotificationsPermission: MutableState<Boolean>
) {
    Column {
        LoanType(
            state = state,
            viewModel = viewModel,
            notificationsPermissionState = notificationsPermissionState,
            askForNotificationsPermission = askForNotificationsPermission
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

    if (askForNotificationsPermission.value && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        TryRequestPermission(
            neededPermission = neededPermission,
            permissionState = notificationsPermissionState,
            askForNotificationsPermission = askForNotificationsPermission
        )
    }
}

@Composable
private fun ColumnScope.LoanType(
    state: EditLoanScreenViewState.Content,
    viewModel: IEditLoanScreenViewModel,
    notificationsPermissionState: PermissionState,
    askForNotificationsPermission: MutableState<Boolean>
) {
    Text(
        modifier = Modifier
            .padding(top = DefaultPadding, start = DefaultPadding, end = DefaultPadding, bottom = HalfPadding)
            .align(Alignment.Start),
        text = stringResource(id = R.string.edit_loan_profile_loan_type),
        style = MaterialTheme.typography.bodyLarge
    )

    ColumnCardWrapper {
        SwitchSetting(
            modifier = Modifier.padding(bottom = DefaultPadding),
            label = stringResource(id = R.string.edit_loan_should_auto_add_to_expenses),
            isChecked = state.inputState.shouldAutoAddToExpenses,
            onCheckedChange = viewModel::onAutomaticallyAddToExpensesChanged
        )

        if (state.inputState.shouldAutoAddToExpenses) {
            SwitchSetting(
                modifier = Modifier.padding(top = HalfPadding, bottom = DefaultPadding),
                label = stringResource(id = R.string.edit_loan_should_notify_when_paid),
                isChecked = state.inputState.shouldNotifyWhenPaid,
                onCheckedChange = { notifyWhenPaid ->
                    viewModel.onNotifyWhenPaidChanged(notifyWhenPaid)

                    if (notificationsPermissionState.status.isGranted) {
                        return@SwitchSetting
                    }

                    if (notifyWhenPaid) {
                        askForNotificationsPermission.value = true
                    }
                }
            )

            NumberInput(
                state = state.inputState.paymentDay,
                label = stringResource(id = R.string.edit_loan_should_auto_add_to_expenses_payment_day),
                onInputChanged = { viewModel.onPaymentDayChanged(it.toIntOrNull()) }
            )
        }

        TextInput(
            modifier = Modifier.padding(bottom = HalfPadding),
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
                .padding(bottom = HalfPadding),
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
                .padding(bottom = HalfPadding),
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
            state = state.inputState.loanAmount,
            label = stringResource(id = if (state.isCreateLoan) R.string.initial_loan_amount else R.string.current_loan_amount),
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
            EditLoanVariableOrFixedChoice.Variable.ordinal -> VariableMortgageInput(state, viewModel)
            EditLoanVariableOrFixedChoice.Fixed.ordinal -> FixedMortgageInput(state, viewModel)
        }
    }
}

@Composable
private fun VariableMortgageInput(
    state: EditLoanScreenViewState.Content,
    viewModel: IEditLoanScreenViewModel
) {
    NumberInput(
        modifier = Modifier.padding(bottom = HalfPadding),
        state = state.inputState.euribor,
        label = stringResource(id = R.string.euribor),
        onInputChanged = { viewModel.onEuriborRateChanged(it.toFloatOrNull()) }
    )

    NumberInput(
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
        EditLoanFixedTypeChoice.Tan.ordinal -> {
            NumberInput(
                state = state.inputState.tanInterestRate,
                label = stringResource(id = R.string.interest_rate),
                onInputChanged = { viewModel.onTanInterestRateChanged(it.toFloatOrNull()) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
            )
        }

        EditLoanFixedTypeChoice.Taeg.ordinal -> {
            NumberInput(
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
