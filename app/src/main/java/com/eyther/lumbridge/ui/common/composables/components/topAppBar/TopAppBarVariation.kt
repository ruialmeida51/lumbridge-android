package com.eyther.lumbridge.ui.common.composables.components.topAppBar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Represents the available variations for the toolbar.
 */
sealed class TopAppBarVariation(
    open val title: String?,
    open val icon: ImageVector?,
    open val onIconClick: (() -> Unit)?
) {
    data class Title(
        override val title: String
    ) : TopAppBarVariation(
        title = title,
        icon = null,
        onIconClick = null
    )

    data class Icon(
        override val icon: ImageVector,
        override val onIconClick: (() -> Unit)? = null
    ) : TopAppBarVariation(
        title = null,
        icon = icon,
        onIconClick = onIconClick
    )

    data class TitleAndIcon(
        override val title: String,
        override val icon: ImageVector = Icons.AutoMirrored.Filled.ArrowBack,
        override val onIconClick: (() -> Unit)? = null
    ) : TopAppBarVariation(
        title = title,
        icon = icon,
        onIconClick = onIconClick
    )
}
