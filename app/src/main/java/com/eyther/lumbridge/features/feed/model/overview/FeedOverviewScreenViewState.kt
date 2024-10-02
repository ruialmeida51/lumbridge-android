package com.eyther.lumbridge.features.feed.model.overview

import com.eyther.lumbridge.model.news.FeedItemUi
import com.eyther.lumbridge.model.news.RssFeedUi

sealed class FeedOverviewScreenViewState(
    open val availableFeeds: List<RssFeedUi>,
    open val selectedFeed: RssFeedUi?
) {
    data object Empty : FeedOverviewScreenViewState(emptyList(), null)

    data class Loading(
        override val availableFeeds: List<RssFeedUi> = emptyList(),
        override val selectedFeed: RssFeedUi? = null
    ) : FeedOverviewScreenViewState(availableFeeds, selectedFeed)

    data class Error(
        override val availableFeeds: List<RssFeedUi> = emptyList(),
        override val selectedFeed: RssFeedUi? = null
    ) : FeedOverviewScreenViewState(availableFeeds, selectedFeed)

    data class Content(
        override val availableFeeds: List<RssFeedUi>,
        override val selectedFeed: RssFeedUi,
        val feedItems: List<FeedItemUi>
    ) : FeedOverviewScreenViewState(availableFeeds, selectedFeed)

    fun shouldDisplayEditFeedIcon() = this.availableFeeds.isNotEmpty()
}
