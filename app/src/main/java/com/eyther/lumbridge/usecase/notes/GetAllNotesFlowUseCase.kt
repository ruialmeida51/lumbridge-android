package com.eyther.lumbridge.usecase.notes

import com.eyther.lumbridge.domain.repository.notes.NotesRepository
import com.eyther.lumbridge.mapper.notes.toUi
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllNotesFlowUseCase @Inject constructor(
    private val notesRepository: NotesRepository
) {
    operator fun invoke() = notesRepository
        .notesListFLow
        .map { it.toUi() }
}
