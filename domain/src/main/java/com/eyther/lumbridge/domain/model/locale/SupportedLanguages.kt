package com.eyther.lumbridge.domain.model.locale

import java.util.Locale

// ISO 3166 ALPHA 2 for country code
enum class SupportedLanguages(
    val countryCode: String
) {
    PORTUGUESE("pt"),
    ENGLISH("en");

    companion object {
        fun get(countryCode: String) =
            SupportedLanguages.entries.first { it.countryCode.equals(countryCode, ignoreCase = true) }

        fun getOrNull(countryCode: String?) =
            SupportedLanguages.entries.firstOrNull { it.countryCode.equals(countryCode, ignoreCase = true) }
    }
}
