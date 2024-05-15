package com.eyther.lumbridge.ui.common.composables.components.buttons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.eyther.lumbridge.ui.theme.DefaultPadding

@Composable
fun LumbridgeButton(
    modifier: Modifier = Modifier,
    label: String,
    enableButton: Boolean = true,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier.fillMaxWidth(),
        enabled = enableButton,
        onClick = onClick
    ) {
        Text(
            modifier = Modifier.padding(horizontal = DefaultPadding),
            text = label
        )
    }
}