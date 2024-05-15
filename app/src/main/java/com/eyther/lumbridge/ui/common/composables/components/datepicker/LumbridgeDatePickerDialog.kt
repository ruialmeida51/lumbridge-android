@file:OptIn(ExperimentalMaterial3Api::class)

package com.eyther.lumbridge.ui.common.composables.components.datepicker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.eyther.lumbridge.R
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.DoublePadding
import com.eyther.lumbridge.ui.theme.HalfPadding

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
