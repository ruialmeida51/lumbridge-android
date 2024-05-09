package com.eyther.lumbridge.features.profile.editprofile.viewmodel.delegate

import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.features.editfinancialprofile.model.EditFinancialProfileInputState
import com.eyther.lumbridge.features.profile.editprofile.model.EditProfileScreenInputState
import kotlinx.coroutines.flow.StateFlow

interface IEditProfileScreenInputHandler {
    val inputState: StateFlow<EditProfileScreenInputState>

    /**
     * Methods called on different input changes. Each one updates the input state with
     * the new value and performs any necessary validation.
     */
    fun onNameChanged(name: String?)
    fun onEmailChanged(email: String?)
    fun onLocaleChanged(locale: SupportedLocales)

    /**
     * Checks if we have enough information available to enable the button.
     * @return true if we have enough information to enable the button, false otherwise.
     * @see EditProfileScreenInputState
     */
    fun shouldEnableSaveButton(inputState: EditProfileScreenInputState): Boolean

    /**
     * Helper function to update the input state.
     * @param update the lambda to update the input state.
     * @see EditFinancialProfileInputState
     */
    fun updateInput(update: (EditProfileScreenInputState) -> EditProfileScreenInputState)
}
