package com.eyther.lumbridge.features.editfinancialprofile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.eyther.lumbridge.features.editfinancialprofile.model.EditFinancialProfileScreenViewState
import com.eyther.lumbridge.model.user.UserFinancialsUi
import com.eyther.lumbridge.usecase.user.financials.GetUserFinancials
import com.eyther.lumbridge.usecase.user.financials.SaveUserFinancials
import com.eyther.lumbridge.usecase.user.profile.GetLocaleOrDefault
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditFinancialProfileScreenViewModel @Inject constructor(
    private val getLocaleOrDefault: GetLocaleOrDefault,
    private val getUserFinancials: GetUserFinancials,
    private val saveUserFinancials: SaveUserFinancials
) : ViewModel(), EditFinancialProfileScreenViewModelInterface {

    override val viewState: MutableStateFlow<EditFinancialProfileScreenViewState> =
        MutableStateFlow(EditFinancialProfileScreenViewState.Loading)

    init {
        fetchUserFinancials()
    }

    private fun fetchUserFinancials() {
        viewModelScope.launch {
            val userFinancials = getUserFinancials()
            val locale = getLocaleOrDefault()

            viewState.update {
                if (userFinancials == null) {
                    EditFinancialProfileScreenViewState.Content.NoData(locale)
                } else {
                    EditFinancialProfileScreenViewState.Content.Data(locale, userFinancials)
                }
            }
        }
    }

    override fun saveUserData(
        annualGrossSalary: Float,
        foodCardPerDiem: Float,
        navController: NavHostController
    ) {
        viewModelScope.launch {
            saveUserFinancials(UserFinancialsUi(annualGrossSalary, foodCardPerDiem))
            navController.navigateUp()
        }
    }
}
