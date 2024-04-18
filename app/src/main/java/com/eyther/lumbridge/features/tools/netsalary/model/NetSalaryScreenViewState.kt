package com.eyther.lumbridge.features.tools.netsalary.model

sealed interface NetSalaryScreenViewState {
    data object Initial: NetSalaryScreenViewState

    // No data yet, we need to request user for input OR we want to edit current data.
    data class Input(
        val annualGrossSalary: Float? = null
    ) : NetSalaryScreenViewState

    // We have user data to calculate the net salary.
    data class HasData(
        val netSalary: Float
    ) : NetSalaryScreenViewState
}
