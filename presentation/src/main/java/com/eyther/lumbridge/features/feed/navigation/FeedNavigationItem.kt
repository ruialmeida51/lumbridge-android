package com.eyther.lumbridge.features.feed.navigation

import androidx.annotation.StringRes
import com.eyther.lumbridge.R
import com.eyther.lumbridge.ui.navigation.NavigationItem
import kotlinx.serialization.Serializable

@Serializable
sealed class FeedNavigationItem(
    override val route: String,
    @StringRes override val label: Int
) : NavigationItem() {

    @Serializable
    data object FeedOverview : FeedNavigationItem(
        route = "feed_overview",
        label = R.string.feed
    )

    @Serializable
    data object FeedEdit : FeedNavigationItem(
        route = "feed_edit",
        label = R.string.feed_edit
    )
}
