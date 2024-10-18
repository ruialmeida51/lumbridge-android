package com.eyther.lumbridge.features.overview.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.eyther.lumbridge.features.overview.components.TabbedTextAndIcon.FinalComponents.FinalText
import com.eyther.lumbridge.features.overview.components.TabbedTextAndIcon.PlaceableComponents
import com.eyther.lumbridge.ui.common.model.text.TextResource
import com.eyther.lumbridge.ui.theme.HalfPadding
import com.eyther.lumbridge.ui.theme.QuarterPadding

private object TabbedTextAndIcon {
    /**
     * Represents the placeables that are used in the layout, before the final layout is calculated.
     */
    enum class PlaceableComponents {
        TextSpacer,
        Text,
        IconSpacer,
        Icon
    }

    /**
     * Represents the placeables that are used in the layout, which need to get their size altered based
     * on the available space. By exclusion, if a component is not in this enum, it will be placed as is.
     */
    enum class FinalComponents {
        FinalText
    }
}

/**
 * A composable that displays a text and a row of icons. The text is displayed at the start of the row,
 * and the icons are displayed at the end of the row. The text will take up as much space as it can,
 * and the icons will be displayed to the right of the text, prioritizing the icon's space.
 *
 * @param modifier The modifier to apply to this layout.
 * @param text The text to display at the start of the row.
 * @param startPadding The padding to apply to the start of the row.
 * @param icons The icons to display at the end of the row.
 * @param textColour The colour of the text.
 * @param textStyle The style of the text.
 *
 * @see [TextResource]
 */
@Composable
fun TabbedTextAndIcon(
    modifier: Modifier = Modifier,
    text: TextResource,
    startPadding: Dp = 0.dp,
    icons: @Composable (RowScope.() -> Unit)? = null,
    textColour: Color = MaterialTheme.colorScheme.onSurface,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium
) {
    val context = LocalContext.current

    SubcomposeLayout(
        modifier = modifier
    ) { constraints ->

        // Gather our constraints, and the maximum width and height
        val maxWidth = constraints.maxWidth
        val maxHeight = constraints.maxHeight

        // Helper function to get the constraints for the components to measure.
        // Since they're all the same, we can reuse this function for all components.
        val getConstraintsToMeasure = {
            Constraints(
                maxWidth = maxWidth,
                maxHeight = maxHeight
            )
        }

        // The composable function for our text to be displayed at the start of the row
        val textComposable = @Composable {
            Text(
                modifier = Modifier.padding(start = startPadding),
                text = text.getString(context),
                style = textStyle,
                color = textColour,
                maxLines = 1,
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis
            )
        }

        // The composable function for our spacer to be displayed between the text and the icons
        val textSpacer = @Composable {
            Spacer(modifier = Modifier.width(HalfPadding))
        }

        // The composable function for our optional icon to be displayed at the end of the row
        val iconSpacerComposable = @Composable {
            if (icons != null) {
                Spacer(modifier = Modifier.width(QuarterPadding))
            } else {
                null
            }
        }

        val iconComposable = @Composable {
            if (icons != null) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(HalfPadding)
                ) {
                    icons.invoke(this)
                }
            } else {
                null
            }
        }

        // Measure all of our placeables to determine their size.
        val textSpacerPlaceable = subcompose(PlaceableComponents.TextSpacer) { textSpacer() }.first().measure(getConstraintsToMeasure())
        val textPlaceable = subcompose(PlaceableComponents.Text) { textComposable() }.first().measure(getConstraintsToMeasure())

        // Since icon is optional, we only measure it if it exists, along with the spacer that comes before it.
        val iconSpacerPlaceable = if (icons != null) subcompose(PlaceableComponents.IconSpacer) { iconSpacerComposable() }.first().measure(getConstraintsToMeasure()) else null
        val iconPlaceable = if (icons != null) subcompose(PlaceableComponents.Icon) { iconComposable() }.first().measure(getConstraintsToMeasure()) else null

        // Helper variables to determine the width of the optional icon and the spacer before it.
        val iconSpacerWidth = iconSpacerPlaceable?.width ?: 0
        val iconWidth = iconPlaceable?.width ?: 0
        val iconHeight = iconPlaceable?.height ?: 0

        // The total width that all the components combined occupy
        val combinedWith = textPlaceable.width + textSpacerPlaceable.width + iconWidth

        val textWidth = when {
            // If the combined width of all components exceeds the maximum width and the text value is longer than the icon,
            // we subtract from our maxWidth the spacer's sizes and icon width to get the max size the text value can be,
            // allowing the text to take the remaining space and still displaying the icon.
            combinedWith > maxWidth && textPlaceable.width > iconWidth -> {
                maxWidth - textSpacerPlaceable.width - iconWidth - iconSpacerWidth
            }

            // If the combined width of all components exceeds the maximum width and the label is equal in length to the text value,
            // we divide the maxWidth by 2 and subtract the spacer's size and icon's width to get the max size the label can be,
            // allowing the text value to take up the other half and still displaying the icon.
            combinedWith > maxWidth && textPlaceable.width == iconWidth -> {
                maxWidth / 2 - iconWidth
            }

            // If they both fit without issues, we just use the label's width.
            else -> {
                textPlaceable.width
            }
        }

        val finalTextPlaceable = subcompose(FinalText) { textComposable() }.first().measure(
            Constraints(
                maxWidth = textWidth,
                maxHeight = maxHeight
            )
        )

        // Attempt to calculate the height of the content by taking the max height a component can have.
        val contentHeight = listOf(finalTextPlaceable.height, iconHeight).max()
        val contentMaxHeight = contentHeight.coerceAtMost(maxHeight)

        layout(
            width = maxWidth,
            height = contentMaxHeight
        ) {
            // Calculate the vertical offset to center the content. This will be used for all components to align them vertically.
            // We take the max height the content can be divide it by two to get the center of the row. We then need to subtract
            // the content itself divided by two to place it properly in the middle, otherwise it would be placed at the bottom
            // of the center line.
            val yAlignCenterVertical = contentMaxHeight / 2 - contentHeight / 2

            // Place the text on the left. We use x = 0 to place it at the start of the row.
            finalTextPlaceable.placeRelative(x = 0, y = yAlignCenterVertical)

            // Place the spacer after the text. Spacer has no need for a width constraint, it's always the same size.
            // We use x = finalTextPlaceable.width to place it right after the text.
            textSpacerPlaceable.placeRelative(x = finalTextPlaceable.width, y = yAlignCenterVertical)

            // Place the spacer before the icon on the right if it exists and after the text value. To calculate the x position offset,
            // we need to take our maxAvailable width and subtract the icon's width.
            iconSpacerPlaceable?.placeRelative(x = maxWidth - iconWidth, y = yAlignCenterVertical)

            // Place the icon on the right if it exists. To calculate the x position offset, we need to take our maxAvailable width and
            // subtract the icon's width.
            iconPlaceable?.placeRelative(x = maxWidth - iconWidth, y = yAlignCenterVertical + (contentHeight - iconHeight) / 2)
        }
    }
}
