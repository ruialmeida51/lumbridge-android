package com.eyther.lumbridge.domain.usecase.notes

import com.eyther.lumbridge.domain.repository.notes.NotesRepository
import com.eyther.lumbridge.domain.mapper.notes.toUi
import javax.inject.Inject

class GetNoteUseCase @Inject constructor(
    private val notesRepository: NotesRepository
) {
    suspend operator fun invoke(noteId: Long) = notesRepository
        .getNoteById(noteId)
        ?.toUi()
}
