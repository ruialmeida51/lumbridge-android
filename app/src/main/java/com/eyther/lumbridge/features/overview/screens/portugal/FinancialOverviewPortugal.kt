package com.eyther.lumbridge.features.overview.screens.portugal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import com.eyther.lumbridge.R
import com.eyther.lumbridge.extensions.kotlin.twoDecimalPlaces
import com.eyther.lumbridge.features.overview.components.DataOverview
import com.eyther.lumbridge.model.finance.DeductionUi
import com.eyther.lumbridge.model.finance.NetSalaryUi
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.DefaultRoundedCorner
import com.eyther.lumbridge.ui.theme.QuarterPadding

@Composable
fun FinancialOverviewPortugal(
    netSalaryUi: NetSalaryUi,
    currencySymbol: String
) {
    DeductionsBreakdown(
        currencySymbol = currencySymbol,
        deductions = netSalaryUi.deductions
    )
}

@Composable
private fun DeductionsBreakdown(currencySymbol: String, deductions: List<DeductionUi>) {
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(DefaultRoundedCorner))
            .shadow(elevation = QuarterPadding)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(DefaultPadding)
    ) {
        Text(
            modifier = Modifier.padding(bottom = QuarterPadding),
            text = stringResource(id = R.string.financial_overview_deductions_portugal),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.tertiary
        )

        deductions.forEach { deductionUi ->
            DataOverview(
                label = stringResource(id = deductionUi.label),
                text = if (deductionUi.hasPercentage()) {
                    "${deductionUi.amount.twoDecimalPlaces()}$currencySymbol (${deductionUi.percentage}%)"
                } else {
                    "${deductionUi.amount.twoDecimalPlaces()}$currencySymbol"
                }
            )
        }
    }
}
