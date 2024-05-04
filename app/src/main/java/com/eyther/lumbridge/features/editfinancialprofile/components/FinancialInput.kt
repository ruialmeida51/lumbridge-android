package com.eyther.lumbridge.features.editfinancialprofile.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import com.eyther.lumbridge.ui.theme.runescapeTypography

@Composable
fun FinancialInput(
    label: String,
    placeholder: String,
    textFieldValue: MutableState<TextFieldValue>,
    suffix: String = "",
    onInputChanged: ((String) -> Unit) = {}
) {
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = textFieldValue.value,
        suffix = {
            Text(
                text = suffix,
                style = runescapeTypography.bodyLarge
            )
        },
        textStyle = runescapeTypography.bodyLarge,
        onValueChange = {
            textFieldValue.value = it
            onInputChanged(it.text)
        },
        label = {
            Text(
                text = label,
                style = runescapeTypography.bodyLarge
            )
        },
        placeholder = {
            Text(
                text = placeholder
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        colors = TextFieldDefaults.colors(
            focusedLabelColor = MaterialTheme.colorScheme.onSecondary
        )
    )
}
