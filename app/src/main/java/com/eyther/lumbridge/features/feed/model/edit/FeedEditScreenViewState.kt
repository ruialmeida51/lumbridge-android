package com.eyther.lumbridge.features.feed.model.edit

import com.eyther.lumbridge.model.news.RssFeedUi

sealed interface FeedEditScreenViewState {
    data object Loading : FeedEditScreenViewState
    data object NoFeeds : FeedEditScreenViewState

    data class HasFeeds(
        val currentFeeds: List<RssFeedUi>
    ) : FeedEditScreenViewState

    fun shouldShowToolbarIcons() = this is HasFeeds
}
