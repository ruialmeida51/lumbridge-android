package com.eyther.lumbridge.features.feed.viewmodel.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyther.lumbridge.features.feed.model.overview.FeedOverviewScreenViewEffects
import com.eyther.lumbridge.features.feed.model.overview.FeedOverviewScreenViewState
import com.eyther.lumbridge.features.feed.model.overview.FeedOverviewScreenViewState.Content
import com.eyther.lumbridge.features.feed.model.overview.FeedOverviewScreenViewState.Empty
import com.eyther.lumbridge.features.feed.model.overview.FeedOverviewScreenViewState.Error
import com.eyther.lumbridge.features.feed.model.overview.FeedOverviewScreenViewState.Loading
import com.eyther.lumbridge.model.news.RssFeedUi
import com.eyther.lumbridge.usecase.news.GetAvailableFeedsFlowUseCase
import com.eyther.lumbridge.usecase.news.GetNewsFeedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedOverviewScreenViewModel @Inject constructor(
    private val getNewsFeed: GetNewsFeedUseCase,
    private val getAvailableFeedsFlowUseCase: GetAvailableFeedsFlowUseCase
) : ViewModel(),
    IFeedOverviewScreenViewModel {

    private var cachedAvailableFeeds = emptyList<RssFeedUi>()

    override val viewState: MutableStateFlow<FeedOverviewScreenViewState> =
        MutableStateFlow(Loading())

    override val viewEffects: MutableSharedFlow<FeedOverviewScreenViewEffects> =
        MutableSharedFlow()

    init {
        observeFeeds()
    }

    private fun observeFeeds() {
        viewModelScope.launch {
            val availableFeedsFlow = getAvailableFeedsFlowUseCase()
            val firstFeedResult = availableFeedsFlow.firstOrNull() ?: emptyList()

            availableFeedsFlow
                .stateIn(this, SharingStarted.Lazily, firstFeedResult)
                .collect { availableFeeds ->
                    if (availableFeeds.isEmpty()) {
                        viewState.update { Empty }
                        return@collect
                    }

                    cachedAvailableFeeds = availableFeeds
                    viewState.update { Loading(availableFeeds, firstFeedResult.firstOrNull()) }

                    // Select the first feed by default.
                    val selectedFeed = availableFeeds.first()

                    updateNewsFeed(selectedFeed)

                    // Scroll to the first element of the list.
                    viewEffects.emit(FeedOverviewScreenViewEffects.ScrollToIndex(0))
                }

        }
    }

    override fun selectFeed(rssFeedUi: RssFeedUi?, index: Int) {
        viewModelScope.launch {
            viewState.update {
                Loading(it.availableFeeds, it.selectedFeed)
            }

            // If everything goes well, the selected feed will be the one passed in. Otherwise, it will be the first available feed.
            val selectedFeed = rssFeedUi ?: cachedAvailableFeeds.first()

            updateNewsFeed(selectedFeed)

            viewEffects.emit(FeedOverviewScreenViewEffects.ScrollToIndex(index))
        }
    }

    override fun refresh() {
        viewState.update { Loading(it.availableFeeds, it.selectedFeed) }
        observeFeeds()
    }

    /**
     * Fetches the news feed for the selected feed, and updates the view state accordingly.
     *
     * @param selectedFeed The selected feed to fetch the news feed for.
     */
    private suspend fun updateNewsFeed(selectedFeed: RssFeedUi) {
        val newsFeed = kotlin.runCatching { getNewsFeed(selectedFeed) }.getOrNull()

        viewState.update {
            if (newsFeed.isNullOrEmpty()) {
                Error(
                    availableFeeds = cachedAvailableFeeds,
                    selectedFeed = selectedFeed
                )
            } else {
                Content(
                    feedItems = newsFeed,
                    availableFeeds = cachedAvailableFeeds,
                    selectedFeed = selectedFeed
                )
            }
        }
    }
}
