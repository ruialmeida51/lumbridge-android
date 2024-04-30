package com.eyther.lumbridge.features.tools.savings.viewModel

import com.eyther.lumbridge.features.tools.savings.model.SavingsScreenViewState
import kotlinx.coroutines.flow.StateFlow

interface SavingsScreenViewModelInterface {
    val viewState: StateFlow<SavingsScreenViewState>
}
