package com.eyther.lumbridge.features.tools.netsalary.viewmodel.input.delegate

import com.eyther.lumbridge.features.tools.netsalary.model.input.NetSalaryInputState
import kotlinx.coroutines.flow.StateFlow

interface INetSalaryInputScreenInputHandler {
    val inputState: StateFlow<NetSalaryInputState>

    /**
     * Methods called on different input changes. Each one updates the input state with
     * the new value and performs any necessary validation.
     */
    fun onSalaryInputTypeChanged(option: Int)
    fun onAnnualGrossSalaryChanged(annualGrossSalary: Float?)
    fun onMonthlyGrossSalaryChanged(monthlyGrossSalary: Float?)
    fun onFoodCardPerDiemChanged(foodCardPerDiem: Float?)
    fun onNumberOfDependantsChanged(numberOfDependants: Int?)
    fun onSingleIncomeChanged(singleIncome: Boolean)
    fun onMarriedChanged(married: Boolean)
    fun onHandicappedChanged(handicapped: Boolean)
    fun onLocaleChanged(countryCode: String)

    /**
     * Checks if we have enough information available to enable the button.
     * @return true if we have enough information to enable the button, false otherwise.
     * @see NetSalaryInputState
     */
    fun shouldEnableSaveButton(inputState: NetSalaryInputState): Boolean

    /**
     * Helper function to update the input state.
     * @param update the lambda to update the input state.
     * @see NetSalaryInputState
     */
    fun updateInput(update: (NetSalaryInputState) -> NetSalaryInputState)
}