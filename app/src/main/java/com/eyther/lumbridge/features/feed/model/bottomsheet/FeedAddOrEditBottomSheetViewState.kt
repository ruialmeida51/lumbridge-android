package com.eyther.lumbridge.features.feed.model.bottomsheet

sealed interface FeedAddOrEditBottomSheetViewState {
    data object Loading : FeedAddOrEditBottomSheetViewState
    data object Add : FeedAddOrEditBottomSheetViewState
    data object Edit : FeedAddOrEditBottomSheetViewState

    fun shouldShowDeleteButton() = this is Edit
}
