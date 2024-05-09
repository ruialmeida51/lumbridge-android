package com.eyther.lumbridge.features.profile.editprofile.viewmodel.delegate

import androidx.annotation.StringRes
import com.eyther.lumbridge.R
import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.features.editfinancialprofile.model.EditFinancialProfileScreenViewState.Content
import com.eyther.lumbridge.features.profile.editprofile.model.EditProfileScreenInputState
import com.eyther.lumbridge.ui.common.model.text.TextResource
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

    override fun onLocaleChanged(locale: SupportedLocales) {
        updateInput { state ->
            state.copy(locale = locale)
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
        return inputState.email.error == null && inputState.name.error == null
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

    /**
     * Helper function to get the error message of the text input.
     * This is just a boilerplate code to avoid code duplication.
     * @param errorRes the error message resource id.
     * @return the updated state with the error message of the text input.
     */
    private fun String?.getErrorOrNull(@StringRes errorRes: Int) = if (isNullOrEmpty()) {
        TextResource.Resource(resId = errorRes)
    } else {
        null
    }
}