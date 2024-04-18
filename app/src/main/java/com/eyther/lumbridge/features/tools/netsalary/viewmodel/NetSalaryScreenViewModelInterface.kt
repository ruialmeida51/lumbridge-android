package com.eyther.lumbridge.features.tools.netsalary.viewmodel

import com.eyther.lumbridge.features.tools.netsalary.model.NetSalaryScreenViewState
import kotlinx.coroutines.flow.StateFlow

interface NetSalaryScreenViewModelInterface {
    val viewState: StateFlow<NetSalaryScreenViewState>
}
