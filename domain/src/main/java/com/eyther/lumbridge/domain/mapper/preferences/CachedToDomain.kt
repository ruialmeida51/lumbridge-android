package com.eyther.lumbridge.domain.mapper.preferences

import com.eyther.lumbridge.data.model.appSettings.AppSettings
import com.eyther.lumbridge.domain.model.preferences.Preferences

fun AppSettings.toDomain(): Preferences {
    return Preferences(
        isDarkMode = isDarkMode
    )
}
