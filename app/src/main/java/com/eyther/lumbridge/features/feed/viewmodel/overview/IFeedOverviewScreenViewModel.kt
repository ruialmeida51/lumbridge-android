package com.eyther.lumbridge.features.feed.viewmodel.overview

import androidx.navigation.NavHostController
import com.eyther.lumbridge.features.expenses.navigation.ExpensesNavigationItem
import com.eyther.lumbridge.features.feed.model.overview.FeedOverviewScreenViewEffects
import com.eyther.lumbridge.features.feed.model.overview.FeedOverviewScreenViewState
import com.eyther.lumbridge.model.news.RssFeedUi
import com.eyther.lumbridge.ui.navigation.NavigationItem
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface IFeedOverviewScreenViewModel {
    val viewState: StateFlow<FeedOverviewScreenViewState>
    val viewEffects: SharedFlow<FeedOverviewScreenViewEffects>

    /**
     * Attempts to refresh the news feed.
     */
    fun refresh()

    /**
     * Selects a feed to display.
     * @param rssFeedUi The feed to display.
     * @param index The index of the selected feed to scroll to.
     * @see RssFeedUi
     */
    fun selectFeed(rssFeedUi: RssFeedUi? = null, index: Int = 0)

    /**
     * Navigate to the selected expenses screen, based on the selected navigation item.
     *
     * @param navigationItem The selected navigation item
     * @param navController The navigation controller
     *
     * @see ExpensesNavigationItem
     */
    fun navigate(navigationItem: NavigationItem, navController: NavHostController)
}
