package com.eyther.lumbridge.features.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyther.lumbridge.features.profile.model.ProfileScreenViewState
import com.eyther.lumbridge.usecase.preferences.GetPreferences
import com.eyther.lumbridge.usecase.preferences.SetIsDarkMode
import com.eyther.lumbridge.usecase.user.GetUserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val getPreferences: GetPreferences,
    private val getUserData: GetUserData,
    private val setIsDarkMode: SetIsDarkMode
) : ViewModel(), ProfileScreenViewModelInterface {

    override val viewState = MutableStateFlow<ProfileScreenViewState>(
        ProfileScreenViewState.Loading
    )

    init {
        observeChanges()
    }

    private fun observeChanges() {
        combine(
            getPreferences(),
            getUserData()
        ) { preferences, userData -> preferences to userData }
            .onEach { (preferences, user) ->
                viewState.update {
                    ProfileScreenViewState.Content(
                        username = "John Doe",
                        email = "eytherdeveloper@gmail.com",
                        isDarkModeEnabled = preferences?.isDarkMode
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    override fun toggleDarkMode(isDarkMode: Boolean) {
        viewModelScope.launch {
            setIsDarkMode(isDarkMode)
        }
    }
}
