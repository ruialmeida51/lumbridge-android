package com.eyther.lumbridge.features.overview.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.eyther.lumbridge.features.overview.model.FinancialOverviewScreenViewState
import com.eyther.lumbridge.ui.navigation.NavigationItem
import com.eyther.lumbridge.usecase.finance.GetNetSalary
import com.eyther.lumbridge.usecase.user.financials.GetUserFinancialsStream
import com.eyther.lumbridge.usecase.user.profile.GetLocaleOrDefault
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FinancialOverviewScreenViewModel @Inject constructor(
    private val getLocaleOrDefault: GetLocaleOrDefault,
    private val getUserFinancialsStream: GetUserFinancialsStream,
    private val getNetSalary: GetNetSalary
) : ViewModel(),
    FinancialOverviewScreenViewModelInterface {

    override val viewState: MutableStateFlow<FinancialOverviewScreenViewState> =
        MutableStateFlow(FinancialOverviewScreenViewState.Loading)

    init {
        observeUserFinancials()
    }

    private fun observeUserFinancials() {
        getUserFinancialsStream()
            .onEach { userFinancials ->
                viewState.update {
                    if (userFinancials == null) {
                        FinancialOverviewScreenViewState.Content.Input(getLocaleOrDefault())
                    } else {
                        val netSalary = getNetSalary(
                            annualGrossSalary = userFinancials.annualGrossSalary,
                            foodCardPerDiem = userFinancials.foodCardPerDiem
                        )

                        FinancialOverviewScreenViewState.Content.Overview(
                            locale = getLocaleOrDefault(),
                            annualGrossSalary = userFinancials.annualGrossSalary,
                            netSalary = netSalary
                        )
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    override fun navigate(navItem: NavigationItem, navController: NavHostController) {
        navController.navigate(navItem.route)
    }
}
