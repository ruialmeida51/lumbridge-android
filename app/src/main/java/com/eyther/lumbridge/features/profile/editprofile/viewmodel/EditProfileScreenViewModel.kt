package com.eyther.lumbridge.features.profile.editprofile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.features.profile.editprofile.model.EditProfileScreenViewState
import com.eyther.lumbridge.model.user.UserProfileUi
import com.eyther.lumbridge.usecase.user.profile.GetUserProfile
import com.eyther.lumbridge.usecase.user.profile.SaveUserProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileScreenViewModel @Inject constructor(
    private val getUserProfile: GetUserProfile,
    private val saveUserProfile: SaveUserProfile
) : ViewModel(), EditProfileScreenViewModelInterface {

    override val viewState: MutableStateFlow<EditProfileScreenViewState> =
        MutableStateFlow(EditProfileScreenViewState.Loading)

    init {
        fetchUserData()
    }

    private fun fetchUserData() {
        viewModelScope.launch {
            val userProfile = getUserProfile()

            viewState.update {
                if (userProfile == null) {
                    EditProfileScreenViewState.Content.NoData(
                        availableLocales = getAvailableLocales()
                    )
                } else {
                    EditProfileScreenViewState.Content.Data(
                        availableLocales = getAvailableLocales(),
                        currentData = userProfile
                    )

                }
            }
        }
    }

    private fun getAvailableLocales() = SupportedLocales.entries

    override fun saveUserData(
        name: String,
        email: String,
        locale: SupportedLocales,
        navController: NavHostController
    ) {
        viewModelScope.launch {
            saveUserProfile(
                UserProfileUi(name, email, locale)
            )

            navController.popBackStack()
        }
    }
}
