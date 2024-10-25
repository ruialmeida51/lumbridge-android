package com.eyther.lumbridge.features.overview.financialprofiledetails.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyther.lumbridge.features.overview.financialprofiledetails.model.DetailsFinancialProfileScreenViewState
import com.eyther.lumbridge.usecase.finance.GetNetSalaryUseCase
import com.eyther.lumbridge.usecase.user.financials.GetUserFinancialsFlow
import com.eyther.lumbridge.usecase.user.profile.GetLocaleOrDefault
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsFinancialProfileScreenViewModel @Inject constructor(
    getUserFinancialsFlow: GetUserFinancialsFlow,
    getNetSalaryUseCase: GetNetSalaryUseCase,
    getLocaleOrDefault: GetLocaleOrDefault
) : ViewModel(),
    IDetailsFinancialProfileScreenViewModel {

    override val viewState: MutableStateFlow<DetailsFinancialProfileScreenViewState> =
        MutableStateFlow(DetailsFinancialProfileScreenViewState.Loading)

    init {
        viewModelScope.launch {
            val userFinancialsFlow = getUserFinancialsFlow()

            userFinancialsFlow
                .onEach { userFinancialsUi ->
                    if (userFinancialsUi == null) {
                        viewState.update { DetailsFinancialProfileScreenViewState.Empty }
                        return@onEach
                    }

                    viewState.update {
                        DetailsFinancialProfileScreenViewState.Content(
                            salaryDetails = getNetSalaryUseCase(userFinancialsUi),
                            locale = getLocaleOrDefault()
                        )
                    }
                }
                .launchIn(this)
        }
    }
}
