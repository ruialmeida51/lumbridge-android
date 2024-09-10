package com.eyther.lumbridge.features.editfinancialprofile.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.eyther.lumbridge.R
import com.eyther.lumbridge.model.finance.SalaryInputTypeUi
import com.eyther.lumbridge.ui.common.composables.components.buttons.ChoiceTab
import com.eyther.lumbridge.ui.common.composables.components.card.ColumnCardWrapper
import com.eyther.lumbridge.ui.common.composables.components.input.NumberInput
import com.eyther.lumbridge.ui.common.composables.model.input.ChoiceTabState
import com.eyther.lumbridge.ui.common.composables.model.input.TextInputState
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.HalfPadding

@Composable
fun ColumnScope.SalaryBreakdownInput(
    currencySymbol: String,
    salaryInputChoice: ChoiceTabState,
    selectedTab: Int,
    monthlyGrossSalary: TextInputState,
    annualGrossSalary: TextInputState,
    foodCardPerDiem: TextInputState,
    onSalaryInputTypeChanged: (index: Int) -> Unit,
    onMonthlyGrossSalaryChanged: (Float?) -> Unit,
    onAnnualGrossSalaryChanged: (Float?) -> Unit,
    onFoodCardPerDiemChanged: (Float?) -> Unit
) {
    Text(
        modifier = Modifier
            .padding(start = DefaultPadding, end = DefaultPadding, bottom = HalfPadding)
            .align(Alignment.Start),
        text = stringResource(id = R.string.edit_financial_profile_income),
        style = MaterialTheme.typography.bodyLarge
    )

    ColumnCardWrapper {
        ChoiceTab(
            title = stringResource(id = R.string.edit_financial_profile_choice),
            choiceTabState = salaryInputChoice,
            onOptionClicked = { onSalaryInputTypeChanged(it) }
        )

        Spacer(modifier = Modifier.height(DefaultPadding))

        when (selectedTab) {
            SalaryInputTypeUi.Monthly.ordinal -> MonthlyInput(
                currencySymbol = currencySymbol,
                monthlyGrossSalary = monthlyGrossSalary,
                onMonthlyGrossSalaryChanged = { onMonthlyGrossSalaryChanged(it) }
            )

            SalaryInputTypeUi.Annually.ordinal -> AnnualInput(
                currencySymbol = currencySymbol,
                annualGrossSalary = annualGrossSalary,
                onAnnualGrossSalaryChanged = { onAnnualGrossSalaryChanged(it) }
            )
        }

        Spacer(modifier = Modifier.height(DefaultPadding))

        NumberInput(
            label = stringResource(id = R.string.edit_financial_profile_per_diem_food_card),
            placeholder = "8.60",
            state = foodCardPerDiem,
            onInputChanged = { input -> onFoodCardPerDiemChanged(input.toFloatOrNull()) }
        )
    }
}

@Composable
private fun MonthlyInput(
    currencySymbol: String,
    monthlyGrossSalary: TextInputState,
    onMonthlyGrossSalaryChanged: (Float?) -> Unit
) {
    NumberInput(
        label = stringResource(id = R.string.gross_monthly),
        placeholder = "2500$currencySymbol",
        state = monthlyGrossSalary,
        onInputChanged = { input -> onMonthlyGrossSalaryChanged(input.toFloatOrNull()) }
    )
}

@Composable
private fun AnnualInput(
    currencySymbol: String,
    annualGrossSalary: TextInputState,
    onAnnualGrossSalaryChanged: (Float?) -> Unit
) {
    NumberInput(
        label = stringResource(id = R.string.gross_annual),
        placeholder = "35000$currencySymbol",
        state = annualGrossSalary,
        onInputChanged = { input -> onAnnualGrossSalaryChanged(input.toFloatOrNull()) }
    )
}
