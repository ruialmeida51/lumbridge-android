package com.eyther.lumbridge.features.tools.notes.model.details

import com.eyther.lumbridge.ui.common.composables.model.input.TextInputState

/**
 * Represents the input state for the Note Details screen.
 *
 * @property title The title of the list.
 * @property text The text of the note.
 */
data class NoteDetailsScreenInputState(
    val title: TextInputState = TextInputState(),
    val text: TextInputState = TextInputState()
)
