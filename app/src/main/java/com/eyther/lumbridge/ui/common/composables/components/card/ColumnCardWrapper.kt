package com.eyther.lumbridge.ui.common.composables.components.card

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.Dp
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.DefaultRoundedCorner
import com.eyther.lumbridge.ui.theme.QuarterPadding

@Composable
fun ColumnCardWrapper(
    modifier: Modifier = Modifier,
    horizontalPadding: Dp = DefaultPadding,
    verticalArrangement: Arrangement.Vertical? = null,
    onClick: (() -> Unit)? = null,
    composable: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier.then(
            Modifier
                .padding(horizontal = horizontalPadding)
                .clip(RoundedCornerShape(DefaultRoundedCorner))
                .shadow(elevation = QuarterPadding)
                .then(
                    if (onClick != null) {
                        Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(bounded = true),
                            onClick = onClick
                        )
                    } else {
                        Modifier
                    }
                )
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .fillMaxWidth()
                .padding(DefaultPadding),
        ),
        verticalArrangement = verticalArrangement ?: Arrangement.Top,
    ) {
        composable()
    }
}
