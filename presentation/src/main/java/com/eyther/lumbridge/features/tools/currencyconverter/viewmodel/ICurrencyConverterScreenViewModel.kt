package com.eyther.lumbridge.features.tools.currencyconverter.viewmodel

import com.eyther.lumbridge.features.tools.currencyconverter.model.CurrencyConverterScreenViewState
import kotlinx.coroutines.flow.StateFlow

interface ICurrencyConverterScreenViewModel {
    val viewState: StateFlow<CurrencyConverterScreenViewState>
}
