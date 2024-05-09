package com.eyther.lumbridge.features.feed.viewmodel

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

    override val viewState: MutableStateFlow<FeedScreenViewState> =
        MutableStateFlow(FeedScreenViewState.Loading)

    init {
        fetchAvailableFeeds()
    }

    private fun fetchAvailableFeeds() {
        selectFeed()
    }

    override fun selectFeed(rssFeedUi: RssFeedUi?) {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            viewState.update { FeedScreenViewState.Empty }
        }

        viewState.update { FeedScreenViewState.Loading }

        viewModelScope.launch(coroutineExceptionHandler) {
            val availableFeeds = getAvailableFeeds()
            val selectedFeed = rssFeedUi ?: availableFeeds.first()
            val newsFeed = getNewsFeed(selectedFeed)

            if (newsFeed.isEmpty()) {
                viewState.update { FeedScreenViewState.Empty }
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
