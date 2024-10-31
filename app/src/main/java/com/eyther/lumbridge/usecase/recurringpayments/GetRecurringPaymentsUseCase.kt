package com.eyther.lumbridge.usecase.recurringpayments

import com.eyther.lumbridge.domain.repository.recurringpayments.RecurringPaymentsRepository
import com.eyther.lumbridge.mapper.recurringpayments.toUi
import com.eyther.lumbridge.model.recurringpayments.RecurringPaymentUi
import javax.inject.Inject

class GetRecurringPaymentsUseCase @Inject constructor(
    private val recurringPaymentsRepository: RecurringPaymentsRepository
) {
    suspend operator fun invoke(): List<RecurringPaymentUi> = recurringPaymentsRepository
        .getAllRecurringPayments()
        .map { it.toUi() }
}
