package com.eyther.lumbridge.usecase.preferences

import com.eyther.lumbridge.domain.repository.preferences.PreferencesRepository
import javax.inject.Inject

class GetIsDarkMode @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(): Boolean? {
        return preferencesRepository.isDarkMode()
    }
}
