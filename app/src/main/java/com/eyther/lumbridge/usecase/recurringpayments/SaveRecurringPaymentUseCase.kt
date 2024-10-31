package com.eyther.lumbridge.usecase.recurringpayments

import com.eyther.lumbridge.domain.repository.recurringpayments.RecurringPaymentsRepository
import com.eyther.lumbridge.mapper.recurringpayments.toDomain
import com.eyther.lumbridge.model.recurringpayments.RecurringPaymentUi
import javax.inject.Inject

class SaveRecurringPaymentUseCase @Inject constructor(
    private val recurringPaymentsRepository: RecurringPaymentsRepository
) {
    suspend operator fun invoke(recurringPayment: RecurringPaymentUi) {
        recurringPaymentsRepository.saveRecurringPayment(recurringPayment.toDomain())
    }
}
