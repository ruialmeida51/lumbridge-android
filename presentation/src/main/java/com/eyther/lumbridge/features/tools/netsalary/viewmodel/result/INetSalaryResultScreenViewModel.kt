package com.eyther.lumbridge.features.tools.netsalary.viewmodel.result

import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.features.tools.netsalary.model.result.NetSalaryResultScreenViewState
import com.eyther.lumbridge.model.finance.NetSalaryUi
import dagger.assisted.AssistedFactory
import kotlinx.coroutines.flow.StateFlow

interface INetSalaryResultScreenViewModel {
    val viewState: StateFlow<NetSalaryResultScreenViewState>

    @AssistedFactory
    interface Factory {
        fun create(
            netSalaryUi: NetSalaryUi,
            locale: SupportedLocales
        ): NetSalaryResultScreenViewModel
    }
}
