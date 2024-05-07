package com.eyther.lumbridge.features.feed.viewmodel

import com.eyther.lumbridge.features.feed.model.FeedScreenViewState
import com.eyther.lumbridge.model.news.RssFeedUi
import kotlinx.coroutines.flow.StateFlow

interface IFeedScreenViewModel {
    val viewState: StateFlow<FeedScreenViewState>

    /**
     * Attempts to refresh the news feed.
     */
    fun refresh()

    /**
     * Selects a feed to display.
     * @param rssFeedUi The feed to display.
     * @see RssFeedUi
     */
    fun selectFeed(rssFeedUi: RssFeedUi? = null)
}
