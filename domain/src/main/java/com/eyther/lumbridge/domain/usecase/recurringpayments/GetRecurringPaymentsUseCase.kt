package com.eyther.lumbridge.domain.usecase.recurringpayments

import com.eyther.lumbridge.domain.repository.recurringpayments.RecurringPaymentsRepository
import com.eyther.lumbridge.domain.mapper.recurringpayments.toUi
import com.eyther.lumbridge.domain.model.recurringpayments.RecurringPaymentUi
import javax.inject.Inject

class GetRecurringPaymentsUseCase @Inject constructor(
    private val recurringPaymentsRepository: RecurringPaymentsRepository
) {
    suspend operator fun invoke(): List<RecurringPaymentUi> = recurringPaymentsRepository
        .getAllRecurringPayments()
        .map { it.toUi() }
}
