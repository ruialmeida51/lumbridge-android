package com.eyther.lumbridge.features.feed.model.overview

sealed interface FeedOverviewScreenViewEffects {
    data class ScrollToIndex(
        val index: Int
    ) : FeedOverviewScreenViewEffects
}
