package com.eyther.lumbridge.mapper.notes

import com.eyther.lumbridge.domain.model.notes.Note
import com.eyther.lumbridge.model.notes.NoteUi

fun NoteUi.toDomain() = Note(
    id = id,
    title = title,
    text = text
)
