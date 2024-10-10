package com.eyther.lumbridge.usecase.locale

import com.eyther.lumbridge.domain.model.locale.SupportedLanguages
import javax.inject.Inject

class GetSupportedLanguages @Inject constructor() {

    operator fun invoke(): List<SupportedLanguages> {
        return SupportedLanguages.entries
    }
}
