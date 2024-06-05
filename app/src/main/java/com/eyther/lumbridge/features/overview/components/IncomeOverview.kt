package com.eyther.lumbridge.features.overview.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.eyther.lumbridge.R
import com.eyther.lumbridge.extensions.kotlin.twoDecimalPlaces
import com.eyther.lumbridge.model.finance.NetSalaryUi
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.DefaultRoundedCorner
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
            .padding(bottom = HalfPadding)
            .align(Alignment.Start),
        text = stringResource(id = R.string.financial_overview_salary),
        style = MaterialTheme.typography.bodyLarge
    )

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(DefaultRoundedCorner))
            .shadow(elevation = QuarterPadding)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .fillMaxWidth()
            .padding(DefaultPadding),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                modifier = Modifier.padding(bottom = QuarterPadding),
                text = stringResource(id = R.string.financial_overview_annual_income),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.tertiary
            )

            DataOverview(
                label = stringResource(id = R.string.net_annual),
                text = "${netSalaryUi.annualNetSalary.twoDecimalPlaces()}$currencySymbol"
            )

            DataOverview(
                label = stringResource(id = R.string.gross_annual),
                text = "${netSalaryUi.annualGrossSalary.twoDecimalPlaces()}$currencySymbol"
            )

            Text(
                modifier = Modifier.padding(top = DefaultPadding, bottom = QuarterPadding),
                text = stringResource(id = R.string.financial_overview_monthly_income),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.tertiary
            )

            DataOverview(
                label = stringResource(id = R.string.net_monthly),
                text = "${netSalaryUi.monthlyNetSalary.twoDecimalPlaces()}$currencySymbol"
            )

            DataOverview(
                label = stringResource(id = R.string.gross_monthly),
                text = "${netSalaryUi.monthlyGrossSalary.twoDecimalPlaces()}$currencySymbol"
            )

            Text(
                modifier = Modifier.padding(top = DefaultPadding, bottom = QuarterPadding),
                text = stringResource(id = R.string.food_card),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.tertiary
            )

            DataOverview(
                label = stringResource(id = R.string.financial_overview_food_card_monthly),
                text = "${netSalaryUi.monthlyFoodCard.twoDecimalPlaces()}$currencySymbol"
            )

            DataOverview(
                label = stringResource(id = R.string.financial_overview_food_card_daily),
                text = "${netSalaryUi.dailyFoodCard.twoDecimalPlaces()}$currencySymbol"
            )
        }

        Icon(
            modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(bounded = false)
                ) { onEditClick() },
            painter = painterResource(id = R.drawable.ic_edit),
            contentDescription = stringResource(id = R.string.edit)
        )
    }
}