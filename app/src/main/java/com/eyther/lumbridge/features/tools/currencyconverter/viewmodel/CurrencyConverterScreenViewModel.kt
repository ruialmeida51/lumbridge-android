package com.eyther.lumbridge.features.tools.currencyconverter.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyther.lumbridge.domain.model.currencyexchange.SupportedCurrencies
import com.eyther.lumbridge.features.tools.currencyconverter.model.CurrencyConverterScreenViewState
import com.eyther.lumbridge.features.tools.currencyconverter.model.CurrencyConverterScreenViewState.Loading
import com.eyther.lumbridge.features.tools.currencyconverter.viewmodel.delegate.CurrencyConverterInputHandler
import com.eyther.lumbridge.features.tools.currencyconverter.viewmodel.delegate.ICurrencyConverterInputHandler
import com.eyther.lumbridge.usecase.currencyexchange.GetCurrencyExchangeRate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyConverterScreenViewModel @Inject constructor(
    private val currencyConverterInputHandler: CurrencyConverterInputHandler,
    private val getCurrencyExchangeRate: GetCurrencyExchangeRate
) : ViewModel(),
    ICurrencyConverterScreenViewModel,
    ICurrencyConverterInputHandler by currencyConverterInputHandler {

    companion object {
        private const val SHORT_REQUEST_DELAY = 300L
    }

    override val viewState = MutableStateFlow<CurrencyConverterScreenViewState>(Loading)

    init {
        observeCurrencyConverterInputState()
    }

    private fun observeCurrencyConverterInputState() {
        viewState.update {
            CurrencyConverterScreenViewState.Content(
                shouldEnableCalculateButton = false,
                availableCurrencies = SupportedCurrencies.entries
            )
        }

        inputState
            .onEach { inputState ->
                viewState.update { state ->
                    (state as CurrencyConverterScreenViewState.Content).copy(
                        inputState = inputState,
                        shouldEnableCalculateButton = shouldEnableSaveButton(inputState),
                        exchangeRate = null,
                        toExchangedAmount = null,
                        hasError = false,
                        isCalculating = false
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    /**
     * Converts the currency from one to another.
     *
     * @return the converted currency
     */
    fun onConvert() {
        val initialTime = System.currentTimeMillis()

        viewState.update {
            (it as CurrencyConverterScreenViewState.Content).copy(
                isCalculating = true,
                hasError = false
            )
        }

        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            viewState.update {
                Log.e(this::class.simpleName, throwable.message ?: "💥 Unknown error.", throwable)

                (it as CurrencyConverterScreenViewState.Content).copy(
                    isCalculating = false,
                    hasError = true
                )
            }
        }

        viewModelScope.launch(coroutineExceptionHandler) {
            val contentState = viewState.value as CurrencyConverterScreenViewState.Content

            val amount = contentState.inputState.fromAmount.text?.toFloat()

            val exchangeRate = getCurrencyExchangeRate.getExchangeRate(
                fromCurrency = contentState.inputState.fromCurrency.currency,
                targetCurrency = contentState.inputState.toCurrency.currency
            )

            // The time we finished the requests.
            val finishedRequestTime = System.currentTimeMillis()

            // Visually, we want to show the loading state for at least a short period of time.
            // as this makes the UI feel more responsive.
            if (finishedRequestTime - initialTime < SHORT_REQUEST_DELAY) {
                delay(SHORT_REQUEST_DELAY)
            }

            viewState.update { state ->
                (state as CurrencyConverterScreenViewState.Content).copy(
                    exchangeRate = exchangeRate,
                    toExchangedAmount = amount?.times(exchangeRate),
                    isCalculating = false,
                    hasError = false
                )
            }
        }
    }
}