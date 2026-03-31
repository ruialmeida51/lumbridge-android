package com.eyther.lumbridge.domain.usecase.recurringpayments

import com.eyther.lumbridge.domain.repository.recurringpayments.RecurringPaymentsRepository
import com.eyther.lumbridge.domain.mapper.recurringpayments.toUi
import com.eyther.lumbridge.domain.model.recurringpayments.RecurringPaymentUi
import javax.inject.Inject

class GetRecurringPaymentByIdUseCase @Inject constructor(
    private val recurringPaymentsRepository: RecurringPaymentsRepository
) {
    suspend operator fun invoke(recurringPaymentId: Long): RecurringPaymentUi? = recurringPaymentsRepository
        .getRecurringPaymentById(recurringPaymentId)
        ?.toUi()
}