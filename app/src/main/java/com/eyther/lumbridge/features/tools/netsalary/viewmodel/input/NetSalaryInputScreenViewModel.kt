package com.eyther.lumbridge.features.tools.netsalary.viewmodel.input

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.features.tools.netsalary.model.input.NetSalaryInputScreenViewEffects
import com.eyther.lumbridge.features.tools.netsalary.model.input.NetSalaryInputScreenViewState
import com.eyther.lumbridge.features.tools.netsalary.viewmodel.input.delegate.INetSalaryInputScreenInputHandler
import com.eyther.lumbridge.features.tools.netsalary.viewmodel.input.delegate.NetSalaryInputScreenInputHandler
import com.eyther.lumbridge.model.finance.NetSalaryUi
import com.eyther.lumbridge.model.finance.SalaryInputTypeUi
import com.eyther.lumbridge.model.user.UserFinancialsUi
import com.eyther.lumbridge.usecase.finance.GetAnnualSalaryUseCase
import com.eyther.lumbridge.usecase.finance.GetMonthlySalaryUseCase
import com.eyther.lumbridge.usecase.finance.GetNetSalaryUseCase
import com.eyther.lumbridge.usecase.user.financials.GetUserFinancials
import com.eyther.lumbridge.usecase.user.profile.GetLocaleOrDefault
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NetSalaryInputScreenViewModel @Inject constructor(
    private val getUserFinancials: GetUserFinancials,
    private val getLocaleOrDefault: GetLocaleOrDefault,
    private val netSalaryScreenInputHandler: NetSalaryInputScreenInputHandler,
    private val getNetSalaryUseCase: GetNetSalaryUseCase,
    private val getAnnualSalaryUseCase: GetAnnualSalaryUseCase,
    private val getMonthlySalaryUseCase: GetMonthlySalaryUseCase
) : ViewModel(),
    INetSalaryInputScreenViewModel,
    INetSalaryInputScreenInputHandler by netSalaryScreenInputHandler {

    private var cachedUserFinancials: UserFinancialsUi? = null

    override val viewState: MutableStateFlow<NetSalaryInputScreenViewState> =
        MutableStateFlow(NetSalaryInputScreenViewState.Loading)

    override val viewEffects: MutableSharedFlow<NetSalaryInputScreenViewEffects> =
        MutableSharedFlow()

    init {
        fetchInitialUserData()
    }

    private fun fetchInitialUserData() {
        viewModelScope.launch {
            resetInput()

            inputState
                .onEach { inputState ->
                    viewState.update {
                        NetSalaryInputScreenViewState.Content(
                            locale = inputState.locale,
                            availableLocales = SupportedLocales.entries,
                            inputState = inputState,
                            shouldEnableSaveButton = shouldEnableSaveButton(inputState)
                        )
                    }
                }.launchIn(this)
        }
    }

    private suspend fun resetInput() {
        val inputState = inputState.value
        val initialLocale = getLocaleOrDefault()
        val initialUserFinancials = cachedUserFinancials ?: getUserFinancials()
        val initialAnnualGrossSalary = initialUserFinancials?.annualGrossSalary
        val initialMonthlyGrossSalary = initialAnnualGrossSalary?.let { getMonthlySalaryUseCase(it) }
        val currencySymbol = initialLocale.getCurrencySymbol()

        updateInput { state ->
            state.copy(
                annualGrossSalary = state.annualGrossSalary.copy(
                    text = initialAnnualGrossSalary?.toString(),
                    suffix = currencySymbol
                ),
                monthlyGrossSalary = state.monthlyGrossSalary.copy(
                    text = initialMonthlyGrossSalary?.toString(),
                    suffix = currencySymbol
                ),
                foodCardPerDiem = state.foodCardPerDiem.copy(
                    text = initialUserFinancials?.foodCardPerDiem?.toString(),
                    suffix = currencySymbol
                ),
                numberOfDependants = state.numberOfDependants.copy(
                    text = initialUserFinancials?.numberOfDependants?.toString()
                ),
                salaryInputChoiceState = state.salaryInputChoiceState.copy(
                    selectedTab = initialUserFinancials?.salaryInputTypeUi?.ordinal ?: 0,
                    tabsStringRes = SalaryInputTypeUi.entries().map { it.label }
                ),
                singleIncome = initialUserFinancials?.singleIncome == true,
                married = initialUserFinancials?.married == true,
                handicapped = initialUserFinancials?.handicapped == true
            )
        }

        viewState.update {
            NetSalaryInputScreenViewState.Content(
                locale = inputState.locale,
                availableLocales = SupportedLocales.entries,
                inputState = inputState,
                shouldEnableSaveButton = shouldEnableSaveButton(inputState)
            )
        }
    }

    override fun onCalculateNetSalary(
        navController: NavHostController,
        cacheArguments: (
            netSalaryUi: NetSalaryUi,
            locale: SupportedLocales
        ) -> Unit
    ) {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e(this::class.simpleName, "💥 Error calculating net salary", throwable)
            viewState.update { NetSalaryInputScreenViewState.Error }
        }

        viewModelScope.launch(coroutineExceptionHandler) {
            val inputState = inputState.value
            val salaryType = SalaryInputTypeUi.fromOrdinal(
                ordinal = inputState.salaryInputChoiceState.selectedTab
            )

            val annualGrossSalary = if (salaryType == SalaryInputTypeUi.Annually) {
                inputState.annualGrossSalary.text?.toFloatOrNull()
            } else {
                getAnnualSalaryUseCase(inputState.monthlyGrossSalary.text?.toFloatOrNull())
            }

            val userFinancials = UserFinancialsUi(
                annualGrossSalary = annualGrossSalary,
                foodCardPerDiem = inputState.foodCardPerDiem.text?.toFloatOrNull(),
                numberOfDependants = inputState.numberOfDependants.text?.toIntOrNull(),
                singleIncome = inputState.singleIncome,
                married = inputState.married,
                handicapped = inputState.handicapped,
                salaryInputTypeUi = salaryType
            )

            cachedUserFinancials = userFinancials

            val netSalary = getNetSalaryUseCase(userFinancials)

            // Cache arguments for the result screen
            cacheArguments(netSalary, inputState.locale)

            // Navigate to the result screen
            viewEffects.emit(NetSalaryInputScreenViewEffects.NavigateToNetSalaryResults)
        }
    }

    override fun onRetryInput() {
        viewModelScope.launch {
            resetInput()
        }
    }
}
