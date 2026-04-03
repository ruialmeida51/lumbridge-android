package com.eyther.lumbridge.domain.usecase.reminders

import com.eyther.lumbridge.domain.model.reminders.ReminderDomain
import com.eyther.lumbridge.domain.repository.reminders.RemindersRepository
import javax.inject.Inject

class GetReminderByIdUseCase @Inject constructor(
    private val remindersRepository: RemindersRepository
) {
    suspend operator fun invoke(reminderId: Long): ReminderDomain? = remindersRepository
        .getReminderById(reminderId)
}
