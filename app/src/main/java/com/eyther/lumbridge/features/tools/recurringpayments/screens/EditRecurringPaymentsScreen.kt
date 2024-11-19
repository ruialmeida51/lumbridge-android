@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)

package com.eyther.lumbridge.features.tools.recurringpayments.screens

import android.os.Build
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.eyther.lumbridge.R
import com.eyther.lumbridge.extensions.kotlin.capitalise
import com.eyther.lumbridge.features.tools.recurringpayments.model.edit.EditRecurringPaymentScreenViewEffects
import com.eyther.lumbridge.features.tools.recurringpayments.model.edit.EditRecurringPaymentsScreenViewState
import com.eyther.lumbridge.features.tools.recurringpayments.viewmodel.edit.EditRecurringPaymentsScreenViewModel
import com.eyther.lumbridge.features.tools.recurringpayments.viewmodel.edit.IEditRecurringPaymentsScreenViewModel
import com.eyther.lumbridge.launcher.model.permissions.NeededPermission
import com.eyther.lumbridge.model.time.PeriodicityUi
import com.eyther.lumbridge.shared.time.extensions.toLocalDate
import com.eyther.lumbridge.ui.common.composables.components.buttons.ChoiceTab
import com.eyther.lumbridge.ui.common.composables.components.buttons.LumbridgeButton
import com.eyther.lumbridge.ui.common.composables.components.card.ColumnCardWrapper
import com.eyther.lumbridge.ui.common.composables.components.datepicker.LumbridgeDatePickerDialog
import com.eyther.lumbridge.ui.common.composables.components.input.DateInput
import com.eyther.lumbridge.ui.common.composables.components.input.DropdownInput
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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun EditRecurringPaymentsScreen(
    navController: NavHostController,
    @StringRes label: Int,
    viewModel: IEditRecurringPaymentsScreenViewModel = hiltViewModel<EditRecurringPaymentsScreenViewModel>()
) {
    val viewState = viewModel.viewState.collectAsStateWithLifecycle().value
    val lifecycleOwner = LocalLifecycleOwner.current
    val recurringPaymentToDelete = remember { mutableLongStateOf(-1L) }

    val neededPermission = remember { NeededPermission.Notifications }
    val notificationsPermissionState = rememberPermissionState(neededPermission.permission)
    val askForNotificationsPermission = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.viewEffects
                .onEach { viewEffects ->
                    when (viewEffects) {
                        EditRecurringPaymentScreenViewEffects.CloseScreen -> navController.popBackStack()
                    }
                }
                .collect()
        }
    }

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
                .then(
                    if (recurringPaymentToDelete.longValue >= 0L || askForNotificationsPermission.value) Modifier.blur(5.dp) else Modifier
                )
        ) {
            when (viewState) {
                is EditRecurringPaymentsScreenViewState.Loading -> LoadingIndicator()
                is EditRecurringPaymentsScreenViewState.Content -> Content(
                    state = viewState,
                    neededPermission = neededPermission,
                    notificationsPermissionState = notificationsPermissionState,
                    askForNotificationsPermission = askForNotificationsPermission,
                    maxSelectableYear = viewModel.getMaxSelectableYear(),
                    onNameChanged = viewModel::onPaymentNamedChanged,
                    onNotifyMeWhenPaidChanged = viewModel::onNotifyMeWhenPaidChanged,
                    onAmountChanged = viewModel::onPaymentAmountChanged,
                    onCategoryTypeChanged = viewModel::onPaymentTypeChanged,
                    onMoneyAllocationChanged = viewModel::onPaymentAllocationChanged,
                    onSurplusOrExpenseChanged = viewModel::onPaymentSurplusOrExpenseChanged,
                    onStartAtDateChanged = viewModel::onPaymentStartDateChanged,
                    onPeriodicityChanged = viewModel::onPeriodicityTypeChanged,
                    onNumOfDaysChanged = viewModel::onNumOfDaysChanged,
                    onNumOfWeeksChanged = viewModel::onNumOfWeeksChanged,
                    onNumOfMonthsChanged = viewModel::onNumOfMonthsChanged,
                    onNumOfYearsChanged = viewModel::onNumOfYearsChanged,
                    onDayOfWeekChanged = viewModel::onDayOfWeekChanged,
                    onDayOfMonthChanged = viewModel::onDayOfMonthChanged,
                    onMonthOfYearChanged = viewModel::onMonthOfYearChanged,
                    onSave = viewModel::saveRecurringPayment
                )
            }
        }
    }
}

