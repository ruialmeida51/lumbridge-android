package com.eyther.lumbridge.features.feed.viewmodel.edit

import com.eyther.lumbridge.features.feed.model.edit.FeedEditScreenViewEffects
import com.eyther.lumbridge.features.feed.model.edit.FeedEditScreenViewState
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface IFeedEditScreenViewModel {
    companion object {
        data class BottomSheetParams(
            val feedName: String? = null,
            val feedUrl: String? = null
        )
    }

    val viewState: StateFlow<FeedEditScreenViewState>
    val viewEffects: SharedFlow<FeedEditScreenViewEffects>

    /**
     * Attempts to signal we want to edit the feed.
     */
    fun onEditFeedClick(feedName: String, feedUrl: String)

    /**
     * Attempts to signal we want to add a feed.
     */
    fun onAddFeedClick()
}
