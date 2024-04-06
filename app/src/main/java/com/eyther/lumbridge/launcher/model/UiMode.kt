package com.eyther.lumbridge.launcher.model

sealed interface UiMode {
	data object Light: UiMode
	data object Dark: UiMode
	
	fun isDarkTheme() = this is Dark
}