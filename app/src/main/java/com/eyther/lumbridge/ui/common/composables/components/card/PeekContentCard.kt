package com.eyther.lumbridge.ui.common.composables.components.card

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.eyther.lumbridge.features.overview.components.TabbedTextAndIcon
import com.eyther.lumbridge.ui.common.model.text.TextResource

/**
 * A card that displays a preview of the content, with optional actions such
 * as deleting or editing the content.
 *
 * @param title The title of the content.
 * @param content The content to display in list format (each item in the list is a new line)
 * @param actions Optional actions to display on the card top-right corner.
 */
@Composable
fun PeekContentCard(
    modifier: Modifier = Modifier,
    title: TextResource,
    content: List<TextResource>,
    onClick: () -> Unit,
    actions: @Composable (RowScope.() -> Unit)? = null,
) {
    val context = LocalContext.current

    ColumnCardWrapper(
        modifier = modifier,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            TabbedTextAndIcon(
                text = title,
                icons = actions,
                textColour = MaterialTheme.colorScheme.onSurface,
                textStyle = MaterialTheme.typography.labelMedium
            )
        }

        LazyColumn {
            items(content.size) { index ->
                val item = content[index]

                Text(
                    text = item.getString(context),
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}
