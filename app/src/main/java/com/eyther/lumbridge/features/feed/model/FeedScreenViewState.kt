package com.eyther.lumbridge.features.feed.model

import com.eyther.lumbridge.model.news.FeedItemUi
import com.eyther.lumbridge.model.news.RssFeedUi

sealed class FeedScreenViewState(
    open val availableFeeds: List<RssFeedUi>,
    open val selectedFeed: RssFeedUi?
) {
    data class Loading(
        override val availableFeeds: List<RssFeedUi> = emptyList(),
        override val selectedFeed: RssFeedUi? = null
    ) : FeedScreenViewState(availableFeeds, selectedFeed)

    data class Empty(
        override val availableFeeds: List<RssFeedUi> = emptyList(),
        override val selectedFeed: RssFeedUi? = null
    ) : FeedScreenViewState(availableFeeds, selectedFeed)

    data class Content(
        override val availableFeeds: List<RssFeedUi>,
        override val selectedFeed: RssFeedUi,
        val feedItems: List<FeedItemUi>
    ) : FeedScreenViewState(availableFeeds, selectedFeed)
}
