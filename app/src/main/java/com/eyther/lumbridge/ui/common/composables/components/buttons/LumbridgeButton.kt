package com.eyther.lumbridge.ui.common.composables.components.buttons

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.MinButtonHeight

@Composable
fun LumbridgeButton(
    modifier: Modifier = Modifier,
    label: String,
    enableButton: Boolean = true,
    isLoading: Boolean = false,
    minHeight: Dp = MinButtonHeight,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = minHeight),
        enabled = enableButton,
        onClick = onClick
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .height(minHeight / 2)
                    .aspectRatio(1f),
                color = MaterialTheme.colorScheme.onPrimary
            )

        } else {
            Text(
                modifier = Modifier.padding(horizontal = DefaultPadding),
                text = label
            )
        }
    }
}
