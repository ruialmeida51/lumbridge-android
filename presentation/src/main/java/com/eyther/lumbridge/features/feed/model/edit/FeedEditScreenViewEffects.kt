package com.eyther.lumbridge.features.feed.model.edit

import com.eyther.lumbridge.model.news.RssFeedUi

sealed interface FeedEditScreenViewEffects {
    data object AddFeed : FeedEditScreenViewEffects

    data class EditFeed(
        val selectedFeed: RssFeedUi
    ) : FeedEditScreenViewEffects
}
