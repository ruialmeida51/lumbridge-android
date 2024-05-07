package com.eyther.lumbridge.features.tools.netsalary.viewmodel

import com.eyther.lumbridge.features.tools.netsalary.model.NetSalaryScreenViewState
import kotlinx.coroutines.flow.StateFlow

interface INetSalaryScreenViewModel {
    val viewState: StateFlow<NetSalaryScreenViewState>

    /**
     * Calculates the net salary based on the annual salary. Also gives you the
     * calculation of the monthly food card.
     *
     * @param annualSalary the annual salary of the user
     * @param foodCardPerDiem the food card per diem
     * @return the net salary
     */
    fun onCalculateNetSalary(annualSalary: Float, foodCardPerDiem: Float)

    fun onEditSalary()
}
