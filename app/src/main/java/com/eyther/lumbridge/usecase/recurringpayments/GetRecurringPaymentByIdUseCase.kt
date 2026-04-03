package com.eyther.lumbridge.usecase.recurringpayments

import com.eyther.lumbridge.domain.model.recurringpayments.RecurringPaymentDomain
import com.eyther.lumbridge.domain.repository.recurringpayments.RecurringPaymentsRepository
import javax.inject.Inject

class GetRecurringPaymentByIdUseCase @Inject constructor(
    private val recurringPaymentsRepository: RecurringPaymentsRepository
) {
    suspend operator fun invoke(recurringPaymentId: Long): RecurringPaymentDomain? = recurringPaymentsRepository
        .getRecurringPaymentById(recurringPaymentId)
}