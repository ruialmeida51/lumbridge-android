package com.eyther.lumbridge.features.overview.shared.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eyther.lumbridge.R
import com.eyther.lumbridge.extensions.kotlin.forceTwoDecimalsPlaces
import com.eyther.lumbridge.model.finance.NetSalaryUi
import com.eyther.lumbridge.ui.common.composables.components.card.ColumnCardWrapper
import com.eyther.lumbridge.ui.common.composables.components.text.TabbedDataOverview
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.HalfPadding
import com.eyther.lumbridge.ui.theme.QuarterPadding

@Composable
fun ColumnScope.IncomeOverview(
    netSalaryUi: NetSalaryUi,
    onEditClick: () -> Unit,
    currencySymbol: String
) {
    Text(
        modifier = Modifier
            .padding(start = DefaultPadding, end = DefaultPadding, bottom = HalfPadding)
            .align(Alignment.Start),
        text = stringResource(id = R.string.financial_overview_salary),
        style = MaterialTheme.typography.bodyLarge
    )

    ColumnCardWrapper {
        Row {
            Text(
                modifier = Modifier.padding(bottom = QuarterPadding),
                text = stringResource(id = R.string.financial_overview_annual_income),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.tertiary
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                modifier = Modifier
                    .size(20.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(bounded = false)
                    ) { onEditClick() },
                painter = painterResource(id = R.drawable.ic_edit),
                contentDescription = stringResource(id = R.string.edit)
            )
        }

        TabbedDataOverview(
            label = stringResource(id = R.string.net_annual),
            text = "${netSalaryUi.annualNetSalary.forceTwoDecimalsPlaces()}$currencySymbol"
        )

        TabbedDataOverview(
            label = stringResource(id = R.string.gross_annual),
            text = "${netSalaryUi.annualGrossSalary.forceTwoDecimalsPlaces()}$currencySymbol"
        )

        Text(
            modifier = Modifier.padding(top = DefaultPadding, bottom = QuarterPadding),
            text = stringResource(id = R.string.financial_overview_monthly_income),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.tertiary
        )

        TabbedDataOverview(
            label = stringResource(id = R.string.net_monthly),
            text = "${netSalaryUi.monthlyNetSalary.forceTwoDecimalsPlaces()}$currencySymbol"
        )

        TabbedDataOverview(
            label = stringResource(id = R.string.gross_monthly),
            text = "${netSalaryUi.monthlyGrossSalary.forceTwoDecimalsPlaces()}$currencySymbol"
        )

        Text(
            modifier = Modifier.padding(top = DefaultPadding, bottom = QuarterPadding),
            text = stringResource(id = R.string.food_card),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.tertiary
        )

        TabbedDataOverview(
            label = stringResource(id = R.string.financial_overview_food_card_monthly),
            text = "${netSalaryUi.monthlyFoodCard.forceTwoDecimalsPlaces()}$currencySymbol"
        )

        TabbedDataOverview(
            label = stringResource(id = R.string.financial_overview_food_card_daily),
            text = "${netSalaryUi.dailyFoodCard.forceTwoDecimalsPlaces()}$currencySymbol"
        )
    }
}
