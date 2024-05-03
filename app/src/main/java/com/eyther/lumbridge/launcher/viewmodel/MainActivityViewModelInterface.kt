package com.eyther.lumbridge.launcher.viewmodel

import com.eyther.lumbridge.launcher.model.MainScreenViewState
import kotlinx.coroutines.flow.StateFlow

interface MainActivityViewModelInterface {
    /**
     * Check if the user has stored preferences. If the user has stored preferences,
     * the app will use the stored preferences to set the theme settings. If the user
     * has not stored preferences, the app will use the default theme settings.
     *
     * @return true if the user has stored theme settings, false otherwise.
     */
    suspend fun hasStoredPreferences(): Boolean

    /**
     * Toggle the dark mode setting. This also saves the dark mode setting to
     * the user's preferences.
     *
     * @param isDarkMode true if the dark mode setting is enabled, false otherwise.
     */
    fun toggleDarkMode(isDarkMode: Boolean)

    val viewState: StateFlow<MainScreenViewState>
}
