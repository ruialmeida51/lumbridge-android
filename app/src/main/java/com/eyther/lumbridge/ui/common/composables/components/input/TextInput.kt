@file:OptIn(ExperimentalMaterial3Api::class)

package com.eyther.lumbridge.ui.common.composables.components.input

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import com.eyther.lumbridge.R
import com.eyther.lumbridge.ui.common.composables.model.TextInputState

@Composable
fun TextInput(
    modifier: Modifier = Modifier,
    state: TextInputState,
    label: String? = null,
    placeholder: String? = null,
    maxLength : Int = 128, // 8 bits in a byte = 256 chars
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onInputChanged: ((String) -> Unit) = {}
) {
    val context = LocalContext.current

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
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        textStyle = MaterialTheme.typography.bodyMedium,
        onValueChange = {
            text.value = it.copy(text = it.text.take(maxLength))
            onInputChanged(it.text.take(maxLength))
        },
        label = {
            label?.let {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        placeholder = {
            placeholder?.let {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        isError = state.isError(),
        supportingText = {
            when {
                state.isError() -> {
                    Text(
                        text = state.error!!.getString(context),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                text.value.text.length == maxLength -> {
                    Text(
                        text = context.getString(R.string.max_length_reached, maxLength),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        trailingIcon = {
            if (state.isError()) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = stringResource(id = R.string.error)
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
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Number,
        imeAction = ImeAction.Next
    ),
    maxDigits: Int = 10,
    onInputChanged: ((String) -> Unit) = {}
) {
    TextInput(
        modifier = modifier,
        state = state,
        label = label,
        placeholder = placeholder,
        keyboardOptions = keyboardOptions,
        onInputChanged = onInputChanged,
        maxLength = maxDigits
    )
}
