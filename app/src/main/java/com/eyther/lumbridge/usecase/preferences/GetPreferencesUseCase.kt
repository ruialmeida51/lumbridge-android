package com.eyther.lumbridge.usecase.preferences

import com.eyther.lumbridge.domain.model.preferences.Preferences
import com.eyther.lumbridge.domain.repository.preferences.PreferencesRepository
import javax.inject.Inject

class GetPreferencesUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(): Preferences? {
        return preferencesRepository.getPreferences()
    }
}
