package com.eyther.lumbridge.ui.common.composables.components.text

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.eyther.lumbridge.ui.common.composables.components.text.PlaceableComponents.Icon
import com.eyther.lumbridge.ui.common.composables.components.text.PlaceableComponents.IconSpacer
import com.eyther.lumbridge.ui.common.composables.components.text.PlaceableComponents.Label
import com.eyther.lumbridge.ui.common.composables.components.text.PlaceableComponents.Text
import com.eyther.lumbridge.ui.common.composables.components.text.PlaceableComponents.TextSpacer
import com.eyther.lumbridge.ui.theme.HalfPadding
import com.eyther.lumbridge.ui.theme.QuarterPadding

/**
 * Represents the placeables that are used in the layout, before the final layout is calculated.
 */
private enum class PlaceableComponents {
    Label,
    TextSpacer,
    Text,
    IconSpacer,
    Icon
}

/**
 * Represents the placeables that are used in the layout, which need to get their size altered based
 * on the available space. By exclusion, if a component is not in this enum, it will be placed as is.
 */
private enum class FinalComponents {
    FinalLabel,
    FinalText
}

/**
 * A composable that displays a label and a text value in a row, with an optional icon at the end.
 *
 * This composable is designed to be used in a list of data overviews, where each row displays a label
 * and a text value. The label is displayed at the start of the row, followed by a spacer, and then the text value.
 * If an icon is provided, it is displayed at the end of the row, after the text value.
 *
 * It tries to balance out the space between the label and the text value, and if the combined width
 * of the label and text value exceeds the available width, it will try to shrink the longer of the two
 * to fit the available space, considering the icon's width if it exists.
 *
 * @param modifier The modifier to be applied to the layout.
 * @param label The label to be displayed at the start of the row.
 * @param text The text value to be displayed at the end of the row.
 * @param startPadding The padding to be applied at the start of the row.
 * @param icon The optional icon to be displayed at the end of the row.
 * @param labelStyle The style to be applied to the label.
 * @param labelColour The colour to be applied to the label.
 * @param textColour The colour to be applied to the text value.
 * @param textStyle The style to be applied to the text value.
 */
