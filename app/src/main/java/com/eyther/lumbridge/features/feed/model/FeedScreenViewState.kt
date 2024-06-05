package com.eyther.lumbridge.features.feed.model

import com.eyther.lumbridge.model.news.FeedItemUi
import com.eyther.lumbridge.model.news.RssFeedUi

sealed interface FeedScreenViewState {
    data object Loading : FeedScreenViewState

    data class Empty(
        val availableFeeds: List<RssFeedUi>,
        val selectedFeed: RssFeedUi? = null
    ) : FeedScreenViewState

    data class Content(
        val feedItems: List<FeedItemUi>,
        val availableFeeds: List<RssFeedUi>,
        val selectedFeed: RssFeedUi? = null
    ) : FeedScreenViewState
}
