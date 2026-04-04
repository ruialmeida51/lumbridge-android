package com.eyther.lumbridge.features.tools.currencyconverter.viewmodel.delegate

import com.eyther.lumbridge.features.tools.currencyconverter.model.CurrencyConverterInputState
import kotlinx.coroutines.flow.StateFlow

interface ICurrencyConverterInputHandler {
    val inputState: StateFlow<CurrencyConverterInputState>

    /**
     * Methods called on different input changes. Each one updates the input state with
     * the new value and performs any necessary validation.
     */
    fun onFromCurrencyChanged(fromCurrencyCode: String)
    fun onToCurrencyChanged(toCurrencyCode: String)
    fun onFromAmountChanged(fromAmount: Float?)

    /**
     * Checks if we have enough information available to enable the button.
     * @return true if we have enough information to enable the button, false otherwise.
     * @see CurrencyConverterInputState
     */
    fun shouldEnableSaveButton(inputState: CurrencyConverterInputState): Boolean

    /**
     * Helper function to update the input state.
     * @param update the lambda to update the input state.
     * @see CurrencyConverterInputState
     */
    fun updateInput(update: (CurrencyConverterInputState) -> CurrencyConverterInputState)
}
