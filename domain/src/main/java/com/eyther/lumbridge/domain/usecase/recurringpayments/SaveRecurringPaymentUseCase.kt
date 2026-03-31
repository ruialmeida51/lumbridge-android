package com.eyther.lumbridge.domain.usecase.recurringpayments

import com.eyther.lumbridge.domain.repository.recurringpayments.RecurringPaymentsRepository
import com.eyther.lumbridge.domain.mapper.recurringpayments.toDomain
import com.eyther.lumbridge.domain.model.recurringpayments.RecurringPaymentUi
import javax.inject.Inject

class SaveRecurringPaymentUseCase @Inject constructor(
    private val recurringPaymentsRepository: RecurringPaymentsRepository
) {
    suspend operator fun invoke(recurringPayment: RecurringPaymentUi) {
        recurringPaymentsRepository.saveRecurringPayment(recurringPayment.toDomain())
    }
}
