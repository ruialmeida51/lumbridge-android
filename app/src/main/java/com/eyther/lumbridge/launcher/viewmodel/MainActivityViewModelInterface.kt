package com.eyther.lumbridge.launcher.viewmodel

import com.eyther.lumbridge.launcher.model.UiMode
import kotlinx.coroutines.flow.StateFlow

interface MainActivityViewModelInterface {
	val uiMode: StateFlow<UiMode>
}