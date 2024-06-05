package com.eyther.lumbridge.features.editfinancialprofile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.eyther.lumbridge.R
import com.eyther.lumbridge.ui.common.composables.components.input.NumberInput
import com.eyther.lumbridge.ui.common.composables.model.TextInputState
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.DefaultRoundedCorner
import com.eyther.lumbridge.ui.theme.HalfPadding
import com.eyther.lumbridge.ui.theme.QuarterPadding

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
            .padding(vertical = HalfPadding)
            .align(Alignment.Start),
        text = stringResource(id = R.string.edit_financial_profile_allocate_earnings),
        style = MaterialTheme.typography.bodyLarge
    )

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(DefaultRoundedCorner))
            .shadow(elevation = QuarterPadding)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(DefaultPadding)
    ) {
        NumberInput(
            label = stringResource(id = R.string.savings_percentage),
            placeholder = stringResource(id = R.string.edit_financial_profile_suggested, "30"),
            state = savingsPercentage,
            onInputChanged = { input -> onSavingsPercentageChanged(input.toIntOrNull()) }
        )

        Spacer(modifier = Modifier.height(DefaultPadding))

        NumberInput(
            label = stringResource(id = R.string.necessities_percentage),
            placeholder = stringResource(id = R.string.edit_financial_profile_suggested, "50"),
            state = necessitiesPercentage,
            onInputChanged = { input -> onNecessitiesPercentageChanged(input.toIntOrNull()) }
        )

        Spacer(modifier = Modifier.height(DefaultPadding))

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