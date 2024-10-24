package com.eyther.lumbridge.features.editloan.viewmodel

import com.eyther.lumbridge.features.editloan.model.EditLoanScreenViewEffect
import com.eyther.lumbridge.features.editloan.model.EditLoanScreenViewState
import com.eyther.lumbridge.features.editloan.viewmodel.delegate.IEditLoanScreenInputHandler
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface IEditLoanScreenScreenViewModel : IEditLoanScreenInputHandler {
    companion object {
        const val MORTGAGE_MAX_DURATION = 40
        const val PADDING_YEARS = 5
    }

    val viewState: StateFlow<EditLoanScreenViewState>
    val viewEffects: SharedFlow<EditLoanScreenViewEffect>

    /**
     * Saves the user data inputted by the user.
     */
    fun saveLoan()

    /**
     * Calculates the minimum selectable year for the mortgage profile. This is done by
     * getting the current year and summing the maximum duration of a mortgage and
     * some padding years for odd cases that we might not have considered.
     *
     * @return The maximum selectable year for the mortgage profile.
     */
    fun getMaxSelectableYear(): Int

    /**
     * Checks if the end date is selectable based on the start date. There needs to be at least
     * one month difference between the start and end date.
     *
     * @param endDateInMillis The end date in milliseconds.
     * @return True if the end date is selectable, false otherwise.
     */
    fun isSelectableEndDate(endDateInMillis: Long): Boolean
}
