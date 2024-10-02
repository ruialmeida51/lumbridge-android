package com.eyther.lumbridge.features.feed

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.eyther.lumbridge.features.feed.navigation.FeedNavigationItem
import com.eyther.lumbridge.features.feed.screens.FeedEditScreen
import com.eyther.lumbridge.features.feed.screens.FeedOverviewScreen

@Composable
fun FeedNavHost(
    navController: NavHostController
) {
    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = FeedNavigationItem.FeedOverview.route
    ) {
        composable(
            route = FeedNavigationItem.FeedOverview.route
        ) {
            FeedOverviewScreen(
                navController = navController,
                label = FeedNavigationItem.FeedOverview.label
            )
        }

        composable(
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start)
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End)
            },
            route = FeedNavigationItem.FeedEdit.route
        ) {
            FeedEditScreen(
                navController = navController,
                label = FeedNavigationItem.FeedEdit.label
            )
        }
    }
}
