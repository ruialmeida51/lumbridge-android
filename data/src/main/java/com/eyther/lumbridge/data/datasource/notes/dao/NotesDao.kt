package com.eyther.lumbridge.data.datasource.notes.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.eyther.lumbridge.data.model.notes.entity.NOTES_LIST_TABLE_NAME
import com.eyther.lumbridge.data.model.notes.entity.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {

    @Transaction
    @Query("SELECT * FROM $NOTES_LIST_TABLE_NAME")
    fun getAllNotes(): Flow<List<NoteEntity>?>

    @Transaction
    @Query("SELECT * FROM $NOTES_LIST_TABLE_NAME WHERE noteId = :notesListId")
    suspend fun getNoteById(notesListId: Long): NoteEntity?

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity): Long

    @Transaction
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateNote(note: NoteEntity)

    @Transaction
    @Query("DELETE FROM $NOTES_LIST_TABLE_NAME WHERE noteId = :notesListId")
    suspend fun deleteNoteById(notesListId: Long)
}
