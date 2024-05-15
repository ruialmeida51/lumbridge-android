package com.eyther.lumbridge.features.overview.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import com.eyther.lumbridge.ui.common.composables.text.buildAnnotatedStringTextWithLabel
import com.eyther.lumbridge.ui.theme.runescapeTypography

@Composable
fun DataOverview(
    label: String,
    text: String
) {
    Text(
        text = buildAnnotatedStringTextWithLabel(
            label = label.plus(": "),
            remainingText = text
        ),
        style = runescapeTypography.bodyMedium,
        textAlign = TextAlign.Center
    )
}
