package com.eyther.lumbridge.platform.locale

import androidx.appcompat.app.AppCompatDelegate
import com.eyther.lumbridge.domain.repository.locale.LocaleRepository
import javax.inject.Inject

class LocaleRepositoryImpl @Inject constructor() : LocaleRepository {
    override fun getApplicationLocaleCountryCode(): String? {
        return AppCompatDelegate.getApplicationLocales().get(0)?.country
    }
}
