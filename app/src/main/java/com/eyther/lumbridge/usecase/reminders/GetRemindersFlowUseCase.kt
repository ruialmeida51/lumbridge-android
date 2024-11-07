package com.eyther.lumbridge.usecase.reminders

import com.eyther.lumbridge.domain.repository.reminders.RemindersRepository
import com.eyther.lumbridge.mapper.reminders.toUi
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetRemindersFlowUseCase @Inject constructor(
    private val remindersRepository: RemindersRepository
) {
    operator fun invoke() = remindersRepository
        .remindersFlow
        .map { reminderDomain ->
            val (noLongerRelevantReminders, relevantReminders) =
                reminderDomain
                    .toUi()
                    .sortedBy { it.dueDate }
                    .partition { it.noLongerRelevant() } // Partition the reminders into two lists: no longer relevant and relevant

            // Add the lists in order, so the relevant reminders are shown first
            relevantReminders + noLongerRelevantReminders
        }
}
