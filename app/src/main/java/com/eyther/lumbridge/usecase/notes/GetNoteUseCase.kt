package com.eyther.lumbridge.usecase.notes

import com.eyther.lumbridge.domain.model.notes.Note
import com.eyther.lumbridge.domain.repository.notes.NotesRepository
import javax.inject.Inject

class GetNoteUseCase @Inject constructor(
    private val notesRepository: NotesRepository
) {
    suspend operator fun invoke(noteId: Long): Note? = notesRepository.getNoteById(noteId)
}
