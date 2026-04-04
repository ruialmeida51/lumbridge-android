package com.eyther.lumbridge.domain.usecase.reminders

import com.eyther.lumbridge.domain.model.reminders.ReminderDomain
import com.eyther.lumbridge.domain.repository.reminders.RemindersRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRemindersFlowUseCase @Inject constructor(
    private val remindersRepository: RemindersRepository
) {
    operator fun invoke(): Flow<List<ReminderDomain>> = remindersRepository.remindersFlow
}
