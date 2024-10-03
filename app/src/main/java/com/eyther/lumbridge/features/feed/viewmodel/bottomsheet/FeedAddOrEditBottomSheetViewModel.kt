package com.eyther.lumbridge.features.feed.viewmodel.bottomsheet

import android.util.Log
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
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = IFeedAddOrEditBottomSheetViewModel.Factory::class)
class FeedAddOrEditBottomSheetViewModel @AssistedInject constructor(
    private val feedAddBottomSheetInputHandler: FeedAddOrEditBottomSheetInputHandler,
    private val saveRssFeedUseCase: SaveRssFeedUseCase,
    private val deleteRssFeedUseCase: DeleteRssFeedUseCase,
    @Assisted("selectedFeed") private val selectedFeedUi: RssFeedUi?
) : ViewModel(),
    IFeedAddOrEditBottomSheetViewModel,
    IFeedAddOrEditBottomSheetInputHandler by feedAddBottomSheetInputHandler {

    companion object {
        const val TAG = "FeedAddOrEditBottomSheetViewModel"
    }

    override val viewState: MutableStateFlow<FeedAddOrEditBottomSheetViewState> =
        MutableStateFlow(FeedAddOrEditBottomSheetViewState.Loading)

    init {
        inputState
            .onEach { inputState ->
                viewState.update { _ ->
                    if (selectedFeedUi == null) {
                        FeedAddOrEditBottomSheetViewState.Add(
                            enableSaveButton = shouldEnableSaveButton(inputState)
                        )
                    } else {
                        FeedAddOrEditBottomSheetViewState.Edit(
                            enableSaveButton = shouldEnableSaveButton(inputState)
                        )
                    }
                }
            }
            .launchIn(viewModelScope)

        updateInput {
            it.copy(
                feedName = it.feedName.copy(text = selectedFeedUi?.label.orEmpty()),
                feedUrl = it.feedUrl.copy(text = selectedFeedUi?.url.orEmpty())
            )
        }
    }

    override fun onAddOrUpdateFeed(name: String, url: String) {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e(TAG, "💥 Error saving feed: $throwable")
        }

        viewModelScope.launch(exceptionHandler) {
            saveRssFeedUseCase(
                selectedFeedUi?.copy(
                    label = name,
                    url = url
                ) ?: RssFeedUi(
                    label = name,
                    url = url
                )
            )

            resetInput()
        }
    }

    override fun onDeleteFeed(name: String) {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e(TAG, "💥 Error deleting feed: $throwable")
        }

        viewModelScope.launch(exceptionHandler) {
            deleteRssFeedUseCase(
                checkNotNull(selectedFeedUi?.id) { "💥 Cannot delete a feed that does not exist." }
            )

            resetInput()
        }
    }

    private fun resetInput() = updateInput {
        it.copy(feedName = TextInputState(), feedUrl = TextInputState())
    }
}
