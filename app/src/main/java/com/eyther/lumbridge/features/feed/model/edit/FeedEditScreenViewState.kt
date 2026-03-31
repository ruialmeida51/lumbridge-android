package com.eyther.lumbridge.features.feed.model.edit

import com.eyther.lumbridge.domain.model.news.RssFeedUi

sealed interface FeedEditScreenViewState {
    data object Loading : FeedEditScreenViewState
    data object NoFeeds : FeedEditScreenViewState

    data class HasFeeds(
        val currentFeeds: List<RssFeedUi>
    ) : FeedEditScreenViewState
}
