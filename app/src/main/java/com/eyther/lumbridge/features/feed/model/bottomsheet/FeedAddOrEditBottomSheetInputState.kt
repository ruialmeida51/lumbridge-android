package com.eyther.lumbridge.features.feed.model.bottomsheet

import com.eyther.lumbridge.ui.common.composables.model.input.TextInputState

data class FeedAddOrEditBottomSheetInputState(
    val feedName: TextInputState = TextInputState(),
    val feedUrl: TextInputState = TextInputState()
)
