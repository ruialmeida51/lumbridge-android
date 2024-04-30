package com.eyther.lumbridge.features.tools.netsalary.screens.portugal

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.navigation.NavController
import com.eyther.lumbridge.features.tools.netsalary.model.NetSalaryScreenViewState
import com.eyther.lumbridge.model.finance.DeductionUi
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.runescapeTypography

@Composable
fun ColumnScope.SalaryOverviewPortugal(
    navController: NavController,
    state: NetSalaryScreenViewState.Content.Overview,
    onEditSalary: () -> Unit
) {
    val currencySymbol = remember { state.locale.getCurrencySymbol() }

    Text(
        modifier = Modifier
            .padding(bottom = DefaultPadding)
            .align(Alignment.Start),
        text = "Salary Overview",
        style = runescapeTypography.titleMedium,
        color = MaterialTheme.colorScheme.onPrimary
    )

    Text(
        modifier = Modifier
            .padding(bottom = DefaultPadding)
            .align(Alignment.Start),
        text = buildTextWithLabel(
            label = "Gross Annual Salary: ",
            remainingText = "${state.annualGrossSalary}$currencySymbol"
        ),
        style = runescapeTypography.titleSmall
    )

    Text(
        modifier = Modifier
            .padding(bottom = DefaultPadding)
            .align(Alignment.Start),
        text = buildTextWithLabel(
            label = "Net Salary: ",
            remainingText = "${state.netSalary.salary}$currencySymbol"
        ),
        style = runescapeTypography.titleSmall
    )

    Text(
        modifier = Modifier
            .padding(bottom = DefaultPadding)
            .align(Alignment.Start),
        text = buildTextWithLabel(
            label = "Food Card: ",
            remainingText = "${state.netSalary.foodCard}$currencySymbol"
        ),
        style = runescapeTypography.titleSmall
    )

    Spacer(
        modifier = Modifier.height(DefaultPadding)
    )

    DeductionsBreakdown(
        currencySymbol = currencySymbol,
        deductions = state.netSalary.deductions
    )

    Spacer(
        modifier = Modifier
            .height(DefaultPadding)
            .weight(1f, true)
    )

    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = { onEditSalary() }
    ) {
        Text(text = "Edit Salary")
    }

    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary
        ),
        modifier = Modifier.fillMaxWidth(),
        onClick = { navController.navigateUp() }
    ) {
        Text(text = "Other Tools")
    }
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
        val text = buildTextWithLabel(
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


/**
 * Build an annotated string with a label and remaining text. The label will be
 * styled with a different colour from the remaining text, to make it stand out.
 */
@Composable
private fun buildTextWithLabel(label: String, remainingText: String) = buildAnnotatedString {
    withStyle(SpanStyle(color = MaterialTheme.colorScheme.inversePrimary)) {
        append(label)
    }

    append(remainingText)
}
