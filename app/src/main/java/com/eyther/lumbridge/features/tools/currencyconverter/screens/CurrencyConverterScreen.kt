package com.eyther.lumbridge.features.tools.currencyconverter.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.eyther.lumbridge.R
import com.eyther.lumbridge.extensions.kotlin.forceTwoDecimalsPlaces
import com.eyther.lumbridge.features.overview.components.DataOverview
import com.eyther.lumbridge.features.tools.currencyconverter.model.CurrencyConverterScreenViewState
import com.eyther.lumbridge.features.tools.currencyconverter.viewmodel.CurrencyConverterScreenViewModel
import com.eyther.lumbridge.ui.common.composables.components.buttons.LumbridgeButton
import com.eyther.lumbridge.ui.common.composables.components.card.ColumnCardWrapper
import com.eyther.lumbridge.ui.common.composables.components.input.DropdownInput
import com.eyther.lumbridge.ui.common.composables.components.input.NumberInput
import com.eyther.lumbridge.ui.common.composables.components.loading.LoadingIndicator
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.LumbridgeTopAppBar
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.TopAppBarVariation
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.HalfPadding

@Composable
fun CurrencyConverterScreen(
    navController: NavController,
    @StringRes label: Int,
    viewModel: CurrencyConverterScreenViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            LumbridgeTopAppBar(
                TopAppBarVariation.TitleAndIcon(
                    title = stringResource(id = label),
                    onIconClick = { navController.popBackStack() }
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            when (val state = viewModel.viewState.collectAsStateWithLifecycle().value) {
                is CurrencyConverterScreenViewState.Loading -> LoadingIndicator()

                is CurrencyConverterScreenViewState.Content -> Content(
                    state = state,
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
private fun Content(
    state: CurrencyConverterScreenViewState.Content,
    viewModel: CurrencyConverterScreenViewModel
) {
    Column {
        Input(
            state = state,
            viewModel = viewModel
        )

        Calculation(
            state = state
        )
    }
}

@Composable
private fun ColumnScope.Input(
    state: CurrencyConverterScreenViewState.Content,
    viewModel: CurrencyConverterScreenViewModel
) {
    Text(
        modifier = Modifier
            .padding(
                start = DefaultPadding,
                end = DefaultPadding,
                top = DefaultPadding,
                bottom = HalfPadding
            )
            .align(Alignment.Start),
        text = stringResource(id = R.string.tools_currency_converter_input),
        style = MaterialTheme.typography.bodyLarge
    )

    ColumnCardWrapper {
        DropdownInput(
            label = stringResource(id = R.string.tools_currency_converter_from_currency),
            selectedOption = state.inputState.fromCurrency.getHumanReadableName(),
            items = state.availableCurrencies.map {
                it.currency.currencyCode to it.getHumanReadableName()
            },
            onItemClick = { identifier, _ ->
                viewModel.onFromCurrencyChanged(identifier)
            }
        )

        DropdownInput(
            label = stringResource(id = R.string.tools_currency_converter_to_currency),
            selectedOption = state.inputState.toCurrency.getHumanReadableName(),
            items = state.availableCurrencies.map {
                it.currency.currencyCode to it.getHumanReadableName()
            },
            onItemClick = { identifier, _ ->
                viewModel.onToCurrencyChanged(identifier)
            }
        )

        Spacer(modifier = Modifier.padding(HalfPadding))

        NumberInput(
            label = stringResource(id = R.string.tools_currency_converter_from_amount),
            placeholder = "250",
            state = state.inputState.fromAmount,
            onInputChanged = { viewModel.onFromAmountChanged(it.toFloatOrNull()) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done,
            )
        )

        Spacer(modifier = Modifier.padding(HalfPadding))

        LumbridgeButton(
            label = stringResource(id = R.string.tools_currency_converter_exchange_currency),
            enableButton = state.shouldEnableCalculateButton,
            isLoading = state.isCalculating,
            onClick = viewModel::onConvert
        )
    }
}

@Composable
private fun ColumnScope.Calculation(state: CurrencyConverterScreenViewState.Content) {
    if (state.displayCalculation()) {
        Text(
            modifier = Modifier
                .padding(
                    start = DefaultPadding,
                    end = DefaultPadding,
                    top = DefaultPadding,
                    bottom = HalfPadding
                )
                .align(Alignment.Start),
            text = stringResource(id = R.string.tools_currency_converter_result),
            style = MaterialTheme.typography.bodyLarge
        )

        ColumnCardWrapper {
            when {
                state.hasError -> {
                    Text(
                        text = stringResource(id = R.string.tools_exchange_error),
                        modifier = Modifier.padding(DefaultPadding),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                state.hasExchangeRate() -> {
                    // Display the exchange rate and converted amount
                    DataOverview(
                        label = stringResource(id = R.string.tools_exchange_rate),
                        text = stringResource(
                            id = R.string.tools_exchange_rate_value,
                            state.inputState.fromCurrency.getCurrencySymbol(),
                            requireNotNull(state.exchangeRate).forceTwoDecimalsPlaces(),
                            state.inputState.toCurrency.getCurrencySymbol()
                        )
                    )

                    DataOverview(
                        label = stringResource(id = R.string.tools_currency_converter_to),
                        text = stringResource(
                            id = R.string.tools_currency_converter_to_value,
                            state.inputState.fromAmount.text?.toFloatOrNull()?.forceTwoDecimalsPlaces().orEmpty(),
                            state.inputState.fromCurrency.getCurrencySymbol(),
                            state.toExchangedAmount?.forceTwoDecimalsPlaces().orEmpty(),
                            state.inputState.toCurrency.getCurrencySymbol()
                        )
                    )
                }
            }
        }
    }
}
