package com.eyther.osrsoutpost.features.main.viewmodel

import androidx.lifecycle.ViewModel
import com.eyther.lumbridge.launcher.model.UiMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(): ViewModel(), MainActivityViewModelInterface {
	override val uiMode = MutableStateFlow<UiMode>(UiMode.Light)
	
	fun setLightMode() = uiMode.update { UiMode.Light }
	fun setDarkMode() = uiMode.update { UiMode.Dark }
}
