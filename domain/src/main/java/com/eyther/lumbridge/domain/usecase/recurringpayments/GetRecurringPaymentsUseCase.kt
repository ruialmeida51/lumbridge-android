package com.eyther.lumbridge.domain.usecase.recurringpayments

import com.eyther.lumbridge.domain.model.recurringpayments.RecurringPaymentDomain
import com.eyther.lumbridge.domain.repository.recurringpayments.RecurringPaymentsRepository
import javax.inject.Inject

class GetRecurringPaymentsUseCase @Inject constructor(
    private val recurringPaymentsRepository: RecurringPaymentsRepository
) {
    suspend operator fun invoke(): List<RecurringPaymentDomain> = recurringPaymentsRepository
        .getAllRecurringPayments()
}
