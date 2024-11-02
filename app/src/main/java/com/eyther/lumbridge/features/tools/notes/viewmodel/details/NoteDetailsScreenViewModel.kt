package com.eyther.lumbridge.features.tools.notes.viewmodel.details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyther.lumbridge.features.tools.notes.model.details.NoteDetailsScreenViewEffect
import com.eyther.lumbridge.features.tools.notes.model.details.NoteDetailsScreenViewState
import com.eyther.lumbridge.features.tools.notes.viewmodel.details.delegate.INoteDetailsScreenInputHandler
import com.eyther.lumbridge.features.tools.notes.viewmodel.details.delegate.NoteDetailsScreenInputHandler
import com.eyther.lumbridge.features.tools.navigation.ToolsNavigationItem.Notes.Companion.ARG_NOTE_ID
import com.eyther.lumbridge.model.notes.NoteUi
import com.eyther.lumbridge.usecase.notes.DeleteNoteUseCase
import com.eyther.lumbridge.usecase.notes.GetNoteUseCase
import com.eyther.lumbridge.usecase.notes.SaveNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

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
    }

    override val viewState: MutableStateFlow<NoteDetailsScreenViewState> =
        MutableStateFlow(NoteDetailsScreenViewState.Loading)

    override val viewEffects: MutableSharedFlow<NoteDetailsScreenViewEffect> =
        MutableSharedFlow()

    private val noteId = requireNotNull(savedStateHandle.get<Long>(ARG_NOTE_ID)) {
        "Note ID must be provided or defaulted to -1"
    }

    init {
        viewModelScope.launch {
            inputState
                .onEach { newInputState ->
                    viewState.update {
                        NoteDetailsScreenViewState.Content(newInputState)
                    }
                }
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

    override fun saveNotes(defaultTitle: String) {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e(TAG, "Error saving note $noteId", throwable)
        }

        viewModelScope.launch(coroutineExceptionHandler) {
            if (inputState.value.text.text.isNullOrEmpty()) {
                if (noteId != -1L) {
                    deleteNoteUseCase(noteId)
                }

                return@launch
            }

            val noteUi = NoteUi(
                id = noteId,
                title = inputState.value.title.text.orEmpty().ifEmpty { defaultTitle },
                text = inputState.value.text.text.orEmpty()
            )

            saveNoteUseCase(noteUi)
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
