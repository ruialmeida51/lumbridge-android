package com.eyther.lumbridge.features.tools.notes.viewmodel.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyther.lumbridge.features.tools.notes.model.list.NotesListScreenViewState
import com.eyther.lumbridge.model.notes.NoteUi
import com.eyther.lumbridge.usecase.notes.DeleteNoteUseCase
import com.eyther.lumbridge.usecase.notes.GetAllNotesFlowUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesListScreenViewModel @Inject constructor(
    private val getAllNotesFlowUseCase: GetAllNotesFlowUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase
) : ViewModel(),
    INotesListScreenViewModel {

    companion object {
        private const val TAG = "NotesListScreenViewModel"
    }

    override val viewState: MutableStateFlow<NotesListScreenViewState> =
        MutableStateFlow(NotesListScreenViewState.Loading)

    init {
        fetchNotes()
    }

    private fun fetchNotes() {
        viewModelScope.launch {
            val notesFlow = getAllNotesFlowUseCase()

            notesFlow
                .onEach { notes ->
                    viewState.update {
                        if (notes.isEmpty()) {
                            return@update NotesListScreenViewState.Empty
                        }

                        NotesListScreenViewState.Content(
                            notesList = notes
                        )
                    }
                }
                .launchIn(this)
        }
    }

    override fun onDeleteNote(note: NoteUi) {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e(TAG, "💥 Error deleting note $note", throwable)
        }

        viewModelScope.launch(coroutineExceptionHandler) {
            deleteNoteUseCase(note.id)
        }
    }
}
