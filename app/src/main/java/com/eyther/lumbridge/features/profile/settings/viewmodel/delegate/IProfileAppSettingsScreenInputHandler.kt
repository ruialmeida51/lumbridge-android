package com.eyther.lumbridge.features.profile.settings.viewmodel.delegate

import com.eyther.lumbridge.features.profile.settings.model.ProfileAppSettingsScreenInputState
import kotlinx.coroutines.flow.StateFlow

interface IProfileAppSettingsScreenInputHandler {
    val inputState: StateFlow<ProfileAppSettingsScreenInputState>

    /**
     * Methods called on different input changes. Each one updates the input state with
     * the new value and performs any necessary validation.
     */
    fun onDarkModeChanged(isDarkMode: Boolean)
    fun onAppLanguageChanged(countryCode: String)
    fun onShowAllocationsOnExpensesChanged(showAllocationsOnExpenses: Boolean)

    /**
     * Helper function to update the input state.
     * @param update the lambda to update the input state.
     * @see ProfileAppSettingsScreenInputState
     */
    fun updateInput(update: (ProfileAppSettingsScreenInputState) -> ProfileAppSettingsScreenInputState)
}
