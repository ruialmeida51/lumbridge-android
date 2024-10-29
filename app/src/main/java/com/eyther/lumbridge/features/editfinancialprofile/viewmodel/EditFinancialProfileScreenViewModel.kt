package com.eyther.lumbridge.features.editfinancialprofile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.eyther.lumbridge.features.editfinancialprofile.model.EditFinancialProfileScreenViewEffect
import com.eyther.lumbridge.features.editfinancialprofile.model.EditFinancialProfileScreenViewState
import com.eyther.lumbridge.features.editfinancialprofile.model.EditFinancialProfileScreenViewState.Content
import com.eyther.lumbridge.features.editfinancialprofile.model.EditFinancialProfileScreenViewState.Loading
import com.eyther.lumbridge.features.editfinancialprofile.viewmodel.delegate.EditFinancialProfileInputHandler
import com.eyther.lumbridge.features.editfinancialprofile.viewmodel.delegate.IEditFinancialProfileInputHandler
import com.eyther.lumbridge.model.finance.SalaryInputTypeUi
import com.eyther.lumbridge.model.user.UserFinancialsUi
import com.eyther.lumbridge.usecase.finance.GetAnnualSalaryUseCase
import com.eyther.lumbridge.usecase.finance.GetMonthlySalaryUseCase
import com.eyther.lumbridge.usecase.user.financials.GetUserFinancials
import com.eyther.lumbridge.usecase.user.financials.SaveUserFinancials
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
class EditFinancialProfileScreenViewModel @Inject constructor(
    private val getLocaleOrDefault: GetLocaleOrDefault,
    private val getUserFinancials: GetUserFinancials,
    private val saveUserFinancials: SaveUserFinancials,
    private val editFinancialProfileInputHandler: EditFinancialProfileInputHandler,
    private val getAnnualSalaryUseCase: GetAnnualSalaryUseCase,
    private val getMonthlySalaryUseCase: GetMonthlySalaryUseCase
) : ViewModel(),
    IEditFinancialProfileScreenViewModel,
    IEditFinancialProfileInputHandler by editFinancialProfileInputHandler {

    companion object {
        private const val PERCENTAGE_SUFFIX = "%"
    }

    override val viewState = MutableStateFlow<EditFinancialProfileScreenViewState>(Loading)
    override val viewEffects = MutableSharedFlow<EditFinancialProfileScreenViewEffect>()

    init {
        observeUserFinancials()
    }

    private fun observeUserFinancials() {
        viewModelScope.launch {
            val initialUserFinancials = getUserFinancials()
            val locale = getLocaleOrDefault()
            val annualGrossSalary = initialUserFinancials?.annualGrossSalary
            val monthlyGrossSalary = annualGrossSalary?.let { getMonthlySalaryUseCase(it) }
            val currencySymbol = locale.getCurrencySymbol()

            updateInput { state ->
                state.copy(
                    annualGrossSalary = state.annualGrossSalary.copy(
                        text = annualGrossSalary?.toString(),
                        suffix = currencySymbol
                    ),
                    monthlyGrossSalary = state.monthlyGrossSalary.copy(
                        text = monthlyGrossSalary?.toString(),
                        suffix = currencySymbol
                    ),
                    foodCardPerDiem = state.foodCardPerDiem.copy(
                        text = initialUserFinancials?.foodCardPerDiem?.toString(),
                        suffix = currencySymbol
                    ),
                    savingsPercentage = state.savingsPercentage.copy(
                        text = initialUserFinancials?.savingsPercentage?.toString(),
                        suffix = PERCENTAGE_SUFFIX
                    ),
                    necessitiesPercentage = state.necessitiesPercentage.copy(
                        text = initialUserFinancials?.necessitiesPercentage?.toString(),
                        suffix = PERCENTAGE_SUFFIX
                    ),
                    luxuriesPercentage = state.luxuriesPercentage.copy(
                        text = initialUserFinancials?.luxuriesPercentage?.toString(),
                        suffix = PERCENTAGE_SUFFIX
                    ),
                    numberOfDependants = state.numberOfDependants.copy(
                        text = initialUserFinancials?.numberOfDependants?.toString()
                    ),
                    salaryInputChoiceState = state.salaryInputChoiceState.copy(
                        selectedTab = initialUserFinancials?.salaryInputTypeUi?.ordinal ?: 0,
                        tabsStringRes = SalaryInputTypeUi.entries().map { it.label }
                    ),
                    singleIncome = initialUserFinancials?.singleIncome ?: false,
                    married = initialUserFinancials?.married ?: false,
                    handicapped = initialUserFinancials?.handicapped ?: false
                )
            }

            inputState
                .onEach { inputState ->
                    viewState.update {
                        Content(
                            locale = locale,
                            shouldEnableSaveButton = shouldEnableSaveButton(inputState),
                            inputState = inputState
                        )
                    }
                }.launchIn(this)
        }
    }

    override fun saveUserData(navController: NavHostController) {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            viewModelScope.launch {
                viewEffects.emit(
                    EditFinancialProfileScreenViewEffect.ShowError(throwable.message.orEmpty())
                )
            }
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
                savingsPercentage = inputState.savingsPercentage.text?.toIntOrNull(),
                necessitiesPercentage = inputState.necessitiesPercentage.text?.toIntOrNull(),
                luxuriesPercentage = inputState.luxuriesPercentage.text?.toIntOrNull(),
                numberOfDependants = inputState.numberOfDependants.text?.toIntOrNull(),
                singleIncome = inputState.singleIncome,
                married = inputState.married,
                handicapped = inputState.handicapped,
                salaryInputTypeUi = salaryType
            )

            saveUserFinancials(userFinancials)

            viewEffects.emit(EditFinancialProfileScreenViewEffect.CloseScreen)
        }
    }
}
