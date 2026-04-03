@file:OptIn(ExperimentalMaterial3Api::class)

package com.eyther.lumbridge.ui.common.composables.components.input

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import com.eyther.lumbridge.R
import com.eyther.lumbridge.ui.common.composables.model.input.TextInputState

@Composable
fun TextInput(
    modifier: Modifier = Modifier,
    state: TextInputState,
    label: String? = null,
    placeholder: String? = null,
    maxLength: Int = 128,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    supportingTextStyle: TextStyle = MaterialTheme.typography.bodySmall,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    textFieldColors: TextFieldColors = TextFieldDefaults.colors(),
    onInputChanged: ((String) -> Unit) = {}
) {
    val context = LocalContext.current

    val text = remember {
        mutableStateOf(TextFieldValue(state.text.orEmpty()))
    }

    TextField(
        modifier = modifier.fillMaxWidth(),
        colors = textFieldColors,
        value = text.value,
        suffix = {
            state.suffix?.let {
                Text(
                    text = state.suffix,
                    style = textStyle
                )
            }
        },
        textStyle = textStyle,
        onValueChange = {
            if (it.text.filter { char -> char.isLetterOrDigit() }.length > maxLength) return@TextField
            text.value = it.copy(text = it.text)
            onInputChanged(it.text)
        },
        label = {
            label?.let {
                Text(
                    text = label,
                    style = textStyle
                )
            }
        },
        placeholder = {
            placeholder?.let {
                Text(
                    text = placeholder,
                    style = textStyle
                )
            }
        },
        isError = state.isError(),
        supportingText = {
            when {
                state.isError() -> {
                    Text(
                        text = state.error!!.getString(context),
                        style = supportingTextStyle
                    )
                }

                text.value.text.filter { char -> char.isLetterOrDigit() }.length >= maxLength -> {
                    Text(
                        text = context.getString(R.string.max_length_reached, maxLength),
                        style = supportingTextStyle
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
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions
    )
}

@Composable
fun BasicTextInput(
    modifier: Modifier = Modifier,
    state: TextInputState,
    maxLength: Int = 128,
    strikethrough: Boolean = false,
    underline: Boolean = false,
    singleLine: Boolean = false,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    defaultInitialValue: String? = null,
    onInputChanged: ((Int, String) -> Unit) = {_, _ -> },
    onFocusChanged: ((String) -> Unit) = {},
) {
    val initialText = state.text.takeIf { !it.isNullOrEmpty() } ?: defaultInitialValue.orEmpty()

    val text = remember {
        mutableStateOf(
            TextFieldValue(
                text = initialText,
                selection = TextRange(initialText.length)
            )
        )
    }

    if (state.text != text.value.text) {
        text.value = text.value.copy(text = state.text ?: defaultInitialValue.orEmpty())
    }

    BasicTextField(
        modifier = modifier
            .onFocusChanged {
                onFocusChanged(text.value.text)
            },
        textStyle = textStyle.copy(
            textDecoration = when {
                strikethrough -> TextDecoration.LineThrough
                underline -> TextDecoration.Underline
                else -> TextDecoration.None
            },
            color = MaterialTheme.colorScheme.onSurface
        ),
        onValueChange = {
            if (it.text.filter { char -> char.isLetterOrDigit() }.length > maxLength) return@BasicTextField
            text.value = it.copy(text = it.text)
            onInputChanged(text.value.selection.start, text.value.text)
        },
        value = text.value,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine
    )
}

@Composable
fun BasicOutlinedTextInput(
    modifier: Modifier = Modifier,
    state: TextInputState,
    maxLength: Int = 128,
    strikethrough: Boolean = false,
    underline: Boolean = false,
    singleLine: Boolean = false,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    defaultInitialValue: String? = null,
    onInputChanged: ((String) -> Unit) = {},
    onFocusChanged: ((String) -> Unit) = {}
) {
    val initialText = state.text.takeIf { !it.isNullOrEmpty() } ?: defaultInitialValue.orEmpty()

    val text = remember { mutableStateOf(TextFieldValue(text = initialText, selection = TextRange(initialText.length))) }

    if (state.text != text.value.text) {
        text.value = text.value.copy(text = state.text ?: defaultInitialValue.orEmpty())
    }

    OutlinedTextField(
        modifier = modifier
            .onFocusChanged {
                onFocusChanged(text.value.text)
            },
        textStyle = textStyle.copy(
            textDecoration = when {
                strikethrough -> TextDecoration.LineThrough
                underline -> TextDecoration.Underline
                else -> TextDecoration.None
            },
            color = MaterialTheme.colorScheme.onSurface
        ),
        onValueChange = {
            if (it.text.filter { char -> char.isLetterOrDigit() }.length > maxLength) return@OutlinedTextField
            text.value = it.copy(text = it.text)
            onInputChanged(text.value.text)
        },
        value = text.value,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine
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
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    textFieldColors: TextFieldColors = TextFieldDefaults.colors(),
    maxDigits: Int = 10,
    onInputChanged: ((String) -> Unit) = {}
) {
    TextInput(
        modifier = modifier,
        state = state,
        label = label,
        placeholder = placeholder,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        onInputChanged = onInputChanged,
        textFieldColors = textFieldColors,
        maxLength = maxDigits
    )
}
