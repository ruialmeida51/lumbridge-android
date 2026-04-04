package com.eyther.lumbridge.domain.usecase.notes

import com.eyther.lumbridge.domain.model.notes.Note
import com.eyther.lumbridge.domain.repository.notes.NotesRepository
import javax.inject.Inject

class SaveNoteUseCase @Inject constructor(
    private val notesRepository: NotesRepository
) {
    suspend operator fun invoke(note: Note): Long {
        return notesRepository.saveNote(note)
    }
}
