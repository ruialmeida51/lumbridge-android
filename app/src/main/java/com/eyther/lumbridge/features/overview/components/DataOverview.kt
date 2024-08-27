package com.eyther.lumbridge.features.overview.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.eyther.lumbridge.ui.common.composables.text.buildAnnotatedStringTextWithLabel

@Composable
fun DataOverview(
    modifier: Modifier = Modifier,
    label: String,
    text: String,
    textAlign: TextAlign = TextAlign.Start
) {
    Text(
        modifier = modifier,
        text = buildAnnotatedStringTextWithLabel(
            label = label.plus(": "),
            remainingText = text
        ),
        style = MaterialTheme.typography.bodyMedium,
        textAlign = textAlign
    )
}

@Composable
fun RowScope.TabbedDataOverview(
    label: String,
    text: String
) {
    Text(
        text = "$label: ",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.secondary
    )

    Spacer(modifier = Modifier.weight(1f))

    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium
    )
}
