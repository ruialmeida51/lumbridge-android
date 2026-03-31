package com.eyther.lumbridge.domain.usecase.reminders

import com.eyther.lumbridge.domain.model.reminders.ReminderUi
import javax.inject.Inject

class SetReminderAsNotifiedUseCase @Inject constructor(
    private val saveReminderUseCase: SaveReminderUseCase
) {
    suspend operator fun invoke(reminderUi: ReminderUi) {
        saveReminderUseCase(reminderUi.copy(alreadyNotified = true))
    }
}
