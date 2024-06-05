package com.eyther.lumbridge.features.tools.savings.viewModel

import androidx.lifecycle.ViewModel
import com.eyther.lumbridge.features.tools.savings.model.SavingsScreenViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SavingsScreenViewModel @Inject constructor() : ViewModel(), ISavingsScreenViewModel {
    override val viewState = MutableStateFlow<SavingsScreenViewState>(
        SavingsScreenViewState.Loading
    )
}
