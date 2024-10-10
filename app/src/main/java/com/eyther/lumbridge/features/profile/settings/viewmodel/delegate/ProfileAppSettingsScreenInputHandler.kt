package com.eyther.lumbridge.features.profile.settings.viewmodel.delegate

import com.eyther.lumbridge.domain.model.locale.SupportedLanguages
import com.eyther.lumbridge.features.editfinancialprofile.model.EditFinancialProfileScreenViewState.Content
import com.eyther.lumbridge.features.profile.settings.model.ProfileAppSettingsScreenInputState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class ProfileAppSettingsScreenInputHandler @Inject constructor(): IProfileAppSettingsScreenInputHandler {

    override val inputState = MutableStateFlow(ProfileAppSettingsScreenInputState())

    override fun onDarkModeChanged(isDarkMode: Boolean) {
        updateInput { state ->
            state.copy(isDarkMode = isDarkMode)
        }
    }

    override fun onAppLanguageChanged(countryCode: String) {
        updateInput { state ->
            state.copy(appLanguage = SupportedLanguages.get(countryCode))
        }
    }

    /**
     * Helper function to update the inputState state of the screen.
     *
     * @param update the function to update the content state.
     * @see Content
     */
    override fun updateInput(
        update: (ProfileAppSettingsScreenInputState) -> ProfileAppSettingsScreenInputState
    ) {
        inputState.update { currentState ->
            update(currentState)
        }
    }
}
