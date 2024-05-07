package com.eyther.lumbridge.features.overview.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import com.eyther.lumbridge.ui.common.composables.text.buildAnnotatedStringTextWithLabel
import com.eyther.lumbridge.ui.theme.runescapeTypography

@Composable
fun Salary(
    leftLabel: String,
    leftText: String,
    rightLabel: String? = null,
    rightText: String? = null
) {
    Column {
        Text(
            text = buildAnnotatedStringTextWithLabel(
                label = leftLabel,
                remainingText = leftText
            ),
            style = runescapeTypography.bodyMedium,
            textAlign = TextAlign.Center
        )

        if (rightLabel == null || rightText == null) return

        Text(
            text = buildAnnotatedStringTextWithLabel(
                label = rightLabel,
                remainingText = rightText
            ),
            style = runescapeTypography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}
