package com.eyther.lumbridge.features.editfinancialprofile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.eyther.lumbridge.features.editfinancialprofile.model.EditFinancialProfileScreenViewEffect
import com.eyther.lumbridge.features.editfinancialprofile.model.EditFinancialProfileScreenViewState
import com.eyther.lumbridge.features.editfinancialprofile.model.EditFinancialProfileScreenViewState.Content
import com.eyther.lumbridge.features.editfinancialprofile.model.EditFinancialProfileScreenViewState.Loading
import com.eyther.lumbridge.model.user.UserFinancialsUi
import com.eyther.lumbridge.usecase.user.financials.GetUserFinancialsStream
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
    private val getUserFinancials: GetUserFinancialsStream,
    private val saveUserFinancials: SaveUserFinancials
) : ViewModel(), EditFinancialProfileScreenViewModelInterface {

    override val viewState: MutableStateFlow<EditFinancialProfileScreenViewState> =
        MutableStateFlow(Loading)

    override val viewEffect = MutableSharedFlow<EditFinancialProfileScreenViewEffect>()

    init {
        fetchUserFinancials()
    }

    private fun fetchUserFinancials() {
        viewModelScope.launch {
            val userFinancialsFlow = getUserFinancials()
            val locale = getLocaleOrDefault()

            userFinancialsFlow
                .onEach { userFinancials ->
                    viewState.update {
                        Content(
                            locale = locale,
                            currentData = userFinancials,
                            shouldEnableSaveButton = shouldEnableButton(userFinancials)
                        )
                    }
                }
                .launchIn(this)
        }
    }


    override fun saveUserData(navController: NavHostController) {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            viewModelScope.launch {
                viewEffect.emit(
                    EditFinancialProfileScreenViewEffect.ShowError(
                        message = throwable.message ?: "An error occurred."
                    )
                )
            }
        }

        viewModelScope.launch(coroutineExceptionHandler) {
            val userFinancials = checkNotNull((viewState.value.asContent())?.currentData) {
                "💥 User financial information cannot be null."
            }

            saveUserFinancials(userFinancials)

            navController.navigateUp()
        }
    }

    override fun onAnnualGrossSalaryChanged(annualGrossSalary: Float?) {
        updateInput { state ->
            state.copy(
                currentData = state.currentData?.copy(annualGrossSalary = annualGrossSalary)
                    ?: UserFinancialsUi(annualGrossSalary = annualGrossSalary)
            )
        }
    }

    override fun onFoodCardPerDiemChanged(foodCardPerDiem: Float?) {
        updateInput { state ->
            state.copy(
                currentData = state.currentData?.copy(foodCardPerDiem = foodCardPerDiem)
                    ?: UserFinancialsUi(foodCardPerDiem = foodCardPerDiem)
            )
        }
    }

    override fun onSavingsPercentageChanged(savingsPercentage: Int?) {
        updateInput { state ->
            state.copy(
                currentData = state.currentData?.copy(savingsPercentage = savingsPercentage)
                    ?: UserFinancialsUi(savingsPercentage = savingsPercentage)
            )
        }
    }

    override fun onNecessitiesPercentageChanged(necessitiesPercentage: Int?) {
        updateInput { state ->
            state.copy(
                currentData = state.currentData?.copy(necessitiesPercentage = necessitiesPercentage)
                    ?: UserFinancialsUi(necessitiesPercentage = necessitiesPercentage)
            )
        }
    }

    override fun onLuxuriesPercentageChanged(luxuriesPercentage: Int?) {
        updateInput { state ->
            state.copy(
                currentData = state.currentData?.copy(luxuriesPercentage = luxuriesPercentage)
                    ?: UserFinancialsUi(luxuriesPercentage = luxuriesPercentage)
            )
        }
    }

    override fun onNumberOfDependantsChanged(numberOfDependants: Int?) {
        updateInput { state ->
            state.copy(
                currentData = state.currentData?.copy(numberOfDependants = numberOfDependants)
                    ?: UserFinancialsUi(numberOfDependants = numberOfDependants)
            )
        }
    }

    override fun onIrsWithPartnerChanged(irsWithPartner: Boolean) {
        updateInput { state ->
            state.copy(
                currentData = state.currentData?.copy(irsWithPartner = irsWithPartner)
                    ?: UserFinancialsUi(irsWithPartner = irsWithPartner)
            )
        }
    }

    override fun onMarriedChanged(married: Boolean) {
        updateInput { state ->
            state.copy(
                currentData = state.currentData?.copy(married = married)
                    ?: UserFinancialsUi(married = married)
            )
        }
    }

    override fun onHandicappedChanged(handicapped: Boolean) {
        updateInput { state ->
            state.copy(
                currentData = state.currentData?.copy(handicapped = handicapped)
                    ?: UserFinancialsUi(handicapped = handicapped)
            )
        }
    }

    private fun updateInput(
        update: (Content) -> Content
    ) {
        viewState.update {
            val currentState = checkNotNull(it.asContent())
            val newState = update(currentState)

            newState.copy(
                shouldEnableSaveButton = shouldEnableButton(newState.currentData)
            )
        }
    }


    private fun shouldEnableButton(
        userFinancialsUi: UserFinancialsUi?
    ): Boolean {
        with(userFinancialsUi) {
            val percentagesValid = listOfNotNull(
                this?.savingsPercentage,
                this?.necessitiesPercentage,
                this?.luxuriesPercentage
            ).sum() <= 100

            return this?.annualGrossSalary != null &&
                    this.foodCardPerDiem != null &&
                    percentagesValid
        }
    }
}
