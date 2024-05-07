package com.eyther.lumbridge.ui.common.composables.components.setting

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

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
