package com.eyther.lumbridge.domain.mapper.notes

import com.eyther.lumbridge.data.model.notes.local.NoteCached
import com.eyther.lumbridge.domain.model.notes.Note

fun NoteCached.toDomain() = Note(
    id = id,
    title = title,
    text = text
)

fun List<NoteCached>.toDomain() = map { it.toDomain() }
