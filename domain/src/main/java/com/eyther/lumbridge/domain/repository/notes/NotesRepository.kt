package com.eyther.lumbridge.domain.repository.notes

import com.eyther.lumbridge.domain.model.notes.Note
import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    val notesListFLow: Flow<List<Note>>
    suspend fun saveNote(note: Note): Long
    suspend fun deleteNoteById(noteId: Long)
    suspend fun getNoteById(noteId: Long): Note?
}
