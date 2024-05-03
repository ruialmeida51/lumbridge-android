package com.eyther.lumbridge.features.editfinancialprofile.viewmodel

import androidx.navigation.NavHostController
import com.eyther.lumbridge.features.editfinancialprofile.model.EditFinancialProfileScreenViewState
import kotlinx.coroutines.flow.StateFlow

interface EditFinancialProfileScreenViewModelInterface {
    val viewState: StateFlow<EditFinancialProfileScreenViewState>

    /**
     * Saves the user data inputted by the user.
     * @param annualGrossSalary the annual gross salary of the user.
     * @param foodCardPerDiem the food card per diem of the user.
     * @param navController the navigation controller.
     */
    fun saveUserData(
        annualGrossSalary: Float,
        foodCardPerDiem: Float,
        navController: NavHostController
    )
}
