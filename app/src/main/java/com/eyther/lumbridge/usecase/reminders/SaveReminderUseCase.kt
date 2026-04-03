package com.eyther.lumbridge.usecase.reminders

import com.eyther.lumbridge.domain.model.reminders.ReminderDomain
import com.eyther.lumbridge.domain.repository.reminders.RemindersRepository
import javax.inject.Inject

class SaveReminderUseCase @Inject constructor(
    private val remindersRepository: RemindersRepository
) {
    suspend operator fun invoke(reminder: ReminderDomain) {
        remindersRepository.saveReminder(reminder)
    }
}