@Composable
private fun ColumnScope.Content(
    state: EditRecurringPaymentsScreenViewState.Content,
    neededPermission: NeededPermission,
    notificationsPermissionState: PermissionState,
    askForNotificationsPermission: MutableState<Boolean>,
    maxSelectableYear: Int,
    onNotifyMeWhenPaidChanged: (Boolean) -> Unit,
    onNameChanged: (String) -> Unit,
    onAmountChanged: (Float?) -> Unit,
    onCategoryTypeChanged: (Int?) -> Unit,
    onMoneyAllocationChanged: (Int?) -> Unit,
    onSurplusOrExpenseChanged: (Int) -> Unit,
    onStartAtDateChanged: (Long?) -> Unit,
    onPeriodicityChanged: (Int?) -> Unit,
    onNumOfDaysChanged: (Int?) -> Unit,
    onNumOfWeeksChanged: (Int?) -> Unit,
    onNumOfMonthsChanged: (Int?) -> Unit,
    onNumOfYearsChanged: (Int?) -> Unit,
    onDayOfWeekChanged: (Int?) -> Unit,
    onDayOfMonthChanged: (Int?) -> Unit,
    onMonthOfYearChanged: (Int?) -> Unit,
    onSave: () -> Unit
) {
    ExpenseDetails(
        state = state,
        notificationsPermissionState = notificationsPermissionState,
        askForNotificationsPermission = askForNotificationsPermission,
        onNotifyMeWhenPaidChanged = onNotifyMeWhenPaidChanged,
        onNameChanged = onNameChanged,
        onAmountChanged = onAmountChanged,
        onCategoryTypeChanged = onCategoryTypeChanged,
        onSurplusOrExpenseChanged = onSurplusOrExpenseChanged,
        onMoneyAllocationChanged = onMoneyAllocationChanged
    )

    PeriodicityDetails(
        state = state,
        maxSelectableYear = maxSelectableYear,
        onStartAtDateChanged = onStartAtDateChanged,
        onPeriodicityChanged = onPeriodicityChanged,
        onNumOfDaysChanged = onNumOfDaysChanged,
        onNumOfWeeksChanged = onNumOfWeeksChanged,
        onNumOfMonthsChanged = onNumOfMonthsChanged,
        onNumOfYearsChanged = onNumOfYearsChanged,
        onDayOfWeekChanged = onDayOfWeekChanged,
        onDayOfMonthChanged = onDayOfMonthChanged,
        onMonthOfYearChanged = onMonthOfYearChanged
    )

    LumbridgeButton(
        modifier = Modifier.padding(DefaultPadding),
        label = stringResource(id = R.string.save),
        onClick = onSave,
        enableButton = state.shouldEnableSaveButton
    )

    if (askForNotificationsPermission.value && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        TryRequestPermission(
            neededPermission = neededPermission,
            permissionState = notificationsPermissionState,
            askForNotificationsPermission = askForNotificationsPermission
        )
    }
}

