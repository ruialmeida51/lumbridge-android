package com.eyther.lumbridge.data.repository.notes

import com.eyther.lumbridge.domain.repository.notes.NotesRepository
import com.eyther.lumbridge.data.datasource.notes.local.NotesLocalDataSource
import com.eyther.lumbridge.domain.mapper.notes.toCached
import com.eyther.lumbridge.domain.mapper.notes.toDomain
import com.eyther.lumbridge.domain.model.notes.Note
import com.eyther.lumbridge.shared.di.model.Schedulers
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NotesRepositoryImpl @Inject constructor(
    private val notesLocalDataSource: NotesLocalDataSource,
    private val schedulers: Schedulers
) : NotesRepository {
    override val notesListFLow = notesLocalDataSource
        .notesFlow
        .mapNotNull { it.toDomain() }

    override suspend fun saveNote(note: Note): Long = withContext(schedulers.io) {
        notesLocalDataSource.saveNote(note.toCached())
    }

    override suspend fun deleteNoteById(noteId: Long) = withContext(schedulers.io) {
        notesLocalDataSource.deleteNoteById(noteId)
    }

    override suspend fun getNoteById(noteId: Long): Note? = withContext(schedulers.io) {
        notesLocalDataSource.getNoteById(noteId)?.toDomain()
    }
}
