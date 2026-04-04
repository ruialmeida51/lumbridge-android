package com.eyther.lumbridge.features.tools.notes.model.list

import com.eyther.lumbridge.model.notes.NoteUi

sealed interface NotesListScreenViewState {
    data object Loading: NotesListScreenViewState
    data object Empty: NotesListScreenViewState

    data class Content(
        val notesList: List<NoteUi>
    ): NotesListScreenViewState
}

