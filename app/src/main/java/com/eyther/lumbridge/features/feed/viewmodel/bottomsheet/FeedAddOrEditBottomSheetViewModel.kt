package com.eyther.lumbridge.features.feed.viewmodel.bottomsheet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyther.lumbridge.features.feed.model.bottomsheet.FeedAddOrEditBottomSheetViewState
import com.eyther.lumbridge.features.feed.viewmodel.delegate.FeedAddOrEditBottomSheetInputHandler
import com.eyther.lumbridge.features.feed.viewmodel.delegate.IFeedAddOrEditBottomSheetInputHandler
import com.eyther.lumbridge.model.news.RssFeedUi
import com.eyther.lumbridge.ui.common.composables.model.input.TextInputState
import com.eyther.lumbridge.usecase.news.DeleteRssFeedUseCase
import com.eyther.lumbridge.usecase.news.SaveRssFeedUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = IFeedAddOrEditBottomSheetViewModel.Factory::class)
class FeedAddOrEditBottomSheetViewModel @AssistedInject constructor(
    private val feedAddBottomSheetInputHandler: FeedAddOrEditBottomSheetInputHandler,
    private val saveRssFeedUseCase: SaveRssFeedUseCase,
    private val deleteRssFeedUseCase: DeleteRssFeedUseCase,
    @Assisted("feedName") private val feedName: String,
    @Assisted("feedUrl") private val feedUrl: String
) : ViewModel(),
    IFeedAddOrEditBottomSheetViewModel,
    IFeedAddOrEditBottomSheetInputHandler by feedAddBottomSheetInputHandler {

    override val viewState: MutableStateFlow<FeedAddOrEditBottomSheetViewState> =
        MutableStateFlow(FeedAddOrEditBottomSheetViewState.Loading)

    init {
        updateInput {
            it.copy(
                feedName = it.feedName.copy(text = feedName),
                feedUrl = it.feedUrl.copy(text = feedUrl)
            )
        }

        viewState.update {
            if (feedName.isEmpty() && feedUrl.isEmpty()) {
                FeedAddOrEditBottomSheetViewState.Add
            } else {
                FeedAddOrEditBottomSheetViewState.Edit
            }
        }
    }

    override fun onAddOrUpdateFeed(name: String, url: String) {
        viewModelScope.launch {
            saveRssFeedUseCase(RssFeedUi(name, url))
            resetInput()
        }
    }

    override fun onDeleteFeed(name: String) {
        viewModelScope.launch {
            deleteRssFeedUseCase(RssFeedUi(name, ""))
            resetInput()
        }
    }

    private fun resetInput() = updateInput {
        it.copy(feedName = TextInputState(), feedUrl = TextInputState())
    }
}