@Composable
fun TabbedDataOverview(
    modifier: Modifier = Modifier,
    label: String,
    text: String,
    startPadding: Dp = 0.dp,
    icon: @Composable (() -> Unit)? = null,
    labelStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    labelColour: Color = MaterialTheme.colorScheme.secondary,
    textColour: Color = MaterialTheme.colorScheme.onSurface,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium
) {
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

        // The composable function for our label to be displayed at the start of the row
        val labelComposable = @Composable {
            Text(
                modifier = Modifier.padding(start = startPadding),
                text = "$label ",
                style = labelStyle,
                color = labelColour,
                maxLines = 1,
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis
            )
        }

        // The composable function for our spacer to be displayed between the label and text
        val textSpacer = @Composable {
            Spacer(modifier = Modifier.width(HalfPadding))
        }

        // The composable function for our text (value) to be displayed at the end of the row
        val textComposable = @Composable {
            Text(
                text = text,
                style = textStyle,
                color = textColour,
                maxLines = 1,
                textAlign = TextAlign.End,
                overflow = TextOverflow.Ellipsis
            )
        }

        // The composable function for our optional icon to be displayed at the end of the row
        val iconSpacerComposable = @Composable {
            if (icon != null) {
                Spacer(modifier = Modifier.width(QuarterPadding))
            } else {
                null
            }
        }

        // Measure all of our placeables to determine their size.
        val labelPlaceable = subcompose(Label) { labelComposable() }.first().measure(getConstraintsToMeasure())
        val textSpacerPlaceable = subcompose(TextSpacer) { textSpacer() }.first().measure(getConstraintsToMeasure())
        val textPlaceable = subcompose(Text) { textComposable() }.first().measure(getConstraintsToMeasure())

        // Since icon is optional, we only measure it if it exists, along with the spacer that comes before it.
        val iconSpacerPlaceable = if (icon != null) subcompose(IconSpacer) { iconSpacerComposable() }.first().measure(getConstraintsToMeasure()) else null
        val iconPlaceable = if (icon != null) subcompose(Icon) { icon() }.first().measure(getConstraintsToMeasure()) else null

        // Helper variables to determine the width of the optional icon and the spacer before it.
        val iconSpacerWidth = iconSpacerPlaceable?.width ?: 0
        val iconWidth = iconPlaceable?.width ?: 0
        val iconHeight = iconPlaceable?.height ?: 0

        // The total width that all the components combined occupy
        val combinedWith = labelPlaceable.width + textPlaceable.width + textSpacerPlaceable.width + iconWidth

        val labelWidth = when {
            // If the combined width of all components exceeds the maximum width and the label is longer than the text value,
            // we subtract from our maxWidth the spacer's sizes, icon and text value's width to get the max size the label can be,
            // allowing the text value to take the remaining space and still displaying the icon.
            combinedWith > maxWidth && labelPlaceable.width > textPlaceable.width -> {
                maxWidth - textSpacerPlaceable.width - textPlaceable.width - iconSpacerWidth - iconWidth
            }

            // If the combined width of all components exceeds the maximum width and the label is equal in length to the text value,
            // we divide the maxWidth by 2 and subtract the spacer's size and icon's width to get the max size the label can be,
            // allowing the text value to take up the other half and still displaying the icon.
            combinedWith > maxWidth && labelPlaceable.width == textPlaceable.width -> {
                maxWidth / 2 - textSpacerPlaceable.width - iconWidth
            }

            // If they both fit without issues, we just use the label's width.
            else -> {
                labelPlaceable.width
            }
        }

        val textWidth = when {
            // If the combined width of all components exceeds the maximum width and the text value is longer than the label,
            // we subtract from our maxWidth the spacer's sizes, icon and label's width to get the max size the text value can be,
            // allowing the label to take the remaining space and still displaying the icon.
            combinedWith > maxWidth && textPlaceable.width > labelPlaceable.width -> {
                maxWidth - textSpacerPlaceable.width - labelPlaceable.width - iconWidth
            }

            // If the combined width of all components exceeds the maximum width and the text value is equal in length to the label,
            // we divide the maxWidth by 2. We don't subtract the other sizes here because that work was already done in the label's
            // width calculation, as it left enough space for the text value to take up the other half.
            combinedWith > maxWidth && textPlaceable.width == labelPlaceable.width -> {
                maxWidth / 2
            }

            // If they both fit without issues, we just use the text value's width.
            else -> {
                textPlaceable.width
            }
        }

        val finalLabelPlaceable = subcompose(FinalComponents.FinalLabel) { labelComposable() }.first().measure(
            Constraints(
                maxWidth = labelWidth,
                maxHeight = maxHeight
            )
        )

        val finalTextPlaceable = subcompose(FinalComponents.FinalText) { textComposable() }.first().measure(
            Constraints(
                maxWidth = textWidth,
                maxHeight = maxHeight
            )
        )

        // Attempt to calculate the height of the content by taking the max height a component can have.
        val contentHeight = listOf(finalLabelPlaceable.height, finalTextPlaceable.height, iconHeight).max()
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

            // Place the label on the left. We use x = 0 to place it at the start of the row.
            finalLabelPlaceable.placeRelative(x = 0, y = yAlignCenterVertical)

            // Place the spacer after the label. Spacer has no need for a width constraint, it's always the same size.
            // We use x = finalLabelPlaceable.width to place it right after the label.
            textSpacerPlaceable.placeRelative(x = finalLabelPlaceable.width, y = yAlignCenterVertical)

            // Place the value on the right but left of the icons if they exist.  To calculate the x position offset, we need to take
            // our maxAvailable width and subtract other components' that can appear to the right of the text value, such as the icon
            // and its spacer.
            finalTextPlaceable.placeRelative(x = maxWidth - finalTextPlaceable.width - iconSpacerWidth - iconWidth, y = yAlignCenterVertical)

            // Place the spacer before the icon on the right if it exists and after the text value. To calculate the x position offset,
            // we need to take our maxAvailable width and subtract the icon's width.
            iconSpacerPlaceable?.placeRelative(x = maxWidth - iconWidth, y = yAlignCenterVertical)

            // Place the icon on the right if it exists. To calculate the x position offset, we need to take our maxAvailable width and
            // subtract the icon's width.
            iconPlaceable?.placeRelative(x = maxWidth - iconWidth, y = yAlignCenterVertical + (contentHeight - iconHeight) / 2)
        }
    }
}
