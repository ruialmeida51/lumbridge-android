package com.eyther.lumbridge.features.tools.tasksandreminders.viewmodel.edit.delegate

import com.eyther.lumbridge.R
import com.eyther.lumbridge.extensions.kotlin.getErrorOrNull
import com.eyther.lumbridge.features.editfinancialprofile.model.EditFinancialProfileScreenViewState.Content
import com.eyther.lumbridge.features.tools.tasksandreminders.model.edit.RemindersEditScreenInputState
import com.eyther.lumbridge.shared.time.extensions.toLocalDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class RemindersEditScreenInputHandler @Inject constructor() : IRemindersEditScreenInputHandler {

    override val inputState = MutableStateFlow(RemindersEditScreenInputState())

    override fun onNameChanged(name: String?) {
        updateInput { state ->
            state.copy(
                name = state.name.copy(
                    text = name,
                    error = name.getErrorOrNull(
                        R.string.recurring_payment_edit_invalid_name
                    )
                )
            )
        }
    }

    override fun onDueDateChanged(dueDate: Long?) {
        updateInput { state ->
            state.copy(
                dueDate = state.dueDate.copy(
                    date = dueDate?.toLocalDate(),
                    error = dueDate?.toString().getErrorOrNull(
                        R.string.invalid_date
                    )
                )
            )
        }
    }

    /**
     * Checks if the save button should be enabled.
     *
     * The button should be enabled if the user has entered valid data.
     *
     * @param inputState the current state of the screen.
     * @return true if the button should be enabled, false otherwise.
     */
    override fun shouldEnableSaveButton(inputState: RemindersEditScreenInputState): Boolean {
        return inputState.name.isValid() &&
            inputState.dueDate.isValid()
    }

    /**
     * Helper function to update the inputState state of the screen.
     *
     * @param update the function to update the content state.
     * @see Content
     */
    override fun updateInput(
        update: (RemindersEditScreenInputState) -> RemindersEditScreenInputState
    ) {
        inputState.update { currentState ->
            update(currentState)
        }
    }
}
