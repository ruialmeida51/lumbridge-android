package com.eyther.lumbridge.data.datasource.notes.local

import com.eyther.lumbridge.data.datasource.notes.dao.NotesDao
import com.eyther.lumbridge.data.mappers.notes.toCached
import com.eyther.lumbridge.data.mappers.notes.toEntity
import com.eyther.lumbridge.data.model.notes.local.NoteCached
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class NotesLocalDataSource @Inject constructor(
    private val notesDao: NotesDao
) {
    val notesFlow: Flow<List<NoteCached>> = notesDao.getAllNotes()
        .mapNotNull { flowItem ->
            flowItem?.map { noteEntity -> noteEntity.toCached() }
        }

    suspend fun saveNote(note: NoteCached) {
        if (note.id == -1L) {
            notesDao.insertNote(note.toEntity())
        } else {
            notesDao.updateNote(note.toEntity().copy(noteId = note.id))
        }
    }

    suspend fun deleteNoteById(noteId: Long) {
        notesDao.deleteNoteById(noteId)
    }

    suspend fun getNoteById(noteId: Long): NoteCached? {
        return notesDao.getNoteById(noteId)?.toCached()
    }
}
