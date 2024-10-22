package com.eyther.lumbridge.data.mappers.notes

import com.eyther.lumbridge.data.model.notes.entity.NoteEntity
import com.eyther.lumbridge.data.model.notes.local.NoteCached

fun NoteEntity.toCached() = NoteCached(
    id = noteId,
    title = title,
    text = text
)
