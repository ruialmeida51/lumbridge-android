package com.eyther.lumbridge.domain.usecase.recurringpayments

import com.eyther.lumbridge.domain.repository.recurringpayments.RecurringPaymentsRepository
import com.eyther.lumbridge.domain.mapper.recurringpayments.toUi
import com.eyther.lumbridge.domain.model.recurringpayments.RecurringPaymentUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetRecurringPaymentsFlowUseCase @Inject constructor(
    private val recurringPaymentsRepository: RecurringPaymentsRepository
) {
    operator fun invoke(): Flow<List<RecurringPaymentUi>> = recurringPaymentsRepository
        .recurringPaymentsFlow
        .map { payments ->
            payments
                .toUi()
                .sortedBy { it.periodicity }
        }
}
