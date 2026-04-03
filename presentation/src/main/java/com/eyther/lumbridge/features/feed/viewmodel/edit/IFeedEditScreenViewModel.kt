package com.eyther.lumbridge.features.feed.viewmodel.edit

import com.eyther.lumbridge.features.feed.model.edit.FeedEditScreenViewEffects
import com.eyther.lumbridge.features.feed.model.edit.FeedEditScreenViewState
import com.eyther.lumbridge.model.news.RssFeedUi
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface IFeedEditScreenViewModel {
    val viewState: StateFlow<FeedEditScreenViewState>
    val viewEffects: SharedFlow<FeedEditScreenViewEffects>

    /**
     * Attempts to signal we want to edit the feed.
     *
     * @param selectedFeed The feed to edit.
     */
    fun onEditFeedClick(selectedFeed: RssFeedUi)

    /**
     * Attempts to signal we want to add a feed.
     */
    fun onAddFeedClick()
}
