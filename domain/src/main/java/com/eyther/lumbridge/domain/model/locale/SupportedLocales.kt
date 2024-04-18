package com.eyther.lumbridge.domain.model.locale

// ISO 3166 ALPHA 2
enum class InternalLocale(val countryCode: String) {
    PORTUGAL("pt");

    companion object {
        fun get(countryCode: String) = InternalLocale.entries.first { it.countryCode == countryCode }
    }
}
