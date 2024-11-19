package com.eyther.lumbridge.features.editfinancialprofile.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.eyther.lumbridge.R
import com.eyther.lumbridge.ui.common.composables.components.card.ColumnCardWrapper
import com.eyther.lumbridge.ui.common.composables.components.input.NumberInput
import com.eyther.lumbridge.ui.common.composables.model.input.TextInputState
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.HalfPadding

@Composable
fun ColumnScope.SavingsBreakdownInput(
    savingsPercentage: TextInputState,
    necessitiesPercentage: TextInputState,
    luxuriesPercentage: TextInputState,
    onSavingsPercentageChanged: (Int?) -> Unit,
    onNecessitiesPercentageChanged: (Int?) -> Unit,
    onLuxuriesPercentageChanged: (Int?) -> Unit,
) {
    Text(
        modifier = Modifier
            .padding(start = DefaultPadding, end = DefaultPadding, bottom = HalfPadding)
            .align(Alignment.Start),
        text = stringResource(id = R.string.edit_financial_profile_allocate_earnings),
        style = MaterialTheme.typography.bodyLarge
    )

    ColumnCardWrapper {

        NumberInput(
            label = stringResource(id = R.string.necessities_percentage),
            placeholder = stringResource(id = R.string.edit_financial_profile_suggested, "50"),
            state = necessitiesPercentage,
            onInputChanged = { input -> onNecessitiesPercentageChanged(input.toIntOrNull()) }
        )

        Spacer(modifier = Modifier.height(HalfPadding))

        NumberInput(
            label = stringResource(id = R.string.savings_percentage),
            placeholder = stringResource(id = R.string.edit_financial_profile_suggested, "30"),
            state = savingsPercentage,
            onInputChanged = { input -> onSavingsPercentageChanged(input.toIntOrNull()) }
        )

        Spacer(modifier = Modifier.height(HalfPadding))

        NumberInput(
            label = stringResource(id = R.string.luxuries_percentage),
            placeholder = stringResource(id = R.string.edit_financial_profile_suggested, "20"),
            state = luxuriesPercentage,
            onInputChanged = { input -> onLuxuriesPercentageChanged(input.toIntOrNull()) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            )
        )
    }
}
