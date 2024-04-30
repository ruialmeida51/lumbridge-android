package com.eyther.lumbridge.features.tools.savings.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyther.lumbridge.features.tools.savings.model.SavingsScreenViewState
import com.eyther.lumbridge.usecase.user.GetUserData
import com.eyther.lumbridge.usecase.user.SaveUserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavingsScreenViewModel @Inject constructor(
    private val getUserData: GetUserData,
    private val saveUserData: SaveUserData
) : ViewModel(), SavingsScreenViewModelInterface {
    override val viewState = MutableStateFlow<SavingsScreenViewState>(
        SavingsScreenViewState.Loading
    )

    init {
        fetchUserData()
    }

    // TODO
    private fun fetchUserData() {
        viewModelScope.launch {
            val userData = getUserData()

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
