package com.eyther.lumbridge.domain.usecase.reminders

import com.eyther.lumbridge.domain.repository.reminders.RemindersRepository
import com.eyther.lumbridge.domain.mapper.reminders.toDomain
import com.eyther.lumbridge.domain.model.reminders.ReminderUi
import javax.inject.Inject

class SaveReminderUseCase @Inject constructor(
    private val remindersRepository: RemindersRepository
) {
    suspend operator fun invoke(reminder: ReminderUi) {
        remindersRepository.saveReminder(reminder.toDomain())
    }
}
