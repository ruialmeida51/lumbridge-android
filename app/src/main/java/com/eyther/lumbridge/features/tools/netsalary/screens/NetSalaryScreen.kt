@file:OptIn(ExperimentalMaterial3Api::class)

package com.eyther.lumbridge.features.tools.netsalary.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.eyther.lumbridge.domain.model.locale.InternalLocale
import com.eyther.lumbridge.features.tools.netsalary.model.NetSalaryScreenViewState
import com.eyther.lumbridge.features.tools.netsalary.viewmodel.NetSalaryScreenViewModel
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.runescapeTypography

@Composable
fun NetSalaryScreen(
    navController: NavController,
    label: String,
    netSalaryScreenViewModel: NetSalaryScreenViewModel = hiltViewModel()
) {
    val state = netSalaryScreenViewModel.viewState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(DefaultPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = runescapeTypography.titleLarge
        )

        Spacer(
            modifier = Modifier.height(DefaultPadding)
        )

        when (val currentState = state.value) {
            is NetSalaryScreenViewState.HasData -> HasUserData(
                state = currentState,
                onEditSalary = netSalaryScreenViewModel::onEditSalary
            )

            is NetSalaryScreenViewState.Input -> Input(
                state = currentState,
                onCalculateNetSalary = netSalaryScreenViewModel::onCalculateNetSalary
            )

            is NetSalaryScreenViewState.Initial -> Unit
        }
    }
}

@Composable
fun ColumnScope.Input(
    state: NetSalaryScreenViewState.Input,
    onCalculateNetSalary: (annualSalary: Float) -> Unit
) {
    var expandedLocale by remember { mutableStateOf(false) }
    var selectedLocale by remember { mutableStateOf(InternalLocale.entries.first().name) }

    var annualGrossSalary by remember {
        mutableStateOf(
            TextFieldValue(state.annualGrossSalary?.toString().orEmpty())
        )
    }

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = annualGrossSalary,
        onValueChange = { annualGrossSalary = it },
        label = { Text(text = "Annual Gross Salary") },
        placeholder = { Text(text = "34500") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )

    Spacer(
        modifier = Modifier.height(DefaultPadding)
    )

    ExposedDropdownMenuBox(
        expanded = expandedLocale,
        onExpandedChange = { expandedLocale = !expandedLocale }
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            value = selectedLocale,
            onValueChange = { },
            label = { Text("Label") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expandedLocale
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )
        ExposedDropdownMenu(
            expanded = expandedLocale,
            onDismissRequest = {
                expandedLocale = false
            }
        ) {
            InternalLocale.entries.forEach { selectionOption ->
                DropdownMenuItem(
                    text = {
                        Text(text = selectionOption.name)
                    },
                    onClick = {
                        selectedLocale = selectionOption.name
                        expandedLocale = false
                    }
                )
            }
        }
    }

    Spacer(
        modifier = Modifier
            .height(DefaultPadding)
            .weight(1f, true)
    )

    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = { onCalculateNetSalary(annualGrossSalary.text.toFloat()) }
    ) {
        Text(text = "Calculate")
    }
}

@Composable
fun ColumnScope.HasUserData(
    state: NetSalaryScreenViewState.HasData,
    onEditSalary: () -> Unit
) {
    Text(
        text = "Net Salary: ${state.netSalary}",
        style = runescapeTypography.titleSmall
    )

    Spacer(
        modifier = Modifier
            .height(DefaultPadding)
            .weight(1f, true)
    )

    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = { onEditSalary() }
    ) {
        Text(text = "Edit Salary")
    }
}

@Preview
@Composable
private fun Preview() {
    Column {
        Input(state = NetSalaryScreenViewState.Input(34000f), {})
        HasUserData(state = NetSalaryScreenViewState.HasData(2400f), {})
    }
}
