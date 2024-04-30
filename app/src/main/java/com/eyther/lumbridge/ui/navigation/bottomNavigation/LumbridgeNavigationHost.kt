package com.eyther.lumbridge.ui.navigation.bottomNavigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.eyther.lumbridge.features.feed.screens.FeedScreen
import com.eyther.lumbridge.features.guides.screens.GuidesScreen
import com.eyther.lumbridge.features.play.screens.PlayScreen
import com.eyther.lumbridge.features.profile.screens.ProfileScreen
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
        composable(route = LumbridgeNavigationItem.Feed.route) { FeedScreen() }
        composable(route = LumbridgeNavigationItem.Guides.route) { GuidesScreen() }
        composable(route = LumbridgeNavigationItem.Play.route) { PlayScreen() }
        composable(route = LumbridgeNavigationItem.Tools.route) { ToolsScreen() }
        composable(route = LumbridgeNavigationItem.Profile.route) { ProfileScreen() }
    }
}
