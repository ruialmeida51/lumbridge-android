package com.eyther.lumbridge.domain.model.user

import android.graphics.Bitmap
import com.eyther.lumbridge.domain.model.locale.SupportedLocales

data class UserProfileDomain(
    val name: String,
    val email: String,
    val imageBitmap: Bitmap?,
    val locale: SupportedLocales
)
