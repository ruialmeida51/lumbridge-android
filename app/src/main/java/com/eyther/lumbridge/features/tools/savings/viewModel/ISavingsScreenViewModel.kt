package com.eyther.lumbridge.features.tools.savings.viewModel

import com.eyther.lumbridge.features.tools.savings.model.SavingsScreenViewState
import kotlinx.coroutines.flow.StateFlow

interface ISavingsScreenViewModel {
    val viewState: StateFlow<SavingsScreenViewState>
}
