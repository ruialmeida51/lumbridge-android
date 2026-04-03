package com.eyther.lumbridge.ui.common.composables.components.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.eyther.lumbridge.ui.common.composables.model.input.ChoiceTabState
import com.eyther.lumbridge.ui.theme.DefaultRoundedCorner
import com.eyther.lumbridge.ui.theme.HalfPadding
import com.eyther.lumbridge.ui.theme.QuarterPadding

@Composable
fun ChoiceTab(
    modifier: Modifier = Modifier,
    title: String = "",
    choiceTabState: ChoiceTabState,
    minHeight: Dp = 40.dp,
    onOptionClicked: (index: Int) -> Unit
) {
    val backgroundColor: @Composable (isSelected: Boolean) -> Color = { isSelected ->
        if (isSelected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        }
    }

    val textColor: @Composable (isSelected: Boolean) -> Color = { isSelected ->
        if (isSelected) {
            MaterialTheme.colorScheme.onPrimary
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        }
    }

    if (!title.isEmpty()) {
        Text(
            modifier = Modifier.padding(bottom = HalfPadding),
            text = title,
            style = MaterialTheme.typography.bodyMedium
        )
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = minHeight)
            .background(
                MaterialTheme.colorScheme.surfaceVariant,
                RoundedCornerShape(DefaultRoundedCorner)
            )
            .padding(QuarterPadding),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        choiceTabState.tabsStringRes.forEachIndexed { index, tabRes ->
            Text(
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        onOptionClicked(index)
                    }
                    .background(
                        color = backgroundColor(choiceTabState.isSelected(index)),
                        shape = RoundedCornerShape(DefaultRoundedCorner)
                    )
                    .padding(QuarterPadding),
                text = stringResource(id = tabRes),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = textColor(choiceTabState.isSelected(index))
            )
        }
    }
}
