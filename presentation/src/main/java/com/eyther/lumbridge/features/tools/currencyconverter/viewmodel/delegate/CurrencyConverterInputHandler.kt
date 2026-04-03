package com.eyther.lumbridge.features.tools.currencyconverter.viewmodel.delegate

import com.eyther.lumbridge.R
import com.eyther.lumbridge.domain.model.currencyexchange.SupportedCurrencies
import com.eyther.lumbridge.extensions.kotlin.getErrorOrNull
import com.eyther.lumbridge.features.editfinancialprofile.model.EditFinancialProfileScreenViewState.Content
import com.eyther.lumbridge.features.tools.currencyconverter.model.CurrencyConverterInputState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class CurrencyConverterInputHandler @Inject constructor() : ICurrencyConverterInputHandler {
    override val inputState = MutableStateFlow(CurrencyConverterInputState())

    override fun onFromCurrencyChanged(fromCurrencyCode: String) {
        updateInput { state ->
            state.copy(
                fromCurrency = SupportedCurrencies.get(fromCurrencyCode)
            )
        }
    }

    override fun onToCurrencyChanged(toCurrencyCode: String) {
        updateInput { state ->
            state.copy(
                toCurrency = SupportedCurrencies.get(toCurrencyCode)
            )
        }
    }

    override fun onFromAmountChanged(fromAmount: Float?) {
        updateInput { state ->
            val amount = fromAmount?.toString()

            state.copy(
                fromAmount = state.fromAmount.copy(
                    text = amount,
                    error = amount.getErrorOrNull(
                        R.string.tools_currency_converter_amount_error
                    )
                )
            )
        }
    }

    /**
     * Checks if the save button should be enabled.
     *
     * The button should be enabled if the user has entered valid data.
     *
     * @param inputState the current state of the screen.
     * @return true if the button should be enabled, false otherwise.
     */
    override fun shouldEnableSaveButton(inputState: CurrencyConverterInputState): Boolean {
        return inputState.fromAmount.isValid()
    }

    /**
     * Helper function to update the inputState state of the screen.
     *
     * @param update the function to update the content state.
     * @see Content
     */
    override fun updateInput(
        update: (CurrencyConverterInputState) -> CurrencyConverterInputState
    ) {
        inputState.update { currentState ->
            update(currentState)
        }
    }
}