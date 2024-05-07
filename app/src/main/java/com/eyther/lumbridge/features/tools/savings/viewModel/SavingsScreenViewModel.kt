package com.eyther.lumbridge.features.tools.savings.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyther.lumbridge.features.tools.savings.model.SavingsScreenViewState
import com.eyther.lumbridge.usecase.user.profile.GetUserProfile
import com.eyther.lumbridge.usecase.user.profile.SaveUserProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavingsScreenViewModel @Inject constructor(
    private val getUserProfile: GetUserProfile,
    private val saveUserData: SaveUserProfile
) : ViewModel(), ISavingsScreenViewModel {
    override val viewState = MutableStateFlow<SavingsScreenViewState>(
        SavingsScreenViewState.Loading
    )

    init {
        fetchUserData()
    }

    // TODO
    private fun fetchUserData() {
        viewModelScope.launch {
            val userData = getUserProfile()

            if (userData == null) {
                // Refer to Net Salary Calculator for the implementation of this block.
                return@launch
            }

            // if (userData.savingsPercentages == null) {
                // Show input
            // } else {
                // Show result
            // }
        }
    }
}
