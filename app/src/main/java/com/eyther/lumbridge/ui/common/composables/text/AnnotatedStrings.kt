package com.eyther.lumbridge.ui.common.composables.text

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

/**
 * Build an annotated string with a label and remaining text. The label will be
 * styled with a different colour from the remaining text, to make it stand out.
 */
@Composable
fun buildAnnotatedStringTextWithLabel(label: String, remainingText: String) = buildAnnotatedString {
    withStyle(SpanStyle(color = MaterialTheme.colorScheme.inversePrimary)) {
        append(label)
    }

    append(remainingText)
}