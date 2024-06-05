package com.eyther.lumbridge.features.feed.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyther.lumbridge.features.feed.model.FeedScreenViewState
import com.eyther.lumbridge.model.news.RssFeedUi
import com.eyther.lumbridge.usecase.news.GetAvailableFeeds
import com.eyther.lumbridge.usecase.news.GetNewsFeed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedScreenViewModel @Inject constructor(
    private val getNewsFeed: GetNewsFeed,
    private val getAvailableFeeds: GetAvailableFeeds
) : ViewModel(), IFeedScreenViewModel {

    private var cachedAvailableFeeds = emptyList<RssFeedUi>()
    private var lastUserSelectedRssFeed: RssFeedUi? = null

    override val viewState: MutableStateFlow<FeedScreenViewState> =
        MutableStateFlow(FeedScreenViewState.Loading)

    init {
        fetchAvailableFeeds()
    }

    private fun fetchAvailableFeeds() {
        selectFeed()
    }

    override fun selectFeed(userSelectedRssFeed: RssFeedUi?) {
        if (userSelectedRssFeed != null) {
            lastUserSelectedRssFeed = userSelectedRssFeed
        }

        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e(this::class.java.simpleName, "💥 Failed to fetch news feed", throwable)

            viewState.update {
                FeedScreenViewState.Empty(
                    availableFeeds = cachedAvailableFeeds,
                    selectedFeed = lastUserSelectedRssFeed
                )
            }
        }

        viewModelScope.launch(coroutineExceptionHandler) {
            viewState.update {
                FeedScreenViewState.Loading
            }

            val availableFeeds = getAvailableFeeds()

            if (availableFeeds.isNotEmpty()) {
                cachedAvailableFeeds = availableFeeds
            }

            // If the user selected a feed, use that. Otherwise, use the last selected feed.
            // If there is neither a user selected feed nor a last selected feed,
            // use the first available feed.
            val selectedFeed = userSelectedRssFeed
                ?: lastUserSelectedRssFeed
                ?: availableFeeds.first()

            val newsFeed = getNewsFeed(selectedFeed)

            if (newsFeed.isEmpty()) {
                viewState.update {
                    FeedScreenViewState.Empty(
                        availableFeeds = availableFeeds,
                        selectedFeed = userSelectedRssFeed
                    )
                }
            } else {
                viewState.update {
                    FeedScreenViewState.Content(
                        feedItems = newsFeed,
                        availableFeeds = availableFeeds,
                        selectedFeed = selectedFeed
                    )
                }
            }
        }
    }

    override fun refresh() {
        viewState.update { FeedScreenViewState.Loading }
        fetchAvailableFeeds()
    }
}
