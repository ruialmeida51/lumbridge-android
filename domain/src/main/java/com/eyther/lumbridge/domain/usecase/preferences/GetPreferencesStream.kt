package com.eyther.lumbridge.domain.usecase.preferences

import com.eyther.lumbridge.domain.model.preferences.Preferences
import com.eyther.lumbridge.domain.repository.preferences.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPreferencesStream @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) {
    operator fun invoke(): Flow<Preferences?> {
        return preferencesRepository.getPreferencesFlow()
    }
}
