package com.eyther.lumbridge.features.feed.viewmodel.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyther.lumbridge.features.feed.model.edit.FeedEditScreenViewEffects
import com.eyther.lumbridge.features.feed.model.edit.FeedEditScreenViewState
import com.eyther.lumbridge.usecase.news.GetAvailableFeedsFlowUseCase
import com.eyther.lumbridge.usecase.news.GetAvailableFeedsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedEditScreenViewModel @Inject constructor(
    private val getAvailableFeedsFlowUseCase: GetAvailableFeedsFlowUseCase,
    private val getAvailableFeedsUseCase: GetAvailableFeedsUseCase
) : ViewModel(),
    IFeedEditScreenViewModel {

    override val viewState: MutableStateFlow<FeedEditScreenViewState> =
        MutableStateFlow(FeedEditScreenViewState.Loading)

    override val viewEffects: MutableSharedFlow<FeedEditScreenViewEffects> =
        MutableSharedFlow()

    init {
        loadFeeds()
    }

    private fun loadFeeds() {
        viewModelScope.launch {
            val availableFeedsFlow = getAvailableFeedsFlowUseCase()
            val firstFeedResult = getAvailableFeedsUseCase()

            availableFeedsFlow
                .stateIn(this, SharingStarted.Lazily, firstFeedResult)
                .collect { feeds ->
                    viewState.update {
                        if (feeds.isEmpty()) {
                            FeedEditScreenViewState.NoFeeds
                        } else {
                            FeedEditScreenViewState.HasFeeds(feeds)
                        }
                    }
                }
        }
    }

    override fun onEditFeedClick(feedName: String, feedUrl: String) {
        viewModelScope.launch {
            viewEffects.emit(FeedEditScreenViewEffects.EditFeed(feedName, feedUrl))
        }
    }

    override fun onAddFeedClick() {
        viewModelScope.launch {
            viewEffects.emit(FeedEditScreenViewEffects.AddFeed)
        }
    }
}
