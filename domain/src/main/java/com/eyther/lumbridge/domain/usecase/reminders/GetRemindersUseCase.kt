package com.eyther.lumbridge.domain.usecase.reminders

import com.eyther.lumbridge.domain.repository.reminders.RemindersRepository
import com.eyther.lumbridge.domain.mapper.reminders.toUi
import javax.inject.Inject

class GetRemindersUseCase @Inject constructor(
    private val remindersRepository: RemindersRepository
) {
    suspend operator fun invoke() = remindersRepository
        .getAllReminders()
        .toUi()
}
