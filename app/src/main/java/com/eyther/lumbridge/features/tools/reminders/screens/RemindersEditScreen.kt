@file:OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)

package com.eyther.lumbridge.features.tools.reminders.screens

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.eyther.lumbridge.R
import com.eyther.lumbridge.features.tools.reminders.model.edit.RemindersEditScreenViewEffects
import com.eyther.lumbridge.features.tools.reminders.model.edit.RemindersEditScreenViewState
import com.eyther.lumbridge.features.tools.reminders.viewmodel.edit.IRemindersEditScreenViewModel
import com.eyther.lumbridge.features.tools.reminders.viewmodel.edit.RemindersEditScreenViewModel
import com.eyther.lumbridge.launcher.model.permissions.NeededPermission
import com.eyther.lumbridge.model.time.RemindMeInUi
import com.eyther.lumbridge.shared.time.extensions.toLocalDate
import com.eyther.lumbridge.ui.common.composables.components.buttons.LumbridgeButton
import com.eyther.lumbridge.ui.common.composables.components.card.ColumnCardWrapper
import com.eyther.lumbridge.ui.common.composables.components.datepicker.LumbridgeDateTimePickerDialog
import com.eyther.lumbridge.ui.common.composables.components.input.DateTimeInput
import com.eyther.lumbridge.ui.common.composables.components.input.DropdownInput
import com.eyther.lumbridge.ui.common.composables.components.input.TextInput
import com.eyther.lumbridge.ui.common.composables.components.loading.LoadingIndicator
import com.eyther.lumbridge.ui.common.composables.components.permissions.TryRequestPermission
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
import java.time.LocalDate

@Composable
fun RemindersEditScreen(
    navController: NavHostController,
    @StringRes label: Int,
    viewModel: IRemindersEditScreenViewModel = hiltViewModel<RemindersEditScreenViewModel>()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val state = viewModel.viewState.collectAsStateWithLifecycle().value
    val snackbarHostState = remember { SnackbarHostState() }
    val neededPermission = remember { NeededPermission.Notifications }
    val notificationsPermissionState = rememberPermissionState(neededPermission.permission)
    val askForNotificationsPermission = remember { mutableStateOf(false) }

    if (!notificationsPermissionState.status.isGranted) {
        askForNotificationsPermission.value = true
    }

    LaunchedEffect(Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.viewEffects
                .onEach { viewEffects ->
                    when (viewEffects) {
                        is RemindersEditScreenViewEffects.ShowError -> {
                            Toast.makeText(context, viewEffects.errorMessage.getString(context), Toast.LENGTH_SHORT).show()
                        }

                        is RemindersEditScreenViewEffects.CloseScreen -> navController.popBackStack()
                    }
                }
                .collect()
        }
    }

    Scaffold(
        topBar = {
            LumbridgeTopAppBar(
                topAppBarVariation = TopAppBarVariation.TitleAndIcon(title = stringResource(id = label)) {
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
                .then(
                    if (askForNotificationsPermission.value) Modifier.blur(5.dp) else Modifier
                )
        ) {
            when (state) {
                is RemindersEditScreenViewState.Loading -> LoadingIndicator()

                is RemindersEditScreenViewState.Content -> {
                    Content(
                        state = state,
                        neededPermission = neededPermission,
                        notificationsPermissionState = notificationsPermissionState,
                        askForNotificationsPermission = askForNotificationsPermission,
                        onReminderNameChanged = viewModel::onNameChanged,
                        onReminderDateChanged = viewModel::onDueDateChanged,
                        onReminderTypeChanged = viewModel::onRemindMeInTypeChanged,
                        onReminderDaysInputChanged = viewModel::onReminderDaysInputChanged,
                        onReminderHoursInputChanged = viewModel::onReminderHoursInputChanged,
                        onReminderMinutesInputChanged = viewModel::onReminderMinutesInputChanged,
                        onInvalidDateTime = viewModel::onInvalidDateTime,
                        saveReminder = viewModel::saveReminder
                    )
                }
            }
        }
    }
}

@Composable
private fun Content(
    state: RemindersEditScreenViewState.Content,
    neededPermission: NeededPermission,
    notificationsPermissionState: PermissionState,
    askForNotificationsPermission: MutableState<Boolean>,
    onReminderNameChanged: (String?) -> Unit,
    onReminderDateChanged: (Long?) -> Unit,
    onReminderTypeChanged: (Int?) -> Unit,
    onReminderDaysInputChanged: (Int?) -> Unit,
    onReminderHoursInputChanged: (Int?) -> Unit,
    onReminderMinutesInputChanged: (Int?) -> Unit,
    onInvalidDateTime: () -> Unit,
    saveReminder: () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier.padding(top = DefaultPadding)
    ) {
        ReminderInformation(
            state = state,
            onReminderNameChanged = onReminderNameChanged,
            onReminderDateChanged = onReminderDateChanged,
            onInvalidDateTime = onInvalidDateTime
        )

        Spacer(modifier = Modifier.height(DefaultPadding))

        RemindMeInInput(
            state = state,
            availableReminderTimes = state.availableReminderTimes,
            context = context,
            onReminderTypeChanged = onReminderTypeChanged,
            onReminderDaysInputChanged = onReminderDaysInputChanged,
            onReminderHoursInputChanged = onReminderHoursInputChanged,
            onReminderMinutesInputChanged = onReminderMinutesInputChanged
        )

        LumbridgeButton(
            modifier = Modifier.padding(DefaultPadding),
            label = stringResource(id = R.string.tools_reminders_save),
            enableButton = state.shouldEnableSaveButton,
            onClick = {
                saveReminder()
            }
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
private fun ReminderInformation(
    state: RemindersEditScreenViewState.Content,
    onReminderNameChanged: (String?) -> Unit,
    onReminderDateChanged: (Long?) -> Unit,
    onInvalidDateTime: () -> Unit
) {
    val showDueDateDialog = remember { mutableStateOf(false) }
    val isSelectableYear = { year: Int -> year >= LocalDate.now().year }

    val datePickerState = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis.toLocalDate() >= LocalDate.now()
            }

            override fun isSelectableYear(year: Int): Boolean {
                return isSelectableYear(year)
            }
        }
    )

    val timePickerState = rememberTimePickerState()

    ColumnCardWrapper {
        TextInput(
            modifier = Modifier.padding(bottom = HalfPadding),
            state = state.inputState.name,
            label = stringResource(id = R.string.name),
            onInputChanged = onReminderNameChanged,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            )
        )

        DateTimeInput(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = HalfPadding),
            state = state.inputState.dueDate,
            label = stringResource(id = R.string.tools_reminders_due_date),
            placeholder = stringResource(id = R.string.tools_reminders_invalid_date),
            onClick = { showDueDateDialog.value = true }
        )

        LumbridgeDateTimePickerDialog(
            showDialog = showDueDateDialog,
            datePickerState = datePickerState,
            timePickerState = timePickerState,
            allowPastDates = false,
            onInvalidDateTime = onInvalidDateTime,
            onSaveDateTime = onReminderDateChanged
        )
    }
}

