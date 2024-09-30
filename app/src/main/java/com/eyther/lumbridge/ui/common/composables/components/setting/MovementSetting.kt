package com.eyther.lumbridge.ui.common.composables.components.setting

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * A setting composable that displays a label and an optional icon at the start of the row. This composable is used to navigate to another screen,
 * and always shows a right arrow icon at the end of the row.
 *
 * @param modifier The modifier to apply to the setting.
 * @param label The label/text to display.
 * @param onClick The action to perform when the setting is clicked.
 * @param icon The icon to display at the start of the row.
 */
@Composable
fun MovementSetting(
    modifier: Modifier = Modifier,
    label: String,
    onClick: () -> Unit,
    @DrawableRes icon: Int? = null
) {
    Settings(
        icon = icon,
        modifier = modifier.clickable { onClick() },
        label = label
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
            contentDescription = label
        )
    }
}
