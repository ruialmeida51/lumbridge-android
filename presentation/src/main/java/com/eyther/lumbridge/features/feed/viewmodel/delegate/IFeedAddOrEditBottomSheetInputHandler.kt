package com.eyther.lumbridge.features.feed.viewmodel.delegate

import com.eyther.lumbridge.features.feed.model.bottomsheet.FeedAddOrEditBottomSheetInputState
import kotlinx.coroutines.flow.StateFlow

interface IFeedAddOrEditBottomSheetInputHandler {
    val inputState: StateFlow<FeedAddOrEditBottomSheetInputState>

    /**
     * Methods called on different input changes. Each one updates the input state with
     * the new value and performs any necessary validation.
     */
    fun onNameChanged(name: String?)
    fun onUrlChanged(url: String?)
    /**
     * Validates the entire input state and returns an error message if the input state is invalid.
     * @return an error message if the percentages are invalid, null otherwise.
     * @see FeedAddOrEditBottomSheetInputState
     */
    fun validateInput(inputState: FeedAddOrEditBottomSheetInputState): Boolean

    /**
     * Checks if we have enough information available to enable the button.
     * @return true if we have enough information to enable the button, false otherwise.
     * @see FeedAddOrEditBottomSheetInputState
     */
    fun shouldEnableSaveButton(inputState: FeedAddOrEditBottomSheetInputState): Boolean

    /**
     * Helper function to update the input state.
     * @param update the lambda to update the input state.
     * @see FeedAddOrEditBottomSheetInputState
     */
    fun updateInput(update: (FeedAddOrEditBottomSheetInputState) -> FeedAddOrEditBottomSheetInputState)
}
