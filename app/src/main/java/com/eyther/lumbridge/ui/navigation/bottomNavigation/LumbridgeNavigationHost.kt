package com.eyther.lumbridge.ui.navigation.bottomNavigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.eyther.lumbridge.features.feed.screens.FeedScreen
import com.eyther.lumbridge.features.overview.OverviewScreen
import com.eyther.lumbridge.features.profile.ProfileScreen
import com.eyther.lumbridge.features.tools.ToolsScreen

@Composable
fun LumbridgeNavigationHost(
    modifier: Modifier,
    navController: NavHostController
) {
    NavHost(
        modifier = modifier.fillMaxSize(),
        navController = navController,
        startDestination = LumbridgeNavigationItem.Feed.route,
    ) {
        composable(route = LumbridgeNavigationItem.Feed.route) { FeedScreen(LumbridgeNavigationItem.Feed.label) }
        composable(route = LumbridgeNavigationItem.Tools.route) { ToolsScreen() }
        composable(route = LumbridgeNavigationItem.Overview.route) { OverviewScreen() }
        composable(route = LumbridgeNavigationItem.Profile.route) { ProfileScreen() }
    }
}
