package com.eyther.lumbridge.features.overview.financialprofiledetails.viewmodel

import com.eyther.lumbridge.features.overview.financialprofiledetails.model.DetailsFinancialProfileScreenViewState
import kotlinx.coroutines.flow.StateFlow

interface IDetailsFinancialProfileScreenViewModel {
    val viewState: StateFlow<DetailsFinancialProfileScreenViewState>
}
