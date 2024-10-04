package com.eyther.lumbridge.features.feed.viewmodel.bottomsheet

import com.eyther.lumbridge.features.feed.model.bottomsheet.FeedAddOrEditBottomSheetViewState
import com.eyther.lumbridge.features.feed.viewmodel.delegate.IFeedAddOrEditBottomSheetInputHandler
import com.eyther.lumbridge.model.news.RssFeedUi
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import kotlinx.coroutines.flow.StateFlow

interface IFeedAddOrEditBottomSheetViewModel : IFeedAddOrEditBottomSheetInputHandler {
    val viewState: StateFlow<FeedAddOrEditBottomSheetViewState>

    /**
     * Adds or updates a feed in the list of available feeds.
     *
     * @param name The name of the feed, the primary key.
     * @param url The URL of the feed.
     */
    fun onAddOrUpdateFeed(name: String, url: String)

    /**
     * Deletes a feed from the list of available feeds.
     */
    fun onDeleteFeed()

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("selectedFeed") selectedRssFeedUi: RssFeedUi?
        ): FeedAddOrEditBottomSheetViewModel
    }
}
