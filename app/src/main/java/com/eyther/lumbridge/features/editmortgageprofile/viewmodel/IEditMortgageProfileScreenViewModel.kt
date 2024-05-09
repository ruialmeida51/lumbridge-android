package com.eyther.lumbridge.features.editmortgageprofile.viewmodel

import androidx.navigation.NavHostController
import com.eyther.lumbridge.features.editmortgageprofile.model.EditMortgageProfileScreenViewEffect
import com.eyther.lumbridge.features.editmortgageprofile.model.EditMortgageProfileScreenViewState
import com.eyther.lumbridge.features.editmortgageprofile.viewmodel.delegate.IEditMortgageProfileInputHandler
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface IEditMortgageProfileScreenViewModel : IEditMortgageProfileInputHandler {
    val viewState: StateFlow<EditMortgageProfileScreenViewState>
    val viewEffect: SharedFlow<EditMortgageProfileScreenViewEffect>

    /**
     * Saves the user data inputted by the user.
     */
    fun saveMortgageProfile(navController: NavHostController)
}
