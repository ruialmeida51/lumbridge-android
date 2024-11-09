package com.eyther.lumbridge.domain.repository.notes

import com.eyther.lumbridge.data.datasource.notes.local.NotesLocalDataSource
import com.eyther.lumbridge.domain.mapper.notes.toCached
import com.eyther.lumbridge.domain.mapper.notes.toDomain
import com.eyther.lumbridge.domain.model.notes.Note
import com.eyther.lumbridge.shared.di.model.Schedulers
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NotesRepository @Inject constructor(
    private val notesLocalDataSource: NotesLocalDataSource,
    private val schedulers: Schedulers
) {
    val notesListFLow = notesLocalDataSource
        .notesFlow
        .mapNotNull { it.toDomain() }

    suspend fun saveNote(note: Note): Long = withContext(schedulers.io) {
        notesLocalDataSource.saveNote(note.toCached())
    }

    suspend fun deleteNoteById(noteId: Long) = withContext(schedulers.io) {
        notesLocalDataSource.deleteNoteById(noteId)
    }

    suspend fun getNoteById(noteId: Long): Note? = withContext(schedulers.io) {
        notesLocalDataSource.getNoteById(noteId)?.toDomain()
    }
}
