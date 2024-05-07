package com.eyther.lumbridge.launcher.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyther.lumbridge.launcher.model.MainScreenViewState
import com.eyther.lumbridge.launcher.model.UiMode
import com.eyther.lumbridge.usecase.preferences.GetPreferences
import com.eyther.lumbridge.usecase.preferences.SetIsDarkMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val getPreferences: GetPreferences,
    private val setIsDarkMode: SetIsDarkMode
) : ViewModel(), IMainActivityViewModel {

    override val viewState = MutableStateFlow(MainScreenViewState())

    init {
        observePreferences()
    }

    private fun observePreferences() {
        getPreferences()
            .filterNotNull()
            .onEach { preferences ->
                viewState.update {
                    it.copy(
                        uiMode = if (preferences.isDarkMode) UiMode.Dark else UiMode.Light
                    )
                }
            }.launchIn(viewModelScope)
    }

    override suspend fun hasStoredPreferences(): Boolean {
        return getPreferences().firstOrNull() != null
    }

    override fun toggleDarkMode(isDarkMode: Boolean) {
        viewModelScope.launch {
            viewState.update {
                it.copy(uiMode = if (isDarkMode) UiMode.Dark else UiMode.Light)
            }
            setIsDarkMode(isDarkMode)
        }
    }
}
