package com.eyther.lumbridge.ui.common.composables.components.topAppBar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.eyther.lumbridge.ui.theme.DefaultPadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LumbridgeTopAppBar(
    topAppBarVariation: TopAppBarVariation,
    showIcons: Boolean = true,
    actions: @Composable (RowScope.() -> Unit)? = null
) {
    TopAppBar(
        navigationIcon = {
            when (topAppBarVariation) {
                is TopAppBarVariation.Icon -> Icon(
                    modifier = Modifier
                        .clickable { topAppBarVariation.onIconClick?.invoke() }
                        .padding(horizontal = DefaultPadding),
                    imageVector = topAppBarVariation.icon,
                    contentDescription = null
                )

                is TopAppBarVariation.TitleAndIcon -> Icon(
                    modifier = Modifier
                        .clickable { topAppBarVariation.onIconClick?.invoke() }
                        .padding(horizontal = DefaultPadding),
                    imageVector = topAppBarVariation.icon,
                    contentDescription = topAppBarVariation.title
                )

                is TopAppBarVariation.Title -> Unit
            }
        },
        title = {
            when (topAppBarVariation) {
                is TopAppBarVariation.Title -> Text(
                    modifier = Modifier.padding(horizontal = DefaultPadding),
                    text = topAppBarVariation.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                is TopAppBarVariation.TitleAndIcon -> Text(
                    text = topAppBarVariation.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                is TopAppBarVariation.Icon -> Unit
            }
        },
        actions = {

            AnimatedVisibility(showIcons) {
                Row(
                    modifier = Modifier.padding(horizontal = DefaultPadding),
                    horizontalArrangement = Arrangement.spacedBy(DefaultPadding)
                ) {
                    actions?.invoke(this@Row)
                }
            }
        }
    )
}
