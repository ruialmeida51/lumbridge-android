package com.eyther.lumbridge.features.editfinancialprofile.viewmodel

import androidx.navigation.NavHostController
import com.eyther.lumbridge.features.editfinancialprofile.model.EditFinancialProfileScreenViewEffect
import com.eyther.lumbridge.features.editfinancialprofile.model.EditFinancialProfileScreenViewState
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface EditFinancialProfileScreenViewModelInterface : EditFinancialProfileInputHandler {
    val viewState: StateFlow<EditFinancialProfileScreenViewState>
    val viewEffect: SharedFlow<EditFinancialProfileScreenViewEffect>

    /**
     * Saves the user data inputted by the user.
     */
    fun saveUserData(navController: NavHostController)
}
