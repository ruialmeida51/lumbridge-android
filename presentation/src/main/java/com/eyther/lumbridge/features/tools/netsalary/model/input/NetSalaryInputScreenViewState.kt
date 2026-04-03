package com.eyther.lumbridge.features.tools.netsalary.model.input

import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.model.finance.DuodecimosTypeUi

sealed interface NetSalaryInputScreenViewState {
    data object Loading : NetSalaryInputScreenViewState
    data object Error : NetSalaryInputScreenViewState

    data class Content(
        val locale: SupportedLocales,
        val availableLocales: List<SupportedLocales>,
        val availableDuodecimos: List<DuodecimosTypeUi>,
        val inputState: NetSalaryInputState,
        val shouldEnableSaveButton: Boolean = false
    ) : NetSalaryInputScreenViewState
}
