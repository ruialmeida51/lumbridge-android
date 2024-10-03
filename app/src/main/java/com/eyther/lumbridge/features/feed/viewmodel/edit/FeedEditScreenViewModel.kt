package com.eyther.lumbridge.features.feed.viewmodel.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyther.lumbridge.features.feed.model.edit.FeedEditScreenViewEffects
import com.eyther.lumbridge.features.feed.model.edit.FeedEditScreenViewState
import com.eyther.lumbridge.model.news.RssFeedUi
import com.eyther.lumbridge.usecase.news.GetAvailableFeedsFlowUseCase
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
class FeedEditScreenViewModel @Inject constructor(
    private val getAvailableFeedsFlowUseCase: GetAvailableFeedsFlowUseCase
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

            availableFeedsFlow
                .stateIn(this, SharingStarted.Lazily, availableFeedsFlow.firstOrNull() ?: emptyList())
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

    override fun onEditFeedClick(selectedFeed: RssFeedUi) {
        viewModelScope.launch {
            viewEffects.emit(FeedEditScreenViewEffects.EditFeed(selectedFeed))
        }
    }

    override fun onAddFeedClick() {
        viewModelScope.launch {
            viewEffects.emit(FeedEditScreenViewEffects.AddFeed)
        }
    }
}
