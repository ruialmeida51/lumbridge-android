package com.eyther.lumbridge.domain.repository.locale

interface LocaleRepository {
    fun getApplicationLocaleCountryCode(): String?
}
