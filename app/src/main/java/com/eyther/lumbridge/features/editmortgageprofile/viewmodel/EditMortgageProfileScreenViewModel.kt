package com.eyther.lumbridge.features.editmortgageprofile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.eyther.lumbridge.domain.time.monthsUntil
import com.eyther.lumbridge.domain.time.toLocalDate
import com.eyther.lumbridge.features.editmortgageprofile.model.EditMortgageProfileScreenViewEffect
import com.eyther.lumbridge.features.editmortgageprofile.model.EditMortgageProfileScreenViewState
import com.eyther.lumbridge.features.editmortgageprofile.model.EditMortgageProfileScreenViewState.Loading
import com.eyther.lumbridge.features.editmortgageprofile.viewmodel.IEditMortgageProfileScreenViewModel.Companion.MORTGAGE_MAX_DURATION
import com.eyther.lumbridge.features.editmortgageprofile.viewmodel.IEditMortgageProfileScreenViewModel.Companion.PADDING_YEARS
import com.eyther.lumbridge.features.editmortgageprofile.viewmodel.delegate.EditMortgageProfileInputHandler
import com.eyther.lumbridge.features.editmortgageprofile.viewmodel.delegate.IEditMortgageProfileInputHandler
import com.eyther.lumbridge.model.mortgage.MortgageTypeUi
import com.eyther.lumbridge.model.user.UserMortgageUi
import com.eyther.lumbridge.usecase.user.mortgage.GetUserMortgage
import com.eyther.lumbridge.usecase.user.mortgage.SaveUserMortgage
import com.eyther.lumbridge.usecase.user.profile.GetLocaleOrDefault
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class EditMortgageProfileScreenViewModel @Inject constructor(
    private val getLocaleOrDefault: GetLocaleOrDefault,
    private val saveUserMortgage: SaveUserMortgage,
    private val getUserMortgage: GetUserMortgage,
    private val mortgageInputHandler: EditMortgageProfileInputHandler
) : ViewModel(),
    IEditMortgageProfileScreenViewModel,
    IEditMortgageProfileInputHandler by mortgageInputHandler {

    override val viewState = MutableStateFlow<EditMortgageProfileScreenViewState>(Loading)
    override val viewEffects = MutableSharedFlow<EditMortgageProfileScreenViewEffect>()

    init {
        fetchMortgageProfile()
    }

    private fun fetchMortgageProfile() {
        viewModelScope.launch {
            val initialUserMortgage = getUserMortgage()
            val locale = getLocaleOrDefault()

            updateInput { state ->
                state.copy(
                    loanAmount = state.loanAmount.copy(
                        text = initialUserMortgage?.loanAmount?.toString()
                    ),
                    euribor = state.euribor.copy(
                        text = initialUserMortgage?.euribor?.toString()
                    ),
                    spread = state.spread.copy(
                        text = initialUserMortgage?.spread?.toString()
                    ),
                    startDate = state.startDate.copy(
                        date = initialUserMortgage?.startDate
                    ),
                    endDate = state.endDate.copy(
                        date = initialUserMortgage?.endDate
                    ),
                    fixedInterestRate = state.fixedInterestRate.copy(
                        text = initialUserMortgage?.fixedInterestRate?.toString()
                    ),
                    mortgageChoiceState = state.mortgageChoiceState.copy(
                        selectedTab = initialUserMortgage?.mortgageType?.ordinal ?: 0,
                        tabsStringRes = MortgageTypeUi.entries().map { it.label }
                    )
                )
            }

            inputState
                .onEach { inputState ->
                    viewState.update {
                        EditMortgageProfileScreenViewState.Content(
                            locale = locale,
                            shouldEnableSaveButton = shouldEnableSaveButton(inputState),
                            inputState = inputState
                        )
                    }
                }.launchIn(this)
        }
    }

    override fun saveMortgageProfile(navController: NavHostController) {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            viewModelScope.launch {
                viewEffects.emit(
                    EditMortgageProfileScreenViewEffect.ShowError(throwable.message.orEmpty())
                )
            }
        }

        viewModelScope.launch(coroutineExceptionHandler) {
            val inputState = inputState.value
            val mortgageType = MortgageTypeUi.fromOrdinal(
                ordinal = inputState.mortgageChoiceState.selectedTab
            )

            val userMortgage =  UserMortgageUi(
                loanAmount = checkNotNull(inputState.loanAmount.text?.toFloatOrNull()),
                euribor = inputState.euribor.text?.toFloatOrNull(),
                spread = inputState.spread.text?.toFloatOrNull(),
                fixedInterestRate = inputState.fixedInterestRate.text?.toFloatOrNull(),
                startDate = checkNotNull(inputState.startDate.date),
                endDate = checkNotNull(inputState.endDate.date),
                mortgageType = mortgageType
            )

            saveUserMortgage(userMortgage)

            navController.navigateUp()
        }
    }

    override fun getMaxSelectableYear(): Int {
        return LocalDate.now().year + MORTGAGE_MAX_DURATION + PADDING_YEARS
    }

    override fun isSelectableEndDate(endDateInMillis: Long): Boolean {
        val startDate = inputState.value.startDate.date ?: return false
        return startDate.monthsUntil(endDateInMillis.toLocalDate()) >= 1
    }
}
