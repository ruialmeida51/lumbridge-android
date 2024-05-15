package com.eyther.lumbridge.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.eyther.lumbridge.R

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val bodyFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont("Poppins"),
        fontProvider = provider,
    )
)

val displayFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont("Lato"),
        fontProvider = provider,
    )
)

// Default Material 3 typography values
val baseline = Typography()

val runescapeTypography = Typography(
    displayLarge = baseline.displayLarge.copy(
        fontFamily = displayFontFamily
    ),
    displayMedium = baseline.displayMedium.copy(
        fontFamily = displayFontFamily
    ),
    displaySmall = baseline.displaySmall.copy(
        fontFamily = displayFontFamily
    ),
    headlineLarge = baseline.headlineLarge.copy(
        fontFamily = displayFontFamily
    ),
    headlineMedium = baseline.headlineMedium.copy(
        fontFamily = displayFontFamily
    ),
    headlineSmall = baseline.headlineSmall.copy(
        fontFamily = displayFontFamily
    ),
    titleLarge = baseline.titleLarge.copy(
        fontFamily = displayFontFamily,
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
    ),
    titleMedium = baseline.titleMedium.copy(
        fontFamily = displayFontFamily,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
    ),
    titleSmall = baseline.titleSmall.copy(
        fontFamily = displayFontFamily,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
    ),
    bodyLarge = baseline.bodyLarge.copy(
        fontFamily = bodyFontFamily,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
    ),
    bodyMedium = baseline.bodyMedium.copy(
        fontFamily = bodyFontFamily,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
    ),
    bodySmall = baseline.bodySmall.copy(
        fontFamily = bodyFontFamily,
        fontSize = 13.sp,
        fontWeight = FontWeight.Light,
    ),
    labelLarge = baseline.labelLarge.copy(
        fontFamily = bodyFontFamily,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold
    ),
    labelMedium = baseline.labelMedium.copy(
        fontFamily = bodyFontFamily,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold
    ),
    labelSmall = baseline.labelSmall.copy(
        fontFamily = bodyFontFamily,
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal
    ),
)
