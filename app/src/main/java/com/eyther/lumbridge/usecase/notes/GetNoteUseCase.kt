package com.eyther.lumbridge.usecase.notes

import com.eyther.lumbridge.domain.repository.notes.NotesRepository
import com.eyther.lumbridge.mapper.notes.toUi
import javax.inject.Inject

class GetNoteUseCase @Inject constructor(
    private val notesRepository: NotesRepository
) {
    suspend operator fun invoke(noteId: Long) = notesRepository
        .getNoteById(noteId)
        ?.toUi()
}
