package com.eyther.lumbridge.features.tools.notes.viewmodel.details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyther.lumbridge.features.tools.navigation.ToolsNavigationItem.Notes.Companion.ARG_NOTE_ID
import com.eyther.lumbridge.features.tools.notes.model.details.NoteDetailsScreenViewEffect
import com.eyther.lumbridge.features.tools.notes.model.details.NoteDetailsScreenViewState
import com.eyther.lumbridge.features.tools.notes.viewmodel.details.delegate.INoteDetailsScreenInputHandler
import com.eyther.lumbridge.features.tools.notes.viewmodel.details.delegate.NoteDetailsScreenInputHandler
import com.eyther.lumbridge.model.notes.NoteUi
import com.eyther.lumbridge.usecase.notes.DeleteNoteUseCase
import com.eyther.lumbridge.usecase.notes.GetNoteUseCase
import com.eyther.lumbridge.usecase.notes.SaveNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class NoteDetailsScreenViewModel @Inject constructor(
    private val getNoteUseCase: GetNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val saveNoteUseCase: SaveNoteUseCase,
    private val noteDetailsScreenInputHandler: NoteDetailsScreenInputHandler,
    savedStateHandle: SavedStateHandle
) : ViewModel(),
    INoteDetailsScreenViewModel,
    INoteDetailsScreenInputHandler by noteDetailsScreenInputHandler {

    companion object {
        private const val TAG = "NoteDetailsScreenViewModel"
        private const val NOTES_SAVE_DEBOUNCE_MS = 500L
    }

    override val viewState: MutableStateFlow<NoteDetailsScreenViewState> =
        MutableStateFlow(NoteDetailsScreenViewState.Loading)

    override val viewEffects: MutableSharedFlow<NoteDetailsScreenViewEffect> =
        MutableSharedFlow()

    private var noteId = requireNotNull(savedStateHandle.get<Long>(ARG_NOTE_ID)) {
        "Note ID must be provided or defaulted to -1"
    }

    private var cachedDefaultTitle: String? = null

    init {
        viewModelScope.launch {
            inputState
                .onEach { newInputState ->
                    viewState.update {
                        NoteDetailsScreenViewState.Content(newInputState)
                    }
                }
                .launchIn(this)

            // Attempt to save the notes every time the input state changes,
            // with a debounce of NOTES_SAVE_DEBOUNCE_MS to avoid saving too often
            // while the user is still typing.
            inputState
                .debounce(NOTES_SAVE_DEBOUNCE_MS)
                .onEach { saveNotes(finish = false) }
                .launchIn(this)

            loadNoteDetails()
        }
    }

    private fun loadNoteDetails() {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e(TAG, "Error loading note $noteId", throwable)
        }

        viewModelScope.launch(coroutineExceptionHandler) {
            val note = getNoteUseCase(noteId)

            if (note == null) {
                viewState.update { NoteDetailsScreenViewState.Content() }
                return@launch
            }

            updateInput {
                it.copy(
                    title = it.title.copy(text = note.title),
                    text = it.text.copy(text = note.text)
                )
            }
        }
    }

    override fun setDefaultTitle(defaultTitle: String) {
        cachedDefaultTitle = defaultTitle
    }

    override fun saveNotes(finish: Boolean) {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            viewModelScope.launch {
                Log.e(TAG, "Error saving note $noteId", throwable)
                viewEffects.emit(NoteDetailsScreenViewEffect.NavigateBack)
            }
        }

        viewModelScope.launch(coroutineExceptionHandler) {
            val noteUi = NoteUi(
                id = noteId,
                title = inputState.value.title.text.orEmpty().ifEmpty { cachedDefaultTitle.orEmpty() },
                text = inputState.value.text.text.orEmpty()
            )

            // We need to keep updating this because: If the note ID was -1 when opening the screen,
            // it means that it's a new note and we need to update the ID with the one returned by
            // the database. This is necessary to avoid saving notes multiple times, as a -1L ID is
            // used to indicate a new note.
            noteId = saveNoteUseCase(noteUi)

            if (finish) {
                viewEffects.emit(NoteDetailsScreenViewEffect.NavigateBack)
            }
        }
    }

    override fun onDeleteNote() {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e(TAG, "Error deleting note $noteId", throwable)
        }

        viewModelScope.launch(coroutineExceptionHandler) {
            deleteNoteUseCase(noteId)
            viewEffects.emit(NoteDetailsScreenViewEffect.NavigateBack)
        }
    }
}
