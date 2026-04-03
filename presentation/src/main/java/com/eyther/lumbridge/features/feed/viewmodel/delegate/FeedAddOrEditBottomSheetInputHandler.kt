package com.eyther.lumbridge.features.feed.viewmodel.delegate

import android.webkit.URLUtil
import com.eyther.lumbridge.R
import com.eyther.lumbridge.extensions.kotlin.getErrorOrNull
import com.eyther.lumbridge.features.editfinancialprofile.model.EditFinancialProfileScreenViewState.Content
import com.eyther.lumbridge.features.feed.model.bottomsheet.FeedAddOrEditBottomSheetInputState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class FeedAddOrEditBottomSheetInputHandler @Inject constructor() : IFeedAddOrEditBottomSheetInputHandler {
    override val inputState = MutableStateFlow(FeedAddOrEditBottomSheetInputState())

    override fun onNameChanged(name: String?) {
        updateInput { state ->
            state.copy(
                feedName = state.feedName.copy(
                    text = name,
                    error = name.getErrorOrNull(
                        R.string.feed_edit_rss_invalid_name
                    )
                )
            )
        }
    }

    override fun onUrlChanged(url: String?) {
        val urlText = url.takeIf { URLUtil.isNetworkUrl(url) }

        updateInput { state ->
            state.copy(
                feedUrl = state.feedUrl.copy(
                    text = urlText,
                    error = urlText.getErrorOrNull(
                        R.string.feed_edit_rss_invalid_url
                    )
                )
            )
        }
    }

    override fun validateInput(inputState: FeedAddOrEditBottomSheetInputState): Boolean {
        return inputState.feedName.isValid() && inputState.feedUrl.isValid()
    }

    override fun shouldEnableSaveButton(inputState: FeedAddOrEditBottomSheetInputState): Boolean {
        return validateInput(inputState)
    }

    /**
     * Helper function to update the inputState state of the screen.
     *
     * @param update the function to update the content state.
     * @see Content
     */
    override fun updateInput(
        update: (FeedAddOrEditBottomSheetInputState) -> FeedAddOrEditBottomSheetInputState
    ) {
        inputState.update { currentState ->
            update(currentState)
        }
    }
}
