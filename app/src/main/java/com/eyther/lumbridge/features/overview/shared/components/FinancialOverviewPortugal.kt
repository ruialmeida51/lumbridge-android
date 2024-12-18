package com.eyther.lumbridge.features.overview.shared.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.eyther.lumbridge.R
import com.eyther.lumbridge.extensions.kotlin.forceTwoDecimalsPlaces
import com.eyther.lumbridge.model.finance.DeductionUi
import com.eyther.lumbridge.model.finance.DuodecimosTypeUi
import com.eyther.lumbridge.model.finance.NetSalaryUi
import com.eyther.lumbridge.ui.common.composables.components.card.ColumnCardWrapper
import com.eyther.lumbridge.ui.common.composables.components.text.TabbedDataOverview
import com.eyther.lumbridge.ui.theme.HalfPadding
import com.eyther.lumbridge.ui.theme.QuarterPadding

@Composable
fun FinancialOverviewPortugal(
    netSalaryUi: NetSalaryUi,
    duodecimosTypeUi: DuodecimosTypeUi,
    currencySymbol: String
) {
    DeductionsBreakdown(
        currencySymbol = currencySymbol,
        duodecimosTypeUi = duodecimosTypeUi,
        deductions = netSalaryUi.deductions
    )
}

@Composable
private fun DeductionsBreakdown(
    currencySymbol: String,
    duodecimosTypeUi: DuodecimosTypeUi,
    deductions: List<DeductionUi>
) {
    ColumnCardWrapper {
        Text(
            modifier = Modifier.padding(bottom = QuarterPadding),
            text = stringResource(id = R.string.financial_overview_deductions_portugal),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.tertiary
        )

        deductions.forEach { deductionUi ->
            TabbedDataOverview(
                label = stringResource(id = deductionUi.label),
                text = if (deductionUi.hasPercentage()) {
                    "${deductionUi.amount.forceTwoDecimalsPlaces()}$currencySymbol (${deductionUi.percentage}%)"
                } else {
                    "${deductionUi.amount.forceTwoDecimalsPlaces()}$currencySymbol"
                }
            )
        }

        TabbedDataOverview(
            modifier = Modifier.padding(top = HalfPadding),
            label = stringResource(R.string.duodecimos),
            text = stringResource(duodecimosTypeUi.label)
        )
    }
}
