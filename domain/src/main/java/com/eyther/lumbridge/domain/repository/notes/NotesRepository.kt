package com.eyther.lumbridge.domain.repository.notes

import com.eyther.lumbridge.domain.mapper.notes.toCached
import com.eyther.lumbridge.domain.mapper.notes.toDomain
import com.eyther.lumbridge.domain.model.notes.Note
import com.eyther.lumbridge.shared.di.model.Schedulers
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface NotesRepository {
    val notesListFLow
    suspend fun saveNote(note: Note): Long
    suspend fun deleteNoteById(noteId: Long)
    suspend fun getNoteById(noteId: Long): Note?
}
