package com.eyther.lumbridge.domain.usecase.notes

import com.eyther.lumbridge.domain.repository.notes.NotesRepository
import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(
    private val notesRepository: NotesRepository
) {
    suspend operator fun invoke(noteId: Long) {
        notesRepository.deleteNoteById(noteId)
    }
}
