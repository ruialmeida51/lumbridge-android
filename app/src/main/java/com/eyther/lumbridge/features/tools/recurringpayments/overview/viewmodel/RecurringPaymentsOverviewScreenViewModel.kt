package com.eyther.lumbridge.features.tools.recurringpayments.overview.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyther.lumbridge.features.tools.recurringpayments.overview.model.RecurringPaymentsOverviewScreenViewEffects
import com.eyther.lumbridge.features.tools.recurringpayments.overview.model.RecurringPaymentsOverviewScreenViewState
import com.eyther.lumbridge.usecase.recurringpayments.DeleteRecurringPaymentUseCase
import com.eyther.lumbridge.usecase.recurringpayments.GetRecurringPaymentsFlowUseCase
import com.eyther.lumbridge.usecase.user.profile.GetLocaleOrDefaultStream
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecurringPaymentsOverviewScreenViewModel @Inject constructor(
    private val getRecurringPaymentsFlowUseCase: GetRecurringPaymentsFlowUseCase,
    private val getLocaleOrDefaultStream: GetLocaleOrDefaultStream,
    private val deleteRecurringPaymentUseCase: DeleteRecurringPaymentUseCase
) : ViewModel(),
    IRecurringPaymentsOverviewScreenViewModel {

    companion object {
        private const val TAG = "RecurringPaymentsOverviewScreenViewModel"
    }

    override val viewState: MutableStateFlow<RecurringPaymentsOverviewScreenViewState> =
        MutableStateFlow(RecurringPaymentsOverviewScreenViewState.Loading)

    override val viewEffects: MutableSharedFlow<RecurringPaymentsOverviewScreenViewEffects> =
        MutableSharedFlow()

    init {
        fetchRecurringPayments()
    }

    private fun fetchRecurringPayments() {
        viewModelScope.launch {
            combine(
                getRecurringPaymentsFlowUseCase(),
                getLocaleOrDefaultStream()
            ) { recurringPaymentsUi, locale ->
                recurringPaymentsUi to locale
            }
                .onEach { (recurringPaymentsUi, locale) ->
                    if (recurringPaymentsUi.isEmpty()) {
                        viewState.update { RecurringPaymentsOverviewScreenViewState.Empty }
                        return@onEach
                    }

                    viewState.update {
                        RecurringPaymentsOverviewScreenViewState.Content(
                            recurringPayments = recurringPaymentsUi,
                            locale = locale
                        )
                    }
                }
                .catch {
                    viewState.update {
                        RecurringPaymentsOverviewScreenViewState.Empty
                    }
                }
                .launchIn(this)
        }
    }

    override fun deleteRecurringPayment(recurringPaymentId: Long) {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e(TAG, "\uD83D\uDCA5 Error deleting recurring payment", throwable)
        }

        viewModelScope.launch(coroutineExceptionHandler) {
            deleteRecurringPaymentUseCase(recurringPaymentId)
        }
    }
}
