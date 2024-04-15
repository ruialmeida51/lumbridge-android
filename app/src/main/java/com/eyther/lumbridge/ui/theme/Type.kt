package com.eyther.lumbridge.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.eyther.lumbridge.R

// Define your custom font family
val runescapeFontFamily = FontFamily(Font(R.font.goudy_bookletter_1911))

// Define your typography
val runescapeTypography = Typography(
	titleLarge = TextStyle(
		fontFamily = runescapeFontFamily,
		fontSize = 28.sp,
		fontWeight = FontWeight.Bold,
		letterSpacing = 1.sp
	),
	titleMedium = TextStyle(
		fontFamily = runescapeFontFamily,
		fontSize = 24.sp,
		fontWeight = FontWeight.Bold,
		letterSpacing = 0.5.sp
	),
	titleSmall = TextStyle(
		fontFamily = runescapeFontFamily,
		fontSize = 20.sp,
		fontWeight = FontWeight.Bold,
		letterSpacing = 0.sp
	),
	bodyLarge = TextStyle(
		fontFamily = runescapeFontFamily,
		fontSize = 16.sp,
		fontWeight = FontWeight.Normal,
		letterSpacing = 0.sp
	),
	bodyMedium = TextStyle(
		fontFamily = runescapeFontFamily,
		fontSize = 14.sp,
		fontWeight = FontWeight.Normal,
		letterSpacing = 0.sp
	),
	labelSmall = TextStyle(
		fontFamily = runescapeFontFamily,
		fontSize = 12.sp,
		fontWeight = FontWeight.Normal,
		letterSpacing = 0.sp
	)
)
