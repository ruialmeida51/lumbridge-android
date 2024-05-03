@file:OptIn(ExperimentalMaterial3Api::class)

package com.eyther.lumbridge.features.editfinancialprofile.screens.portugal

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.NavHostController
import com.eyther.lumbridge.features.editfinancialprofile.model.EditFinancialProfileScreenViewState
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.runescapeTypography

@Composable
fun ColumnScope.SalaryInputPortugal(
    navController: NavHostController,
    state: EditFinancialProfileScreenViewState.Content,
    saveUserData: (Float, Float, NavHostController) -> Unit
) {
    val userData = state.getUserData()

    val currencySymbol = state.locale.getCurrencySymbol()

    var annualGrossSalary by remember {
        mutableStateOf(TextFieldValue(userData?.annualGrossSalary?.toString().orEmpty()))
    }

    var foodCardPerDiem by remember {
        mutableStateOf(TextFieldValue(userData?.foodCardPerDiem?.toString().orEmpty()))
    }

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = annualGrossSalary,
        suffix = {
            Text(
                text = currencySymbol,
                style = runescapeTypography.bodyLarge
            )
        },
        textStyle = runescapeTypography.bodyLarge,
        onValueChange = { annualGrossSalary = it },
        label = {
            Text(
                text = "Annual Gross Salary",
                style = runescapeTypography.bodyLarge
            )
        },
        placeholder = {
            Text(
                text = "50000$currencySymbol"
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        colors = TextFieldDefaults.colors(
            focusedLabelColor = MaterialTheme.colorScheme.onSecondary
        )
    )

    Spacer(
        modifier = Modifier.height(DefaultPadding)
    )

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = foodCardPerDiem,
        suffix = {
            Text(
                text = currencySymbol,
                style = runescapeTypography.bodyLarge
            )
        },
        textStyle = runescapeTypography.bodyLarge,
        onValueChange = { foodCardPerDiem = it },
        label = {
            Text(
                text = "Per Diem for Food Card",
                style = runescapeTypography.bodyLarge
            )
        },
        placeholder = {
            Text(
                text = "8.60"
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        colors = TextFieldDefaults.colors(
            focusedLabelColor = MaterialTheme.colorScheme.onSecondary
        )
    )

    Spacer(
        modifier = Modifier.height(DefaultPadding)
    )

    Button(
        modifier = Modifier.fillMaxWidth(),
        enabled = annualGrossSalary.text.isNotEmpty() && foodCardPerDiem.text.isNotEmpty(),
        onClick = {
            saveUserData(
                annualGrossSalary.text.toFloat(),
                foodCardPerDiem.text.toFloat(),
                navController
            )
        }
    ) {
        Text(text = "Save Financial Profile")
    }
}
