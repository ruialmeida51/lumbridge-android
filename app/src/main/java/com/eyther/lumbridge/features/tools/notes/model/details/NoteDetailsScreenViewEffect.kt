package com.eyther.lumbridge.features.tools.notes.model.details

sealed interface NoteDetailsScreenViewEffect {
    data object NavigateBack : NoteDetailsScreenViewEffect
}
