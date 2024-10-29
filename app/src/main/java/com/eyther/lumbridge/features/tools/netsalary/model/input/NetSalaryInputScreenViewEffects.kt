package com.eyther.lumbridge.features.tools.netsalary.model.input

sealed interface NetSalaryInputScreenViewEffects {
    data object NavigateToNetSalaryResults : NetSalaryInputScreenViewEffects
}
