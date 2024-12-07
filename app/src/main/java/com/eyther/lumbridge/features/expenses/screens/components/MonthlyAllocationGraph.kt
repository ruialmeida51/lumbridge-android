package com.eyther.lumbridge.features.expenses.screens.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.eyther.lumbridge.R
import com.eyther.lumbridge.extensions.kotlin.forceTwoDecimalsPlaces
import com.eyther.lumbridge.model.expenses.ExpensesMonthUi
import com.eyther.lumbridge.ui.common.composables.components.progress.LineProgressIndicator
import com.eyther.lumbridge.ui.theme.HalfPadding

@Composable
fun MonthlyAllocationGraph(
    expensesMonthUi: ExpensesMonthUi,
    currencySymbol: String,
    showErrorDisclaimer: Boolean = false
) {
    BoxWithConstraints {
        val boxWithConstraintsScope = this

        Column(
            verticalArrangement = Arrangement.spacedBy(HalfPadding)
        ) {
            if (expensesMonthUi.snapshotAllocations.isEmpty() && showErrorDisclaimer) {
                Text(
                    text = stringResource(id = R.string.expenses_no_snapshot_allocations),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
            } else {
                expensesMonthUi.snapshotAllocations.forEach { allocation ->
                    AllocationItem(
                        availableWidth = boxWithConstraintsScope.maxWidth,
                        iconRes = allocation.iconRes,
                        labelRes = allocation.labelRes,
                        spent = allocation.spent,
                        allocated = allocation.allocated,
                        percentage = allocation.percentage,
                        currencySymbol = currencySymbol
                    )
                }
            }
        }
    }
}

@Composable
private fun AllocationItem(
    availableWidth: Dp,
    @DrawableRes iconRes: Int,
    @StringRes labelRes: Int,
    spent: Float,
    allocated: Float,
    percentage: Float,
    currencySymbol: String
) {
    val labelWidth = availableWidth * 0.22f
    val progressWidth = availableWidth * 0.17f
    val textWidth = availableWidth * 0.60f

    Row(
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier
                .size(16.dp)
                .align(Alignment.CenterVertically)
        )

        Spacer(modifier = Modifier.width(HalfPadding))

        Text(
            modifier = Modifier
                .width(labelWidth),
            text = stringResource(id = labelRes),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Start
        )

        Spacer(modifier = Modifier.width(HalfPadding))

        LineProgressIndicator(
            modifier = Modifier
                .height(8.dp)
                .align(Alignment.CenterVertically)
                .width(progressWidth),
            progressColor = if (percentage > 1.0f) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
            progress = percentage
        )

        Spacer(modifier = Modifier.width(HalfPadding))

        Text(
            modifier = Modifier
                .width(textWidth),
            text = stringResource(
                id = R.string.expenses_spent_versus_allocated,
                "${spent.forceTwoDecimalsPlaces()}$currencySymbol",
                "${allocated.forceTwoDecimalsPlaces()}$currencySymbol"
            ),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            textAlign = TextAlign.End,
            overflow = TextOverflow.Ellipsis
        )
    }
}
