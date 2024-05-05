package com.eyther.lumbridge.ui.common.composables.components.setting

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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
        label = label
    ) {
        Switch(
            enabled = enabled,
            modifier = Modifier.height(16.dp),
            checked = isChecked,
            onCheckedChange = onCheckedChange
        )
    }
}
