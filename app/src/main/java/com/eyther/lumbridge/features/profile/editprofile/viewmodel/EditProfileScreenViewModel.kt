package com.eyther.lumbridge.features.profile.editprofile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.features.profile.editprofile.model.EditProfileScreenViewEffects
import com.eyther.lumbridge.features.profile.editprofile.model.EditProfileScreenViewState
import com.eyther.lumbridge.features.profile.editprofile.model.EditProfileScreenViewState.Loading
import com.eyther.lumbridge.features.profile.editprofile.viewmodel.delegate.EditProfileScreenInputHandler
import com.eyther.lumbridge.features.profile.editprofile.viewmodel.delegate.IEditProfileScreenInputHandler
import com.eyther.lumbridge.model.user.UserProfileUi
import com.eyther.lumbridge.usecase.user.profile.GetUserProfile
import com.eyther.lumbridge.usecase.user.profile.SaveUserProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileScreenViewModel @Inject constructor(
    private val getUserProfile: GetUserProfile,
    private val saveUserProfile: SaveUserProfile,
    private val profileInputHandler: EditProfileScreenInputHandler
) : ViewModel(), IEditProfileScreenViewModel,
    IEditProfileScreenInputHandler by profileInputHandler {

    override val viewState = MutableStateFlow<EditProfileScreenViewState>(Loading)
    override val viewEffects = MutableSharedFlow<EditProfileScreenViewEffects>()

    init {
        observeUserProfile()
    }

    private fun observeUserProfile() {
        viewModelScope.launch {
            val userProfile = getUserProfile()

            updateInput {
                it.copy(
                    name = it.name.copy(text = userProfile?.name),
                    email = it.email.copy(text = userProfile?.email)
                )
            }

            inputState
                .onEach { inputState ->
                    viewState.update {
                        EditProfileScreenViewState.Content(
                            availableLocales = getAvailableLocales(),
                            inputState = inputState,
                            shouldEnableSaveButton = shouldEnableSaveButton(inputState)
                        )
                    }
                }
                .launchIn(this)
        }
    }

    private fun getAvailableLocales() = SupportedLocales.entries

    override fun saveUserData(navController: NavHostController) {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            viewModelScope.launch {
                viewEffects.emit(
                    EditProfileScreenViewEffects.ShowError(throwable.message.orEmpty())
                )
            }
        }

        viewModelScope.launch(coroutineExceptionHandler) {
            val input = inputState.value

            saveUserProfile(
                UserProfileUi(
                    name = checkNotNull(input.name.text),
                    email = checkNotNull(input.email.text),
                    locale = input.locale
                )
            )

            navController.popBackStack()
        }
    }
}
