package com.eyther.lumbridge.features.tools.netsalary.viewmodel.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.features.tools.netsalary.model.result.NetSalaryResultScreenViewState
import com.eyther.lumbridge.model.finance.NetSalaryUi
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = INetSalaryResultScreenViewModel.Factory::class)
class NetSalaryResultScreenViewModel @AssistedInject constructor(
    @Assisted private val netSalaryUi: NetSalaryUi,
    @Assisted private val locale: SupportedLocales
) : ViewModel(), INetSalaryResultScreenViewModel {

    override val viewState = MutableStateFlow<NetSalaryResultScreenViewState>(
        NetSalaryResultScreenViewState.Loading
    )

    init {
        fetchInitialUserData()
    }

    private fun fetchInitialUserData() {
        viewModelScope.launch {
            viewState.update {
                NetSalaryResultScreenViewState.Content(
                    netSalary = netSalaryUi,
                    locale = locale
                )
            }
        }
    }
}
