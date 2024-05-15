package com.eyther.lumbridge.ui.common.composables.components.input

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.eyther.lumbridge.R
import com.eyther.lumbridge.domain.time.toIsoLocalDateString
import com.eyther.lumbridge.ui.common.composables.model.DateInputState
import com.eyther.lumbridge.ui.theme.runescapeTypography


@Composable
fun DateInput(
    modifier: Modifier = Modifier,
    state: DateInputState,
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
            .fillMaxWidth()
            .clickable { onClick() },
    ) {
        TextField(
            interactionSource = interactionSource,
            modifier = modifier.fillMaxWidth(),
            value = state.date?.toIsoLocalDateString().orEmpty(),
            textStyle = runescapeTypography.bodyMedium,
            onValueChange = { },
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
                        text = state.error!!.getString(context),
                        style = runescapeTypography.bodySmall
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