package com.eyther.lumbridge.usecase.preferences

import com.eyther.lumbridge.domain.repository.preferences.PreferencesRepository
import javax.inject.Inject

class SetIsDarkMode @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(isDarkMode: Boolean) {
        preferencesRepository.setDarkMode(isDarkMode)
    }
}
