package com.eyther.lumbridge.features.feed.model

sealed interface FeedScreenViewEffects {
    data class ScrollToIndex(val index: Int) : FeedScreenViewEffects
}
