package com.eyther.lumbridge.data.model.notes.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

const val NOTES_LIST_TABLE_NAME = "notes_list"

/**
 * Represents a note, containing a title and text.
 *
 * @property noteId The unique identifier for the note.
 * @property title The title of the note.
 * @property text The text of the note.
 */
@Entity(
    tableName = NOTES_LIST_TABLE_NAME
)
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val noteId: Long = 0,
    val title: String,
    val text: String
)
