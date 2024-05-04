package com.eyther.lumbridge.features.overview.screens.portugal

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.eyther.lumbridge.features.overview.model.FinancialOverviewScreenViewState
import com.eyther.lumbridge.model.finance.DeductionUi
import com.eyther.lumbridge.ui.common.composables.text.buildAnnotatedStringTextWithLabel
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.runescapeTypography

@Composable
fun ColumnScope.FinancialOverviewPortugal(
    state: FinancialOverviewScreenViewState.Content.Overview
) {
    val currencySymbol = remember { state.locale.getCurrencySymbol() }

    DeductionsBreakdown(
        currencySymbol = currencySymbol,
        deductions = state.netSalary.deductions
    )
}

@Composable
fun ColumnScope.DeductionsBreakdown(currencySymbol: String, deductions: List<DeductionUi>) {
    Text(
        modifier = Modifier
            .padding(bottom = DefaultPadding)
            .align(Alignment.Start),
        text = "Deduction Breakdown",
        style = runescapeTypography.titleMedium,
        color = MaterialTheme.colorScheme.onPrimary
    )

    deductions.forEach { deductionUi ->
        val text = buildAnnotatedStringTextWithLabel(
            label = "${deductionUi.label}: ", remainingText = if (deductionUi.hasPercentage()) {
                "${deductionUi.amount}$currencySymbol (${deductionUi.percentage}%)"
            } else {
                "${deductionUi.amount}$currencySymbol"
            }
        )

        Text(
            modifier = Modifier
                .padding(bottom = DefaultPadding)
                .align(Alignment.Start),
            text = text,
            style = runescapeTypography.titleSmall,
            textAlign = TextAlign.Start
        )
    }
}
