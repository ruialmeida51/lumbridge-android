package com.eyther.lumbridge.features.profile.viewmodel

import com.eyther.lumbridge.features.profile.model.ProfileScreenViewState
import kotlinx.coroutines.flow.StateFlow

interface ProfileScreenViewModelInterface {
    val viewState: StateFlow<ProfileScreenViewState>

    /**
     * Toggle the dark mode setting. This also saves the dark mode setting to
     * the user's preferences.
     *
     * @param isDarkMode true if the dark mode setting is enabled, false otherwise.
     */
    fun toggleDarkMode(isDarkMode: Boolean)
}
