@file:OptIn(ExperimentalMaterial3Api::class)

package com.eyther.lumbridge.ui.common.composables.components.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import com.eyther.lumbridge.ui.common.composables.model.TextInputState
import com.eyther.lumbridge.ui.theme.runescapeTypography

@Composable
fun TextInput(
    modifier: Modifier = Modifier,
    state: TextInputState,
    label: String? = null,
    placeholder: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onInputChanged: ((String) -> Unit) = {}
) {
    val text = remember {
        mutableStateOf(TextFieldValue(state.text.orEmpty()))
    }

    TextField(
        modifier = modifier.fillMaxWidth(),
        value = text.value,
        suffix = {
            state.suffix?.let {
                Text(
                    text = state.suffix,
                    style = runescapeTypography.bodyMedium
                )
            }
        },
        textStyle = runescapeTypography.bodyMedium,
        onValueChange = {
            text.value = it
            onInputChanged(it.text)
        },
        label = {
            label?.let {
                Text(
                    text = label,
                    style = runescapeTypography.bodyMedium
                )
            }
        },
        placeholder = {
            placeholder?.let {
                Text(
                    text = placeholder,
                    style = runescapeTypography.bodyMedium
                )
            }
        },
        isError = state.isError(),
        supportingText = {
            if (state.isError()) {
                Text(
                    text = state.error.orEmpty(),
                    style = runescapeTypography.bodySmall
                )
            }
        },
        trailingIcon = {
            if (state.isError()) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "Error"
                )
            }
        },
        keyboardOptions = keyboardOptions
    )
}

@Composable
fun NumberInput(
    modifier: Modifier = Modifier,
    state: TextInputState,
    label: String? = null,
    placeholder: String? = null,
    onInputChanged: ((String) -> Unit) = {}
) {
    TextInput(
        modifier = modifier,
        state = state,
        label = label,
        placeholder = placeholder,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        onInputChanged = onInputChanged
    )
}