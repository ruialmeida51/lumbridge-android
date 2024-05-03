package com.eyther.lumbridge.features.feed.viewmodel

import com.eyther.lumbridge.features.feed.model.FeedScreenViewState
import kotlinx.coroutines.flow.StateFlow

interface FeedScreenViewModelInterface {
    val viewState: StateFlow<FeedScreenViewState>

    /**
     * Attempts to refresh the news feed.
     */
    fun refresh()
}
