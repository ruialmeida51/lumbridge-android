package com.eyther.lumbridge.features.profile.editprofile.viewmodel.delegate

import com.eyther.lumbridge.R
import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.extensions.kotlin.getErrorOrNull
import com.eyther.lumbridge.features.editfinancialprofile.model.EditFinancialProfileScreenViewState.Content
import com.eyther.lumbridge.features.profile.editprofile.model.EditProfileScreenInputState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class EditProfileScreenInputHandler @Inject constructor() : IEditProfileScreenInputHandler {

    override val inputState = MutableStateFlow(EditProfileScreenInputState())

    override fun onEmailChanged(email: String?) {
        updateInput { state ->
            state.copy(
                email = state.email.copy(
                    text = email,
                    error = email.getErrorOrNull(R.string.edit_profile_invalid_email)
                )
            )
        }
    }

    override fun onNameChanged(name: String?) {
        updateInput { state ->
            state.copy(
                name = state.name.copy(
                    text = name,
                    error = name.getErrorOrNull(R.string.edit_profile_invalid_name)
                )
            )
        }
    }

    override fun onLocaleChanged(countryCode: String) {
        updateInput { state ->
            state.copy(locale = SupportedLocales.get(countryCode))
        }
    }

    /**
     * Checks if the save button should be enabled.
     *
     * The button should be enabled if the user has entered valid data.
     *
     * @param inputState the current state of the screen.
     * @return true if the button should be enabled, false otherwise.
     */
    override fun shouldEnableSaveButton(inputState: EditProfileScreenInputState): Boolean {
        return inputState.email.isValid() && inputState.name.isValid()
    }

    /**
     * Helper function to update the inputState state of the screen.
     *
     * @param update the function to update the content state.
     * @see Content
     */
    override fun updateInput(
        update: (EditProfileScreenInputState) -> EditProfileScreenInputState
    ) {
        inputState.update { currentState ->
            update(currentState)
        }
    }
}