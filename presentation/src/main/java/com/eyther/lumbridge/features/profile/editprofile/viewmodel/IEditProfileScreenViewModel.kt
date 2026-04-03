package com.eyther.lumbridge.features.profile.editprofile.viewmodel

import androidx.navigation.NavHostController
import com.eyther.lumbridge.features.profile.editprofile.model.EditProfileScreenViewEffects
import com.eyther.lumbridge.features.profile.editprofile.model.EditProfileScreenViewState
import com.eyther.lumbridge.features.profile.editprofile.viewmodel.delegate.IEditProfileScreenInputHandler
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface IEditProfileScreenViewModel : IEditProfileScreenInputHandler {
    val viewState: StateFlow<EditProfileScreenViewState>
    val viewEffects: SharedFlow<EditProfileScreenViewEffects>

    /**
     * Saves the user data inputted by the user.
     * @param navController the navigation controller.
     */
    fun saveUserData(navController: NavHostController)
}
