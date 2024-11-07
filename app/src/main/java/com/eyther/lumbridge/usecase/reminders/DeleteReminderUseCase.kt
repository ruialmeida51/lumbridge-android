package com.eyther.lumbridge.usecase.reminders

import com.eyther.lumbridge.domain.repository.reminders.RemindersRepository
import javax.inject.Inject

class DeleteReminderUseCase @Inject constructor(
    private val reminderRepository: RemindersRepository
) {
    suspend operator fun invoke(reminderId: Long) {
        reminderRepository.deleteReminderById(reminderId)
    }
}
