package com.eyther.lumbridge.mapper.notes

import com.eyther.lumbridge.domain.model.notes.Note
import com.eyther.lumbridge.model.notes.NoteUi

fun Note.toUi() = NoteUi(
    id = id,
    title = title,
    text = text
)

fun List<Note>.toUi() = map { it.toUi() }
