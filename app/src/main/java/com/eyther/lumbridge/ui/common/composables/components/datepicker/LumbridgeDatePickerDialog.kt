@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.eyther.lumbridge.ui.common.composables.components.datepicker

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.eyther.lumbridge.R
import com.eyther.lumbridge.shared.time.toLocalDate
import com.eyther.lumbridge.ui.common.composables.components.buttons.LumbridgeButton
import com.eyther.lumbridge.ui.common.composables.components.input.DateInput
import com.eyther.lumbridge.ui.common.composables.model.input.DateInputState
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.DoublePadding
import com.eyther.lumbridge.ui.theme.HalfPadding

private const val INVALID = -1

@Composable
fun LumbridgeDatePickerDialog(
    showDialog: MutableState<Boolean>,
    datePickerState: DatePickerState,
    onSaveDate: (selectedDate: Long?) -> Unit
) {
    if (showDialog.value) {
        DatePickerDialog(
            onDismissRequest = {
                showDialog.value = false
            },
            confirmButton = {
                Column(
                    modifier = Modifier
                        .padding(
                            start = DefaultPadding,
                            end = DoublePadding,
                            bottom = DefaultPadding
                        )
                        .clickable {
                            showDialog.value = false
                            onSaveDate(datePickerState.selectedDateMillis)
                        },
                ) {
                    Text(
                        text = stringResource(id = R.string.save)
                    )
                }
            },
            dismissButton = {
                Column(
                    modifier = Modifier
                        .padding(
                            start = DoublePadding,
                            end = DefaultPadding,
                            bottom = DefaultPadding
                        )
                        .clickable { showDialog.value = false },
                ) {
                    Text(
                        text = stringResource(id = R.string.cancel)
                    )
                }
            },
            content = {
                Column(
                    modifier = Modifier.padding(HalfPadding),
                ) {
                    DatePicker(datePickerState)
                }
            }
        )
    }
}

@Composable
fun LumbridgeYearMonthPicker(
    modifier: Modifier = Modifier,
    shouldShowDialog: MutableState<Boolean>,
    @StringRes title: Int,
    @StringRes label: Int,
    datePickerState: DatePickerState,
    onSaveDate: (year: Int, month: Int) -> Unit
) {
    if (shouldShowDialog.value) {
        val year = remember { mutableIntStateOf(INVALID) }
        val month = remember { mutableIntStateOf(INVALID) }
        val showDatePickerDialog = remember { mutableStateOf(false) }
        val dateInputState = remember { mutableStateOf(DateInputState()) }

        BasicAlertDialog(
            onDismissRequest = {
                shouldShowDialog.value = false
            },
            content = {
                Surface(
                    shape = AlertDialogDefaults.shape,
                    color = AlertDialogDefaults.containerColor
                ) {
                    Column(
                        modifier = modifier
                            .padding(DefaultPadding),
                        verticalArrangement = Arrangement.spacedBy(DefaultPadding)
                    ) {
                        Text(
                            text = stringResource(id = title),
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Text(
                            text = stringResource(id = label),
                            style = MaterialTheme.typography.bodySmall
                        )

                        DateInput(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = DefaultPadding),
                            state = dateInputState.value,
                            label = stringResource(id = R.string.start_date),
                            placeholder = stringResource(id = R.string.edit_loan_profile_invalid_start_date),
                            onClick = { showDatePickerDialog.value = true }
                        )

                        LumbridgeButton(
                            enableButton = year.intValue != INVALID && month.intValue != INVALID,
                            label = stringResource(id = R.string.apply),
                            onClick = {
                                onSaveDate(year.intValue, month.intValue)
                                shouldShowDialog.value = false
                            }
                        )

                        LumbridgeButton(
                            label = stringResource(id = R.string.cancel),
                            onClick = {
                                shouldShowDialog.value = false
                            }
                        )
                    }

                }
            }
        )

        LumbridgeDatePickerDialog(
            showDialog = showDatePickerDialog,
            datePickerState = datePickerState,
            onSaveDate = { date ->
                // Get the local date
                val localDate = date?.toLocalDate()

                // Update the date input state
                dateInputState.value = dateInputState.value.copy(date = localDate)

                // Update the year and month
                year.intValue = localDate?.year ?: INVALID
                month.intValue = localDate?.monthValue ?: INVALID
            }
        )
    }
}

