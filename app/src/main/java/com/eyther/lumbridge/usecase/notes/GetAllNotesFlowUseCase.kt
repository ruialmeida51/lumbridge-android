package com.eyther.lumbridge.usecase.notes

import com.eyther.lumbridge.domain.model.notes.Note
import com.eyther.lumbridge.domain.repository.notes.NotesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllNotesFlowUseCase @Inject constructor(
    private val notesRepository: NotesRepository
) {
    operator fun invoke(): Flow<List<Note>> = notesRepository.notesListFLow
}
