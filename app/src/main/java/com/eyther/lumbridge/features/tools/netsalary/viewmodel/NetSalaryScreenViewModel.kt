package com.eyther.lumbridge.features.tools.netsalary.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.usecase.user.GetUserData
import com.eyther.lumbridge.features.tools.netsalary.model.NetSalaryScreenViewState
import com.eyther.lumbridge.model.user.UserUi
import com.eyther.lumbridge.usecase.finance.GetNetSalary
import com.eyther.lumbridge.usecase.user.GetLocaleOrDefault
import com.eyther.lumbridge.usecase.user.SaveUserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class NetSalaryScreenViewModel @Inject constructor(
    private val getUserData: GetUserData,
    private val saveUserData: SaveUserData,
    private val getNetSalary: GetNetSalary,
    private val getLocaleOrDefault: GetLocaleOrDefault
) : ViewModel(), NetSalaryScreenViewModelInterface {

    override val viewState = MutableStateFlow<NetSalaryScreenViewState>(
        NetSalaryScreenViewState.Loading
    )

    init {
        fetchUserData()
    }

    private fun fetchUserData() {
        viewModelScope.launch {
            val userData = getUserData()

            if (userData == null) {
                viewState.update {
                    NetSalaryScreenViewState.Content.Input(
                        annualGrossSalary = null,
                        foodCardPerDiem = null,
                        locale = getLocaleOrDefault()
                    )
                }

                return@launch
            }

            calculateNetSalary(userData)
        }
    }

    private fun calculateNetSalary(userData: UserUi) {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e(this::class.java.name, "Error calculating net salary 💥", throwable)
        }

        viewModelScope.launch(exceptionHandler) {

            val netSalary = getNetSalary(userData.annualGrossSalary, userData.foodCardPerDiem)

            viewState.update {
                NetSalaryScreenViewState.Content.Overview(
                    annualGrossSalary = userData.annualGrossSalary,
                    netSalary = netSalary,
                    locale = userData.locale
                )
            }
        }
    }

    fun onCalculateNetSalary(annualSalary: Float, foodCardPerDiem: Float) {
        viewModelScope.launch {
            val userData = UserUi(
                annualGrossSalary = annualSalary,
                foodCardPerDiem = foodCardPerDiem,
                locale = SupportedLocales.PORTUGAL
            )

            saveUserData(userData).also { calculateNetSalary(userData) }
        }
    }

    fun onEditSalary() {
        viewModelScope.launch {
            val userData = getUserData()

            viewState.update {
                NetSalaryScreenViewState.Content.Input(
                    annualGrossSalary = userData?.annualGrossSalary,
                    foodCardPerDiem = userData?.foodCardPerDiem,
                    locale = userData?.locale ?: getLocaleOrDefault()
                )
            }
        }
    }
}
