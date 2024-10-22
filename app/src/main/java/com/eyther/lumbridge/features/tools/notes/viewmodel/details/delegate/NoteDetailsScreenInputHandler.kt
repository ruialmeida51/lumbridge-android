package com.eyther.lumbridge.features.tools.notes.viewmodel.details.delegate

import com.eyther.lumbridge.features.tools.notes.model.details.NoteDetailsScreenInputState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class NoteDetailsScreenInputHandler @Inject constructor() : INoteDetailsScreenInputHandler {
    override val inputState: MutableStateFlow<NoteDetailsScreenInputState> =
        MutableStateFlow(NoteDetailsScreenInputState())

    override fun onTitleChanged(name: String?) {
        updateInput { state ->
            state.copy(
                title = state.title.copy(
                    text = name
                )
            )
        }
    }

    override fun onTextUpdated(text: String?) {
        updateInput { state ->
            state.copy(
                text = state.text.copy(
                    text = text
                )
            )
        }
    }

    override fun updateInput(update: (NoteDetailsScreenInputState) -> NoteDetailsScreenInputState) {
        inputState.update { currentState ->
            update(currentState)
        }
    }
}
