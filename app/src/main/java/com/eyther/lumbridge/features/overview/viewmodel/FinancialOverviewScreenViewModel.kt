package com.eyther.lumbridge.features.overview.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyther.lumbridge.features.overview.model.FinancialOverviewScreenViewState
import com.eyther.lumbridge.features.overview.model.FinancialOverviewTabItem
import com.eyther.lumbridge.usecase.finance.AddMonthlyPaymentUseCase
import com.eyther.lumbridge.usecase.finance.GetMortgageCalculationUseCase
import com.eyther.lumbridge.usecase.finance.GetNetSalaryUseCase
import com.eyther.lumbridge.usecase.user.financials.GetUserFinancialsStream
import com.eyther.lumbridge.usecase.user.mortgage.GetUserMortgageStream
import com.eyther.lumbridge.usecase.user.profile.GetLocaleOrDefault
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FinancialOverviewScreenViewModel @Inject constructor(
    private val getLocaleOrDefault: GetLocaleOrDefault,
    private val getUserFinancialsStream: GetUserFinancialsStream,
    private val getUserMortgageStream: GetUserMortgageStream,
    private val getNetSalaryUseCase: GetNetSalaryUseCase,
    private val getMortgageCalculationUseCase: GetMortgageCalculationUseCase,
    private val addMonthlyPaymentUseCase: AddMonthlyPaymentUseCase
) : ViewModel(),
    IFinancialOverviewScreenViewModel {

    override val viewState: MutableStateFlow<FinancialOverviewScreenViewState> =
        MutableStateFlow(FinancialOverviewScreenViewState.Loading)

    init {
        observeUserFinancials()
    }

    private fun observeUserFinancials() {
        viewModelScope.launch {
            val locale = getLocaleOrDefault()

            combine(
                getUserFinancialsStream(),
                getUserMortgageStream()
            ) { userFinancials, userMortgage ->
                userFinancials to userMortgage
            }.onEach { (userFinancials, userMortgage) ->
                viewState.update { state ->
                    FinancialOverviewScreenViewState.Content(
                        locale = locale,
                        selectedTabItem = if (state.isContent()) {
                            state.asContent().selectedTabItem
                        } else {
                            FinancialOverviewTabItem.PersonalOverview
                        },
                        netSalary = userFinancials?.let {
                            getNetSalaryUseCase(userFinancials)
                        },
                        mortgage = userMortgage?.let {
                            getMortgageCalculationUseCase(userMortgage)
                        },
                        enablePaymentButton = userMortgage != null
                    )
                }
            }.launchIn(this)
        }
    }

    override fun onTabSelected(tabItem: FinancialOverviewTabItem) {
        viewState.update {
            it.asContent().copy(selectedTabItem = tabItem)
        }
    }

    override fun onPayment() {
        viewModelScope.launch {
            viewState.value.asContent().mortgage?.let {
                addMonthlyPaymentUseCase(it)
            }
        }
    }
}
