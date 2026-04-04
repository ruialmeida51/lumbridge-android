package com.eyther.lumbridge.domain.usecase.reminders

import com.eyther.lumbridge.domain.model.reminders.ReminderDomain
import javax.inject.Inject

class SetReminderAsNotifiedUseCase @Inject constructor(
    private val saveReminderUseCase: SaveReminderUseCase
) {
    suspend operator fun invoke(reminderDomain: ReminderDomain) {
        saveReminderUseCase(reminderDomain.copy(alreadyNotified = true))
    }
}
