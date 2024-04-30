package com.eyther.lumbridge.launcher.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyther.lumbridge.domain.repository.preferences.PreferencesRepository
import com.eyther.lumbridge.launcher.model.UiMode
import com.eyther.lumbridge.usecase.preferences.GetIsDarkMode
import com.eyther.lumbridge.usecase.preferences.SetIsDarkMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val getIsDarkMode: GetIsDarkMode,
    private val setIsDarkMode: SetIsDarkMode
) : ViewModel(), MainActivityViewModelInterface {
    override val uiMode = MutableStateFlow<UiMode>(UiMode.Light)

    suspend fun checkUserUiMode(): UiMode? {
        val isDarkMode = getIsDarkMode() ?: return null

        return when {
            isDarkMode -> UiMode.Dark
            else -> UiMode.Light
        }
    }

    fun setLightMode() {
        viewModelScope.launch {
            uiMode.update { UiMode.Light }
            setIsDarkMode(isDarkMode = false)
        }
    }

    fun setDarkMode() {
        viewModelScope.launch {
            uiMode.update { UiMode.Dark }
            setIsDarkMode(isDarkMode = true)
        }
    }
}
