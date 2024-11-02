package com.eyther.lumbridge.usecase.recurringpayments

import com.eyther.lumbridge.domain.repository.recurringpayments.RecurringPaymentsRepository
import javax.inject.Inject

class DeleteRecurringPaymentUseCase @Inject constructor(
    private val recurringPaymentsRepository: RecurringPaymentsRepository
) {
    suspend operator fun invoke(recurringPaymentId: Long) =
        recurringPaymentsRepository.deleteRecurringPaymentById(recurringPaymentId)
}
