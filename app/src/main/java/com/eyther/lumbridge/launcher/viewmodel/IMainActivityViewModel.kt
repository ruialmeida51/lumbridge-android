package com.eyther.lumbridge.launcher.viewmodel

import com.eyther.lumbridge.launcher.model.MainScreenViewState
import kotlinx.coroutines.flow.StateFlow

interface IMainActivityViewModel {
    val viewState: StateFlow<MainScreenViewState>

    /**
     * Check if the user has stored preferences. If the user has stored preferences,
     * the app will use the stored preferences to set the theme settings. If the user
     * has not stored preferences, the app will use the default theme settings.
     *
     * @return true if the user has stored theme settings, false otherwise.
     */
    suspend fun hasStoredPreferences(): Boolean

    /**
     * This assumes that the user has stored preferences. It will update the user's settings
     * with the new settings provided.
     *
     * If, for some reason, the user has not stored preferences, the app will create try to create a default
     * with these settings as a best effort.
     *
     * @param isDarkMode true if the dark mode setting is enabled, false otherwise. If it is null, the setting will not be updated and instead
     * read from the stored preferences.
     * @param appLanguageCountryCode the country code for the language setting. If it is null, the setting will not be updated and instead
     * read from the system defaults or stored preferences.
     * @param showAllocationsOnExpenses true if the allocations should be shown on the expenses screen, false otherwise. If it is null,
     * the setting will not be updated and instead read from the stored preferences.
     * @param addFoodCardToNecessitiesAllocation true if the food card should be added to the necessities allocation, false otherwise. If it is null,
     * the setting will not be updated and instead read from the stored preferences.
     */
    suspend fun updateSettings(
        isDarkMode: Boolean? = null,
        appLanguageCountryCode: String? = null,
        showAllocationsOnExpenses: Boolean? = null,
        addFoodCardToNecessitiesAllocation: Boolean? = null
    )
}
