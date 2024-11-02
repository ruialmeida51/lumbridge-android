package com.eyther.lumbridge.usecase.recurringpayments

import com.eyther.lumbridge.domain.repository.recurringpayments.RecurringPaymentsRepository
import com.eyther.lumbridge.mapper.recurringpayments.toUi
import com.eyther.lumbridge.model.recurringpayments.RecurringPaymentUi
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
