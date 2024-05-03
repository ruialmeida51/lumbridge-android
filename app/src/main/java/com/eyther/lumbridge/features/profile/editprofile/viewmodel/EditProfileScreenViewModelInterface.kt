package com.eyther.lumbridge.features.profile.editprofile.viewmodel

import androidx.navigation.NavHostController
import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.features.profile.editprofile.model.EditProfileScreenViewState
import kotlinx.coroutines.flow.StateFlow

interface EditProfileScreenViewModelInterface {
    val viewState: StateFlow<EditProfileScreenViewState>

    /**
     * Saves the user data inputted by the user.
     * @param name the name of the user.
     * @param email the email of the user.
     * @param locale the locale of the user.
     * @param navController the navigation controller.
     */
    fun saveUserData(
        name: String,
        email: String,
        locale: SupportedLocales,
        navController: NavHostController
    )
}
