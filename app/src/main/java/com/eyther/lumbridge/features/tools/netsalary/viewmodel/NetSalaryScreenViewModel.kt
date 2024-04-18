package com.eyther.lumbridge.features.tools.netsalary.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyther.lumbridge.domain.model.finance.NetSalaryProvider
import com.eyther.lumbridge.domain.model.locale.InternalLocale
import com.eyther.lumbridge.usecase.user.GetUserData
import com.eyther.lumbridge.features.tools.netsalary.model.NetSalaryScreenViewState
import com.eyther.lumbridge.model.user.UserUi
import com.eyther.lumbridge.usecase.user.SaveUserData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class NetSalaryScreenViewModel @Inject constructor(
    private val getUserData: GetUserData,
    private val saveUserData: SaveUserData,
    private val netSalaryProvider: NetSalaryProvider
) : ViewModel(), NetSalaryScreenViewModelInterface {

    override val viewState = MutableStateFlow<NetSalaryScreenViewState>(getInitialState())

    init {
        fetchUserData()
    }

    private fun fetchUserData() {
        viewModelScope.launch {
            val userData = getUserData()

            if (userData == null) {
                viewState.update { NetSalaryScreenViewState.Input() }
                return@launch
            }

            calculateNetSalary(userData)
        }
    }

    private fun calculateNetSalary(userData: UserUi) {
        val netSalary = netSalaryProvider.calculate(userData.grossSalary, userData.locale)
        viewState.update { NetSalaryScreenViewState.HasData(netSalary) }
    }

    private fun getInitialState() = NetSalaryScreenViewState.Initial

    fun onCalculateNetSalary(annualSalary: Float) {
        viewModelScope.launch {
            val userData = UserUi(
                grossSalary = annualSalary,
                locale = InternalLocale.PORTUGAL
            )

            saveUserData(userData).also { calculateNetSalary(userData) }
        }
    }

    fun onEditSalary() {
        viewModelScope.launch {
            val userData = getUserData()

            viewState.update {
                NetSalaryScreenViewState.Input(
                    annualGrossSalary = userData?.grossSalary
                )
            }
        }
    }
}
