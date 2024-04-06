package com.eyther.lumbridge.ui.navigation.bottomNavigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.eyther.lumbridge.features.feed.screens.FeedScreen
import com.eyther.lumbridge.features.guides.screens.GuidesScreen
import com.eyther.lumbridge.features.play.screens.PlayScreen
import com.eyther.lumbridge.features.profile.screens.ProfileScreen
import com.eyther.lumbridge.features.tools.screens.ToolsScreen

@Composable
fun LumbridgeNavigationHost(navController: NavHostController) {
	NavHost(navController = navController, startDestination = LumbridgeNavigationItem.Feed.route) {
		composable(LumbridgeNavigationItem.Feed.route) { FeedScreen() }
		composable(LumbridgeNavigationItem.Guides.route) { GuidesScreen() }
		composable(LumbridgeNavigationItem.Play.route) { PlayScreen() }
		composable(LumbridgeNavigationItem.Tools.route) { ToolsScreen() }
		composable(LumbridgeNavigationItem.Profile.route) { ProfileScreen() }
	}
}