package com.eyther.lumbridge.features.overview.shared.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.eyther.lumbridge.R
import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.model.finance.NetSalaryUi
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.HalfPadding

@Composable
fun ColumnScope.PerCountryBreakdown(
    netSalaryUi: NetSalaryUi,
    locale: SupportedLocales,
    currencySymbol: String
) {
    Text(
        modifier = Modifier
            .padding(start = DefaultPadding, end = DefaultPadding, bottom = HalfPadding)
            .align(Alignment.Start),
        text = stringResource(id = R.string.financial_overview_deductions),
        style = MaterialTheme.typography.bodyLarge
    )

    when (locale) {
        SupportedLocales.PORTUGAL -> FinancialOverviewPortugal(
            netSalaryUi = netSalaryUi,
            currencySymbol = currencySymbol
        )
    }
}
