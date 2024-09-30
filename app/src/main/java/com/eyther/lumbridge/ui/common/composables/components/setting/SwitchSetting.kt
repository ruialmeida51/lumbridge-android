package com.eyther.lumbridge.ui.common.composables.components.setting

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * A setting composable that displays a label and an optional icon at the start of the row. This composable is used to toggle a setting on or off.
 *
 * @param modifier The modifier to apply to the setting.
 * @param icon The icon to display at the start of the row, if any.
 * @param enabled Whether the setting is enabled or not.
 * @param label The label/text to display.
 * @param isChecked The current state of the switch.
 * @param onCheckedChange The action to perform when the switch is toggled.
 */
@Composable
fun SwitchSetting(
    modifier: Modifier = Modifier,
    icon: Int? = null,
    enabled: Boolean = true,
    label: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Settings(
        modifier = modifier,
        icon = icon,
        label = label,
        composableRight = {
            Switch(
                enabled = enabled,
                modifier = Modifier.height(16.dp),
                checked = isChecked,
                onCheckedChange = onCheckedChange
            )
        }
    )
}
