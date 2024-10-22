package com.eyther.lumbridge.features.tools.notes.viewmodel.details

import com.eyther.lumbridge.features.tools.notes.model.details.NoteDetailsScreenViewEffect
import com.eyther.lumbridge.features.tools.notes.model.details.NoteDetailsScreenViewState
import com.eyther.lumbridge.features.tools.notes.viewmodel.details.delegate.INoteDetailsScreenInputHandler
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface INoteDetailsScreenViewModel : INoteDetailsScreenInputHandler {
    val viewState: StateFlow<NoteDetailsScreenViewState>
    val viewEffects: SharedFlow<NoteDetailsScreenViewEffect>

    /**
     * Saves the notes list. If the user didn't provide a title, it uses the default title.
     *
     * This will be called on onPause and on onResume. If the user leaves the screen,
     * we save the list if there is any content to save. The user might've just opened the screen
     * and closed it without adding anything.
     *
     * @param defaultTitle the default title to use if the user didn't provide one
     */
    fun saveNotes(defaultTitle: String)

    /**
     * Attempts to delete the note. It uses the note ID stored in the view model to delete the note.
     */
    fun onDeleteNote()
}
