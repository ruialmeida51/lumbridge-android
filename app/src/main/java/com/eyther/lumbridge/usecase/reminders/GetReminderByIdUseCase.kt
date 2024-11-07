package com.eyther.lumbridge.usecase.reminders

import com.eyther.lumbridge.domain.repository.reminders.RemindersRepository
import com.eyther.lumbridge.mapper.reminders.toUi
import javax.inject.Inject

class GetReminderByIdUseCase @Inject constructor(
    private val remindersRepository: RemindersRepository
) {
    suspend operator fun invoke(reminderId: Long) = remindersRepository
        .getReminderById(reminderId)
        ?.toUi()
}
