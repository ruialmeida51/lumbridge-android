package com.eyther.lumbridge.usecase.reminders

import com.eyther.lumbridge.model.reminders.ReminderUi
import javax.inject.Inject

class SetReminderAsNotifiedUseCase @Inject constructor(
    private val saveReminderUseCase: SaveReminderUseCase
) {
    suspend operator fun invoke(reminderUi: ReminderUi) {
        saveReminderUseCase(reminderUi.copy(alreadyNotified = true))
    }
}
