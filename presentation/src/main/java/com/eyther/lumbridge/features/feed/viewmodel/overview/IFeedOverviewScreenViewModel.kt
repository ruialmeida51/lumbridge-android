package com.eyther.lumbridge.features.feed.viewmodel.overview

import com.eyther.lumbridge.features.feed.model.overview.FeedOverviewScreenViewEffects
import com.eyther.lumbridge.features.feed.model.overview.FeedOverviewScreenViewState
import com.eyther.lumbridge.model.news.RssFeedUi
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface IFeedOverviewScreenViewModel {
    val viewState: StateFlow<FeedOverviewScreenViewState>
    val viewEffects: SharedFlow<FeedOverviewScreenViewEffects>

    /**
     * Attempts to refresh the news feed.
     */
    fun refresh()

    /**
     * Selects a feed to display.
     * @param rssFeedUi The feed to display.
     * @param index The index of the selected feed to scroll to.
     * @see RssFeedUi
     */
    fun selectFeed(rssFeedUi: RssFeedUi? = null, index: Int = 0)
}
