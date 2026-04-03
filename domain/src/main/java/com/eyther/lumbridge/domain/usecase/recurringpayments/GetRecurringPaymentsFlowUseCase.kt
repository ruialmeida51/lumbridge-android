package com.eyther.lumbridge.domain.usecase.recurringpayments

import com.eyther.lumbridge.domain.model.recurringpayments.RecurringPaymentDomain
import com.eyther.lumbridge.domain.repository.recurringpayments.RecurringPaymentsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecurringPaymentsFlowUseCase @Inject constructor(
    private val recurringPaymentsRepository: RecurringPaymentsRepository
) {
    operator fun invoke(): Flow<List<RecurringPaymentDomain>> = recurringPaymentsRepository.recurringPaymentsFlow
}
