package com.eyther.lumbridge.ui.common.composables.components.setting

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * A setting composable that just displays a label and an optional icon at the end of the row. The difference between this and [MovementSetting]
 * is that this setting doesn't show an icon at the start of the row and the [composableRight] is optional.
 *
 * @param modifier The modifier to apply to the setting.
 * @param label The label/text to display.
 * @param onClick The action to perform when the setting is clicked.
 * @param composableRight The composable to display at the end of the row, e.g a switch or an icon.
 */
@Composable
fun SimpleSetting(
    modifier: Modifier = Modifier,
    label: String,
    onClick: () -> Unit,
    composableRight: (@Composable () -> Unit)? = null
) {
    Settings(
        modifier = modifier.clickable { onClick() },
        label = label,
        composableRight = composableRight
    )
}
