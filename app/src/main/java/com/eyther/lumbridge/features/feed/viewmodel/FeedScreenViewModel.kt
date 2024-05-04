package com.eyther.lumbridge.features.feed.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyther.lumbridge.features.feed.model.FeedScreenViewState
import com.eyther.lumbridge.usecase.news.GetNewsFeed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FeedScreenViewModel @Inject constructor(
    private val getNewsFeed: GetNewsFeed
) : ViewModel(), FeedScreenViewModelInterface {

    override val viewState: MutableStateFlow<FeedScreenViewState> =
        MutableStateFlow(FeedScreenViewState.Loading)

    init {
        fetchNewsFeed()
    }

    private fun fetchNewsFeed() {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            viewState.update { FeedScreenViewState.Empty }
        }

        viewModelScope.launch(coroutineExceptionHandler) {
            val newsFeed = getNewsFeed()

            if (newsFeed.isEmpty()) {
                viewState.update { FeedScreenViewState.Empty }
            } else {
                viewState.update { FeedScreenViewState.Content(newsFeed) }
            }
        }
    }

    override fun refresh() {
        viewState.update { FeedScreenViewState.Loading }
        fetchNewsFeed()
    }
}
