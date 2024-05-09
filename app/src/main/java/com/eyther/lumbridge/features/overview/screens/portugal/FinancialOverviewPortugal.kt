package com.eyther.lumbridge.features.overview.screens.portugal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eyther.lumbridge.R
import com.eyther.lumbridge.features.overview.components.Salary
import com.eyther.lumbridge.features.overview.model.FinancialOverviewScreenViewState
import com.eyther.lumbridge.model.finance.DeductionUi
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.QuarterPadding
import com.eyther.lumbridge.ui.theme.runescapeTypography

@Composable
fun FinancialOverviewPortugal(
    state: FinancialOverviewScreenViewState.Content.Overview
) {
    val currencySymbol = remember { state.locale.getCurrencySymbol() }

    DeductionsBreakdown(
        currencySymbol = currencySymbol,
        deductions = state.netSalary.deductions
    )
}

@Composable
fun DeductionsBreakdown(currencySymbol: String, deductions: List<DeductionUi>) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = DefaultPadding)
            .clip(RoundedCornerShape(8.dp))
            .shadow(elevation = QuarterPadding)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(DefaultPadding)
    ) {
        Text(
            modifier = Modifier.padding(bottom = QuarterPadding),
            text = stringResource(id = R.string.financial_overview_deductions_portugal),
            style = runescapeTypography.bodyLarge,
            color = MaterialTheme.colorScheme.tertiary
        )

        deductions.forEach { deductionUi ->
            Salary(
                leftLabel = stringResource(id = deductionUi.label).plus(": "),
                leftText = if (deductionUi.hasPercentage()) {
                    "${deductionUi.amount}$currencySymbol (${deductionUi.percentage}%)"
                } else {
                    "${deductionUi.amount}$currencySymbol"
                }
            )
        }
    }
}
