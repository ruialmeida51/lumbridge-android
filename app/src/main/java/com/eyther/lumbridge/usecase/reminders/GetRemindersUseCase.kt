package com.eyther.lumbridge.usecase.reminders

import com.eyther.lumbridge.domain.repository.reminders.RemindersRepository
import com.eyther.lumbridge.mapper.reminders.toUi
import javax.inject.Inject

class GetRemindersUseCase @Inject constructor(
    private val remindersRepository: RemindersRepository
) {
    suspend operator fun invoke() = remindersRepository
        .getAllReminders()
        .toUi()
}
