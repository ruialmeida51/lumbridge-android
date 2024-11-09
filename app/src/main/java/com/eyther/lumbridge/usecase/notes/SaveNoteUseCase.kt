package com.eyther.lumbridge.usecase.notes

import com.eyther.lumbridge.domain.repository.notes.NotesRepository
import com.eyther.lumbridge.mapper.notes.toDomain
import com.eyther.lumbridge.model.notes.NoteUi
import javax.inject.Inject

class SaveNoteUseCase @Inject constructor(
    private val notesRepository: NotesRepository
) {
    suspend operator fun invoke(note: NoteUi): Long {
        return notesRepository.saveNote(note.toDomain())
    }
}