@Composable
private fun RemindMeInInput(
    state: RemindersEditScreenViewState.Content,
    availableReminderTimes: List<RemindMeInUi>,
    context: Context,
    onReminderTypeChanged: (Int?) -> Unit,
    onReminderDaysInputChanged: (Int?) -> Unit,
    onReminderHoursInputChanged: (Int?) -> Unit,
    onReminderMinutesInputChanged: (Int?) -> Unit
) {
    ColumnCardWrapper {
        DropdownInput(
            label = stringResource(id = R.string.tools_reminders_remind_me_in),
            isError = state.inputState.remindMeInputState.isError(),
            errorText = state.inputState.remindMeInputState.errorText?.getString(context),
            selectedOption = state.inputState.remindMeInputState.remindMeInUi.getPeriodicitySelectionHumanReadable().getString(context),
            items = availableReminderTimes.map { it.ordinal.toString() to it.getPeriodicitySelectionHumanReadable().getString(context) },
            onItemClick = { ordinal, _ -> onReminderTypeChanged(ordinal.toIntOrNull()) }
        )

        when (state.inputState.remindMeInputState.remindMeInUi) {
            is RemindMeInUi.XDaysBefore -> RemindMeXDaysBeforeInput(state, onReminderDaysInputChanged)
            is RemindMeInUi.XHoursBefore -> RemindMeXHoursBeforeInput(state, onReminderHoursInputChanged)
            is RemindMeInUi.XMinutesBefore -> RemindMeXMinutesBeforeInput(state, onReminderMinutesInputChanged)
            else -> Unit
        }
    }
}

@Composable
private fun RemindMeXDaysBeforeInput(
    state: RemindersEditScreenViewState.Content,
    onReminderDaysInputChanged: (Int?) -> Unit
) {
    TextInput(
        state = state.inputState.nDaysBeforeInput,
        label = stringResource(id = R.string.tools_reminder_n_days_before_input),
        onInputChanged = { onReminderDaysInputChanged(it.toIntOrNull()) },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next
        )
    )
}

@Composable
private fun RemindMeXHoursBeforeInput(
    state: RemindersEditScreenViewState.Content,
    onReminderHoursInputChanged: (Int?) -> Unit
) {
    TextInput(
        state = state.inputState.nHoursBeforeInput,
        label = stringResource(id = R.string.tools_reminder_n_hours_before_input),
        onInputChanged = { onReminderHoursInputChanged(it.toIntOrNull()) },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next
        )
    )
}

@Composable
private fun RemindMeXMinutesBeforeInput(
    state: RemindersEditScreenViewState.Content,
    onReminderMinutesInputChanged: (Int?) -> Unit
) {
    TextInput(
        state = state.inputState.nMinutesBeforeInput,
        label = stringResource(id = R.string.tools_reminder_n_minutes_before_input),
        onInputChanged = { onReminderMinutesInputChanged(it.toIntOrNull()) },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        )
    )
}
