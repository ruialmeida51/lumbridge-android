package com.eyther.lumbridge.ui.common.composables.components.loan

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.eyther.lumbridge.R
import com.eyther.lumbridge.extensions.kotlin.forceTwoDecimalsPlaces
import com.eyther.lumbridge.model.loan.LoanCalculationUi
import com.eyther.lumbridge.model.loan.LoanInterestRateUi
import com.eyther.lumbridge.model.loan.LoanUi
import com.eyther.lumbridge.ui.common.composables.components.card.ColumnCardWrapper
import com.eyther.lumbridge.ui.common.composables.components.progress.LineProgressIndicator
import com.eyther.lumbridge.ui.common.composables.components.setting.MovementSetting
import com.eyther.lumbridge.ui.common.composables.components.text.TabbedDataOverview
import com.eyther.lumbridge.ui.common.composables.components.text.TabbedTextAndIcon
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.HalfPadding
import com.eyther.lumbridge.ui.theme.QuarterPadding

/**
 * Creates a loan card with a prettify graphic to represent the
 * remaining values and
 */
@Composable
fun PeekLoanCard(
    loanUi: LoanUi,
    loanCalculationUi: LoanCalculationUi,
    currencySymbol: String,
    onCardClick: (Long) -> Unit,
    onDelete: (Long) -> Unit,
) {
    ColumnCardWrapper(
        onClick = { onCardClick(loanUi.id) }
    ) {
        Row(
            modifier = Modifier.padding(bottom = HalfPadding)
        ) {
            Icon(
                modifier = Modifier
                    .size(20.dp)
                    .align(Alignment.CenterVertically),
                painter = painterResource(id = loanUi.loanCategoryUi.icon),
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(QuarterPadding))

            TabbedTextAndIcon(
                modifier = Modifier.padding(end = QuarterPadding),
                text = loanUi.name,
                textStyle = MaterialTheme.typography.bodyLarge,
                textColour = MaterialTheme.colorScheme.tertiary,
                icons = {
                    Icon(
                        modifier = Modifier
                            .size(20.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = rememberRipple(bounded = false),
                                onClick = { onDelete(loanUi.id) }
                            ),
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = stringResource(id = R.string.delete)
                    )
                }
            )
        }

        TabbedDataOverview(
            label = stringResource(id = R.string.breakdown_loans_next_payment),
            text = "${loanCalculationUi.monthlyPayment.forceTwoDecimalsPlaces()}$currencySymbol",
        )

        TabbedDataOverview(
            label = stringResource(id = R.string.breakdown_loans_months_left),
            text = loanCalculationUi.remainingMonths.toString(),
        )

        LineProgressIndicator(
            modifier = Modifier
                .padding(top = DefaultPadding)
                .height(8.dp),
            progress = 1 - (loanUi.currentLoanAmount / loanUi.initialLoanAmount),
        )

        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = DefaultPadding, bottom = QuarterPadding),
            text = stringResource(
                id = R.string.breakdown_loans_paid_amount,
                "${loanUi.paidLoanAmount.forceTwoDecimalsPlaces()}$currencySymbol",
                "${loanUi.initialLoanAmount.forceTwoDecimalsPlaces()}$currencySymbol"
            ),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            modifier = Modifier.padding(top = DefaultPadding, bottom = QuarterPadding),
            text = stringResource(id = R.string.breakdown_loans_interest_rate),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.tertiary
        )

        when (val loanInterestRateUi = loanUi.loanInterestRateUi) {
            is LoanInterestRateUi.Fixed -> {
                TabbedDataOverview(
                    label = stringResource(id = loanInterestRateUi.label),
                    text = "${loanInterestRateUi.interestRate}%"
                )
            }

            is LoanInterestRateUi.Variable -> {
                TabbedDataOverview(
                    label = stringResource(id = R.string.euribor),
                    text = "${loanInterestRateUi.euribor}%"
                )

                TabbedDataOverview(
                    label = stringResource(id = R.string.spread),
                    text = "${loanInterestRateUi.spread}%"
                )
            }
        }

        MovementSetting(
            modifier = Modifier.padding(top = DefaultPadding),
            label = stringResource(id = R.string.breakdown_tap_to_view_more)
        )
    }
}
