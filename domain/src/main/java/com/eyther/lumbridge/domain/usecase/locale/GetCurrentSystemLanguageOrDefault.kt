package com.eyther.lumbridge.domain.usecase.locale

import com.eyther.lumbridge.domain.model.locale.SupportedLanguages
import com.eyther.lumbridge.domain.repository.locale.LocaleRepository
import com.eyther.lumbridge.domain.usecase.preferences.GetPreferencesStream
import com.eyther.lumbridge.shared.di.model.Schedulers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

class GetCurrentSystemLanguageOrDefault @Inject constructor(
    private val getPreferencesFlow: GetPreferencesStream,
    private val localeRepository: LocaleRepository,
    private val schedulers: Schedulers
) {
    suspend operator fun invoke(): SupportedLanguages {
        val preferencesFlow = getPreferencesFlow().firstOrNull()
        val systemCountryCode = withContext(schedulers.io) {
            localeRepository.getApplicationLocaleCountryCode() ?: Locale.getDefault().country
        }

        return preferencesFlow?.appLanguage
            ?: SupportedLanguages.getOrNull(countryCode = systemCountryCode)
            ?: SupportedLanguages.ENGLISH
    }
}
