package com.eyther.lumbridge.usecase.locale

import androidx.appcompat.app.AppCompatDelegate
import com.eyther.lumbridge.domain.model.locale.SupportedLanguages
import com.eyther.lumbridge.shared.di.model.Schedulers
import com.eyther.lumbridge.usecase.preferences.GetPreferencesFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

class GetCurrentSystemLanguageOrDefault @Inject constructor(
    private val getPreferencesFlow: GetPreferencesFlow,
    private val schedulers: Schedulers
) {
    suspend operator fun invoke(): SupportedLanguages {
        val preferencesFlow = getPreferencesFlow().firstOrNull()
        val systemCountryCode = withContext(schedulers.io) {
            AppCompatDelegate.getApplicationLocales().get(0)?.country ?: Locale.getDefault().country
        }

        return preferencesFlow?.appLanguage
            ?: SupportedLanguages.getOrNull(countryCode = systemCountryCode)
            ?: SupportedLanguages.ENGLISH
    }
}
