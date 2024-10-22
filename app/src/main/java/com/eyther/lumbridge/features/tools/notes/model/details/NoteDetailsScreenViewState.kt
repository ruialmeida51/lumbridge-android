package com.eyther.lumbridge.features.tools.notes.model.details

sealed interface NoteDetailsScreenViewState  {
    data object Loading: NoteDetailsScreenViewState

    data class Content(
        val inputState: NoteDetailsScreenInputState = NoteDetailsScreenInputState()
    ): NoteDetailsScreenViewState
}
