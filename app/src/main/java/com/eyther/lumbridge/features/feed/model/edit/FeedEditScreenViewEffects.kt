package com.eyther.lumbridge.features.feed.model.edit

sealed interface FeedEditScreenViewEffects {
    data object AddFeed : FeedEditScreenViewEffects

    data class EditFeed(
        val feedName: String,
        val feedUrl: String
    ) : FeedEditScreenViewEffects
}
