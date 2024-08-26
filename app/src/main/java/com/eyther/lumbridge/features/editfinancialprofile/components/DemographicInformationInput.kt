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
import com.eyther.lumbridge.ui.common.composables.components.card.ColumnCardWrapper
import com.eyther.lumbridge.ui.common.composables.components.input.NumberInput
import com.eyther.lumbridge.ui.common.composables.components.setting.SwitchSetting
import com.eyther.lumbridge.ui.common.composables.model.TextInputState
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.HalfPadding

@Composable
fun ColumnScope.DemographicInformationInput(
    handicapped: Boolean,
    married: Boolean,
    singleIncome: Boolean,
    numberOfDependants: TextInputState,
    onHandicappedChanged: (Boolean) -> Unit,
    onMarriedChanged: (Boolean) -> Unit,
    onSingleIncomeChanged: (Boolean) -> Unit,
    onNumberOfDependantsChanged: (Int?) -> Unit
) {
    Text(
        modifier = Modifier
            .padding(start = DefaultPadding, end = DefaultPadding, top = HalfPadding, bottom = HalfPadding)
            .align(Alignment.Start),
        text = stringResource(id = R.string.edit_financial_profile_household),
        style = MaterialTheme.typography.bodyLarge
    )

    ColumnCardWrapper {
        SwitchSetting(
            label = stringResource(id = R.string.handicapped),
            isChecked = handicapped,
            onCheckedChange = { onHandicappedChanged(it) }
        )

        Spacer(modifier = Modifier.height(DefaultPadding))

        SwitchSetting(
            label = stringResource(id = R.string.married),
            isChecked = married,
            onCheckedChange = { onMarriedChanged(it) }
        )

        Spacer(modifier = Modifier.height(DefaultPadding))

        SwitchSetting(
            label = stringResource(id = R.string.single_incomne),
            enabled = married,
            isChecked = singleIncome,
            onCheckedChange = { onSingleIncomeChanged(it) }
        )

        Spacer(modifier = Modifier.height(DefaultPadding))

        NumberInput(
            label = stringResource(id = R.string.number_of_dependants),
            placeholder = "0",
            state = numberOfDependants,
            onInputChanged = { input ->
                onNumberOfDependantsChanged(input.toIntOrNull())
            }
        )
    }
}
