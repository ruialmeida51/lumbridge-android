package com.eyther.lumbridge.usecase.reminders

import com.eyther.lumbridge.domain.model.reminders.ReminderDomain
import com.eyther.lumbridge.domain.repository.reminders.RemindersRepository
import javax.inject.Inject

class GetRemindersUseCase @Inject constructor(
    private val remindersRepository: RemindersRepository
) {
    suspend operator fun invoke(): List<ReminderDomain> = remindersRepository.getAllReminders()
}
