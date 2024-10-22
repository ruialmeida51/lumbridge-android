package com.eyther.lumbridge.ui.common.composables.components.card

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.eyther.lumbridge.R
import com.eyther.lumbridge.features.overview.components.TabbedTextAndIcon
import com.eyther.lumbridge.ui.common.composables.model.card.PeekContentCardType
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.HalfPadding

/**
 * A card that displays a preview of the content, with optional actions such
 * as deleting or editing the content.
 *
 * @param modifier The modifier to apply to the card.
 * @param title The title of the content.
 * @param content The content to display in list format (each item in the list is a new line)
 * @param maxItemsToTake The maximum number of items to display in the content list.
 * @param peekContentCardType The type of peek content card to display, determines the icons to be shown before the text
 * @param onClick The action to perform when the card is clicked.
 * @param actions Optional actions to display on the card top-right corner.
 */
@Composable
fun PeekContentCard(
    modifier: Modifier = Modifier,
    title: String,
    content: List<String>,
    maxItemsToTake: Int = 4,
    peekContentCardType: PeekContentCardType,
    onClick: () -> Unit,
    actions: @Composable (RowScope.() -> Unit)? = null,
) {
    val contentSize = remember { mutableStateOf(IntSize.Zero) }

    ColumnCardWrapper(
        horizontalPadding = 0.dp,
        modifier = modifier.then(
            Modifier
                .onGloballyPositioned { contentSize.value = it.size }
        ),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            TabbedTextAndIcon(
                text = title,
                icons = actions,
                textColour = MaterialTheme.colorScheme.onSurface,
                textStyle = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
            )
        }

        content
            .take(maxItemsToTake)
            .forEach { DrawItem(it, peekContentCardType) }

        if (content.size > maxItemsToTake) {
            Text(
                text = stringResource(R.string.and_more, content.size),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun DrawItem(
    item: String,
    peekContentCardType: PeekContentCardType
) {
    val textSize = remember { mutableStateOf(IntSize.Zero) }

    when (peekContentCardType) {
        PeekContentCardType.Checkbox -> {
            val rectangleColour = MaterialTheme.colorScheme.onSurface

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = DefaultPadding)
            ) {
                Canvas(
                    modifier = Modifier
                ) {
                    drawOutline(
                        outline = Outline.Rectangle(
                            Rect(
                                size = Size(HalfPadding.toPx(), HalfPadding.toPx()),
                                offset = Offset(0f, textSize.value.height / 2f - HalfPadding.toPx() / 2f)
                            )
                        ),
                        color = rectangleColour,
                        style = Stroke(width = 1.dp.toPx())
                    )
                }

                Text(
                    modifier = Modifier
                        .padding(start = HalfPadding * 2)
                        .onGloballyPositioned { textSize.value = it.size },
                    text = item,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }
        }

        PeekContentCardType.PlainText -> Text(
            text = item,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
