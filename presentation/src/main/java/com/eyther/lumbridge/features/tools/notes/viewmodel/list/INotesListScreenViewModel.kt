package com.eyther.lumbridge.features.tools.notes.viewmodel.list

import com.eyther.lumbridge.features.tools.notes.model.list.NotesListScreenViewState
import com.eyther.lumbridge.model.notes.NoteUi
import kotlinx.coroutines.flow.StateFlow

interface INotesListScreenViewModel {
    val viewState: StateFlow<NotesListScreenViewState>

    /**
     * Attempt to delete a note from the database.
     *
     * @param note The note to delete.
     */
    fun onDeleteNote(note: NoteUi)
}
