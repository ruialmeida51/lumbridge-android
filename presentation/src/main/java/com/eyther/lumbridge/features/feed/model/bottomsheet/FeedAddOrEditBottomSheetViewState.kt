package com.eyther.lumbridge.features.feed.model.bottomsheet

sealed interface FeedAddOrEditBottomSheetViewState {
    data object Loading : FeedAddOrEditBottomSheetViewState
    data class Add(
        val enableSaveButton: Boolean = false
    ) : FeedAddOrEditBottomSheetViewState

    data class Edit(
        val enableSaveButton: Boolean = false
    ) : FeedAddOrEditBottomSheetViewState

    fun shouldShowDeleteButton() = this is Edit
}
