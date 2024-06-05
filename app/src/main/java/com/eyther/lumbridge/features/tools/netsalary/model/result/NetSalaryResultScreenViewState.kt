package com.eyther.lumbridge.features.tools.netsalary.model.result

import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.model.finance.NetSalaryUi

sealed interface NetSalaryResultScreenViewState {
    data object Loading : NetSalaryResultScreenViewState
    data object Error : NetSalaryResultScreenViewState

    data class Content(
        val netSalary: NetSalaryUi,
        val locale: SupportedLocales
    ) : NetSalaryResultScreenViewState
}
