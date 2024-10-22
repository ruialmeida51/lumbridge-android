package com.eyther.lumbridge.features.tools.notes.viewmodel.details.delegate

import com.eyther.lumbridge.features.tools.notes.model.details.NoteDetailsScreenInputState
import kotlinx.coroutines.flow.StateFlow

interface INoteDetailsScreenInputHandler {
    val inputState: StateFlow<NoteDetailsScreenInputState>

    /**
     * Methods called on different input changes. Each one updates the input state with
     * the new value and performs any necessary validation.
     */
    fun onTitleChanged(name: String?)
    fun onTextUpdated(text: String?)

    /**
     * Helper function to update the input state.
     * @param update the lambda to update the input state.
     * @see NoteDetailsScreenInputState
     */
    fun updateInput(update: (NoteDetailsScreenInputState) -> NoteDetailsScreenInputState)
}
