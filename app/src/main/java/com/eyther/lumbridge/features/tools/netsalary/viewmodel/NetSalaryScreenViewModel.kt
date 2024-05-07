package com.eyther.lumbridge.features.tools.netsalary.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.features.tools.netsalary.model.NetSalaryScreenViewState
import com.eyther.lumbridge.model.user.UserFinancialsUi
import com.eyther.lumbridge.usecase.finance.GetNetSalary
import com.eyther.lumbridge.usecase.user.financials.GetUserFinancials
import com.eyther.lumbridge.usecase.user.financials.SaveUserFinancials
import com.eyther.lumbridge.usecase.user.profile.GetLocaleOrDefault
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NetSalaryScreenViewModel @Inject constructor(
    private val getUserFinancials: GetUserFinancials,
    private val getLocaleOrDefault: GetLocaleOrDefault,
    private val saveUserFinancials: SaveUserFinancials,
    private val getNetSalary: GetNetSalary
) : ViewModel(), INetSalaryScreenViewModel {

    override val viewState = MutableStateFlow<NetSalaryScreenViewState>(
        NetSalaryScreenViewState.Loading
    )

    init {
        fetchUserData()
    }

    private fun fetchUserData() {
        viewModelScope.launch {
            val userData = getUserFinancials()
            val locale = getLocaleOrDefault()

            if (userData == null) {
                viewState.update {
                    NetSalaryScreenViewState.Content.Input(
                        annualGrossSalary = null,
                        foodCardPerDiem = null,
                        locale = locale
                    )
                }

                return@launch
            }

            calculateNetSalary(userData, locale)
        }
    }

    private fun calculateNetSalary(userData: UserFinancialsUi, locale: SupportedLocales) {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e(this::class.java.name, "Error calculating net salary 💥", throwable)
        }

        viewModelScope.launch(coroutineExceptionHandler) {

        }
    }

    override fun onCalculateNetSalary(annualSalary: Float, foodCardPerDiem: Float) {
        viewModelScope.launch {

        }
    }

    override fun onEditSalary() {
        viewModelScope.launch {
            val userData = getUserFinancials()

            viewState.update {
                NetSalaryScreenViewState.Content.Input(
                    annualGrossSalary = userData?.annualGrossSalary,
                    foodCardPerDiem = userData?.foodCardPerDiem,
                    locale = getLocaleOrDefault()
                )
            }
        }
    }
}
