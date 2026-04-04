package com.eyther.lumbridge.data.mapper.notes

import com.eyther.lumbridge.data.model.notes.local.NoteCached
import com.eyther.lumbridge.domain.model.notes.Note

fun Note.toCached() = NoteCached(
    id = id,
    title = title,
    text = text
)