@Composable
private fun ColumnScope.ExpenseDetails(
    state: EditRecurringPaymentsScreenViewState.Content,
    notificationsPermissionState: PermissionState,
    askForNotificationsPermission: MutableState<Boolean>,
    onNotifyMeWhenPaidChanged: (Boolean) -> Unit,
    onNameChanged: (String) -> Unit,
    onAmountChanged: (Float?) -> Unit,
    onCategoryTypeChanged: (Int?) -> Unit,
    onMoneyAllocationChanged: (Int?) -> Unit,
    onSurplusOrExpenseChanged: (Int) -> Unit
) {
    Text(
        modifier = Modifier
            .padding(top = DefaultPadding, start = DefaultPadding, end = DefaultPadding, bottom = HalfPadding)
            .align(Alignment.Start),
        text = stringResource(id = R.string.recurring_payments_edit_information),
        style = MaterialTheme.typography.bodyLarge
    )

    ColumnCardWrapper {
        SwitchSetting(
            modifier = Modifier.padding(top = HalfPadding, bottom = DefaultPadding),
            label = stringResource(id = R.string.recurring_payment_notify_me_when_paid),
            isChecked = state.inputState.shouldNotifyWhenPaid,
            onCheckedChange = { notifyWhenPaid ->
                onNotifyMeWhenPaidChanged(notifyWhenPaid)

                if (notificationsPermissionState.status.isGranted) {
                    return@SwitchSetting
                }

                if (notifyWhenPaid) {
                    askForNotificationsPermission.value = true
                }
            }
        )

        TextInput(
            modifier = Modifier.padding(bottom = HalfPadding),
            state = state.inputState.paymentName,
            label = stringResource(id = R.string.name),
            onInputChanged = { onNameChanged(it) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            )
        )

        NumberInput(
            modifier = Modifier.padding(bottom = HalfPadding),
            state = state.inputState.paymentAmount,
            label = stringResource(id = R.string.recurring_payment_amount),
            onInputChanged = { onAmountChanged(it.toFloatOrNull()) }
        )

        ChoiceTab(
            modifier = Modifier.padding(bottom = DefaultPadding),
            choiceTabState = state.inputState.surplusOrExpenseChoice,
            onOptionClicked = onSurplusOrExpenseChanged
        )

        if (!state.inputState.isSurplusSelected) {
            DropdownInputWithIcon(
                label = stringResource(id = R.string.recurring_payment_category),
                selectedOption = stringResource(state.inputState.categoryType.categoryRes),
                selectedIcon = state.inputState.categoryType.iconRes,
                items = state.availableCategories.map { Triple(it.iconRes, it.ordinal.toString(), stringResource(it.categoryRes)) },
                onItemClick = { _, ordinal, _ -> onCategoryTypeChanged(ordinal.toIntOrNull()) }
            )
        }


        DropdownInputWithIcon(
            label = stringResource(id = R.string.expenses_add_allocation),
            selectedOption = stringResource(state.inputState.allocationTypeUi.labelRes),
            selectedIcon = state.inputState.allocationTypeUi.iconRes,
            items = state.availableMoneyAllocations.map { Triple(it.iconRes, it.ordinal.toString(), stringResource(it.labelRes)) },
            onItemClick = { _, ordinal, _ -> onMoneyAllocationChanged(ordinal.toIntOrNull()) }
        )
    }
}

@Composable
private fun ColumnScope.PeriodicityDetails(
    state: EditRecurringPaymentsScreenViewState.Content,
    maxSelectableYear: Int,
    onStartAtDateChanged: (Long?) -> Unit,
    onPeriodicityChanged: (Int?) -> Unit,
    onNumOfDaysChanged: (Int?) -> Unit,
    onNumOfWeeksChanged: (Int?) -> Unit,
    onNumOfMonthsChanged: (Int?) -> Unit,
    onNumOfYearsChanged: (Int?) -> Unit,
    onDayOfWeekChanged: (Int?) -> Unit,
    onDayOfMonthChanged: (Int?) -> Unit,
    onMonthOfYearChanged: (Int?) -> Unit
) {
    val context = LocalContext.current
    val jvmLocale = remember { Locale.getDefault() }
    val selectableYears = LocalDate.now().year..maxSelectableYear
    val isSelectableYear = { year: Int -> year >= LocalDate.now().year }
    val showStartAtDatePickerDialog = remember { mutableStateOf(false) }

    val startAtDatePickerState = rememberDatePickerState(
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

    Text(
        modifier = Modifier
            .padding(top = DefaultPadding, start = DefaultPadding, end = DefaultPadding, bottom = HalfPadding)
            .align(Alignment.Start),
        text = stringResource(id = R.string.recurring_payment_edit_expense_frequency),
        style = MaterialTheme.typography.bodyLarge
    )

    ColumnCardWrapper {
        DateInput(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = HalfPadding),
            state = state.inputState.paymentStartDate,
            label = stringResource(id = R.string.recurring_payment_edit_frequency_start_date),
            placeholder = stringResource(id = R.string.recurring_payment_edit_frequency_start_date_error),
            onClick = { showStartAtDatePickerDialog.value = true }
        )

        LumbridgeDatePickerDialog(
            showDialog = showStartAtDatePickerDialog,
            datePickerState = startAtDatePickerState,
            onSaveDate = onStartAtDateChanged
        )

        DropdownInput(
            label = stringResource(id = R.string.recurring_payment_edit_expense_frequency_input),
            selectedOption = state.inputState.periodicityUi.getPeriodicitySelectionHumanReadable(jvmLocale).getString(context),
            items = state.availablePeriodicity.map { it.ordinal.toString() to it.getPeriodicitySelectionHumanReadable(jvmLocale).getString(context) },
            onItemClick = { ordinal, _ -> onPeriodicityChanged(ordinal.toIntOrNull()) }
        )

        Spacer(modifier = Modifier.height(HalfPadding))

        PeriodicityDataInput(
            state = state,
            locale = jvmLocale,
            onNumOfDaysChanged = onNumOfDaysChanged,
            onNumOfWeeksChanged = onNumOfWeeksChanged,
            onNumOfMonthsChanged = onNumOfMonthsChanged,
            onNumOfYearsChanged = onNumOfYearsChanged,
            onDayOfWeekChanged = onDayOfWeekChanged,
            onDayOfMonthChanged = onDayOfMonthChanged,
            onMonthOfYearChanged = onMonthOfYearChanged
        )
    }
}