@Composable
fun LumbridgeYearMonthRangePicker(
    modifier: Modifier = Modifier,
    shouldShowDialog: MutableState<Boolean>,
    @StringRes title: Int,
    @StringRes label: Int,
    startDatePickerState: DatePickerState,
    endDatePickerState: DatePickerState,
    onSaveDate: (startYear: Int, startMonth: Int, endYear: Int, endMonth: Int) -> Unit
) {
    if (shouldShowDialog.value) {
        val startYear = remember { mutableIntStateOf(INVALID) }
        val startMonth = remember { mutableIntStateOf(INVALID) }
        val endYear = remember { mutableIntStateOf(INVALID) }
        val endMonth = remember { mutableIntStateOf(INVALID) }
        val showStartDatePickerDialog = remember { mutableStateOf(false) }
        val showEndDatePickerDialog = remember { mutableStateOf(false) }
        val startDateInputState = remember { mutableStateOf(DateInputState()) }
        val endDateInputState = remember { mutableStateOf(DateInputState()) }

        BasicAlertDialog(
            onDismissRequest = {
                shouldShowDialog.value = false
            },
            content = {
                Surface(
                    shape = AlertDialogDefaults.shape,
                    color = AlertDialogDefaults.containerColor
                ) {
                    Column(
                        modifier = modifier
                            .padding(DefaultPadding),
                        verticalArrangement = Arrangement.spacedBy(DefaultPadding)
                    ) {
                        Text(
                            text = stringResource(id = title),
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Text(
                            text = stringResource(id = label),
                            style = MaterialTheme.typography.bodySmall
                        )

                        DateInput(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = DefaultPadding),
                            state = startDateInputState.value,
                            label = stringResource(id = R.string.start_date),
                            placeholder = stringResource(id = R.string.edit_loan_profile_invalid_start_date),
                            onClick = { showStartDatePickerDialog.value = true }
                        )

                        DateInput(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = DefaultPadding),
                            state = endDateInputState.value,
                            label = stringResource(id = R.string.end_date),
                            placeholder = stringResource(id = R.string.edit_loan_profile_invalid_start_date),
                            onClick = { showEndDatePickerDialog.value = true }
                        )

                        LumbridgeButton(
                            enableButton = startYear.intValue != INVALID &&
                                endMonth.intValue != INVALID &&
                                endYear.intValue != INVALID  &&
                                endMonth.intValue != INVALID,
                            label = stringResource(id = R.string.apply),
                            onClick = {
                                onSaveDate(startYear.intValue, startMonth.intValue, endYear.intValue, endMonth.intValue)
                                shouldShowDialog.value = false
                            }
                        )

                        LumbridgeButton(
                            label = stringResource(id = R.string.cancel),
                            onClick = {
                                shouldShowDialog.value = false
                            }
                        )
                    }

                }
            }
        )

        LumbridgeDatePickerDialog(
            showDialog = showStartDatePickerDialog,
            datePickerState = startDatePickerState,
            onSaveDate = { startDate ->
                // Get the local date
                val startLocalDate = startDate?.toLocalDate()

                // Update the date input state
                startDateInputState.value = startDateInputState.value.copy(date = startLocalDate)

                // Update the year and month
                startYear.intValue = startLocalDate?.year ?: INVALID
                startMonth.intValue = startLocalDate?.monthValue ?: INVALID
            }
        )

        LumbridgeDatePickerDialog(
            showDialog = showEndDatePickerDialog,
            datePickerState = endDatePickerState,
            onSaveDate = { endDate ->
                // Get the local date
                val endLocalDate = endDate?.toLocalDate()

                // Update the date input state
                endDateInputState.value = endDateInputState.value.copy(date = endLocalDate)

                // Update the year and month
                endYear.intValue = endLocalDate?.year ?: INVALID
                endMonth.intValue = endLocalDate?.monthValue ?: INVALID
            }
        )
    }
}
