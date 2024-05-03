package com.eyther.lumbridge.features.feed.model

import com.eyther.lumbridge.model.news.FeedItemUi

sealed interface FeedScreenViewState {
    data object Loading : FeedScreenViewState
    data object Empty : FeedScreenViewState

    data class Content(
        val feedItems: List<FeedItemUi>
    ) : FeedScreenViewState
}
