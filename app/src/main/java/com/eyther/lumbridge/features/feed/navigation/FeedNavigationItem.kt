package com.eyther.lumbridge.features.feed.navigation

import androidx.annotation.StringRes
import com.eyther.lumbridge.R
import com.eyther.lumbridge.ui.navigation.NavigationItem

sealed class FeedNavigationItem(
    route: String,
    @StringRes label: Int
) : NavigationItem(route, label) {
    data object FeedOverview : FeedNavigationItem(
        route = "feed_overview",
        label = R.string.feed
    )

    data object FeedEdit : FeedNavigationItem(
        route = "feed_edit",
        label = R.string.feed_edit
    )
}
