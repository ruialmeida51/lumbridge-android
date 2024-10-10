package com.eyther.lumbridge.extensions.platform

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

/**
 * Creates an app locale based on the country code and changes the app language.
 */
fun changeAppLanguage(countryCode: String) {
    val appLocale = LocaleListCompat.forLanguageTags(countryCode)
    AppCompatDelegate.setApplicationLocales(appLocale)
}
