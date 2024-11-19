package com.eyther.lumbridge.usecase.preferences

import com.eyther.lumbridge.domain.model.locale.SupportedLanguages
import com.eyther.lumbridge.domain.repository.preferences.PreferencesRepository
import javax.inject.Inject

class SavePreferences @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(
        isDarkMode: Boolean,
        appLanguages: SupportedLanguages,
        showAllocationsOnExpenses: Boolean
    ) {
        preferencesRepository.updatePreferences(
            isDarkMode = isDarkMode,
            appLanguage = appLanguages,
            showAllocationsOnExpenses = showAllocationsOnExpenses
        )
    }
}
