package com.eyther.lumbridge.ui.common.composables.components.input

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.eyther.lumbridge.R
import com.eyther.lumbridge.shared.time.extensions.toIsoLocalDateString
import com.eyther.lumbridge.ui.common.composables.model.input.DateInputState

@Composable
fun DateInput(
    modifier: Modifier = Modifier,
    state: DateInputState,
    enabled: Boolean = true,
    label: String? = null,
    placeholder: String? = null,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect {
            if (it is PressInteraction.Release) {
                onClick()
            }
        }
    }

    Column(
        modifier = modifier
            .then(
                if (enabled) Modifier.clickable { onClick() } else Modifier
            )
    ) {
        TextField(
            enabled = enabled,
            interactionSource = interactionSource,
            modifier = Modifier.fillMaxWidth(),
            value = state.date?.toIsoLocalDateString().orEmpty(),
            textStyle = MaterialTheme.typography.bodyMedium,
            onValueChange = { },
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
                if (state.isError()) {
                    Text(
                        text = state.error!!.getString(context),
                        style = MaterialTheme.typography.bodySmall
                    )
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
            singleLine = true,
            readOnly = true
        )
    }
}