@Composable
private fun PeriodicityDataInput(
    state: EditRecurringPaymentsScreenViewState.Content,
    locale: Locale,
    onNumOfDaysChanged: (Int?) -> Unit,
    onNumOfWeeksChanged: (Int?) -> Unit,
    onNumOfMonthsChanged: (Int?) -> Unit,
    onNumOfYearsChanged: (Int?) -> Unit,
    onDayOfWeekChanged: (Int?) -> Unit,
    onDayOfMonthChanged: (Int?) -> Unit,
    onMonthOfYearChanged: (Int?) -> Unit
) {
    when (state.inputState.periodicityUi) {
        is PeriodicityUi.EveryXDays -> {
            NumberInput(
                modifier = Modifier.padding(bottom = HalfPadding),
                state = state.inputState.numOfDays,
                label = stringResource(id = R.string.recurring_payment_edit_frequency_days),
                onInputChanged = { onNumOfDaysChanged(it.toIntOrNull()) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                )
            )
        }

        is PeriodicityUi.EveryXWeeks -> {
            NumberInput(
                modifier = Modifier.padding(bottom = HalfPadding),
                state = state.inputState.numOfWeeks,
                label = stringResource(id = R.string.recurring_payment_edit_frequency_weeks),
                onInputChanged = { onNumOfWeeksChanged(it.toIntOrNull()) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                )
            )

            DropdownInput(
                label = stringResource(id = R.string.recurring_payment_edit_frequency_weeks_day),
                selectedOption = state.inputState.dayOfWeek.getDisplayName(TextStyle.FULL, locale).capitalise(),
                items = DayOfWeek.entries.map { (it.ordinal + 1).toString() to it.getDisplayName(TextStyle.FULL, locale).capitalise() },
                onItemClick = { dayOfWeek, _ -> onDayOfWeekChanged(dayOfWeek.toIntOrNull()) }
            )
        }

        is PeriodicityUi.EveryXMonths -> {
            NumberInput(
                modifier = Modifier.padding(bottom = HalfPadding),
                state = state.inputState.numOfMonths,
                label = stringResource(id = R.string.recurring_payment_edit_frequency_months),
                onInputChanged = { onNumOfMonthsChanged(it.toIntOrNull()) }
            )

            NumberInput(
                modifier = Modifier.padding(bottom = HalfPadding),
                state = state.inputState.dayOfMonth,
                label = stringResource(id = R.string.recurring_payment_edit_frequency_months_day_of_month),
                onInputChanged = { onDayOfMonthChanged(it.toIntOrNull()) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                )
            )
        }

        is PeriodicityUi.EveryXYears -> {
            NumberInput(
                modifier = Modifier.padding(bottom = HalfPadding),
                state = state.inputState.numOfYears,
                label = stringResource(id = R.string.recurring_payment_edit_frequency_years),
                onInputChanged = { onNumOfYearsChanged(it.toIntOrNull()) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                )
            )

            DropdownInput(
                label = stringResource(id = R.string.recurring_payment_edit_frequency_years_month),
                selectedOption = state.inputState.monthOfYear.getDisplayName(TextStyle.FULL, locale).capitalise(),
                items = Month.entries.map { (it.ordinal + 1).toString() to it.getDisplayName(TextStyle.FULL, locale).capitalise() },
                onItemClick = { month, _ -> onMonthOfYearChanged(month.toIntOrNull()) }
            )
        }
    }
}
