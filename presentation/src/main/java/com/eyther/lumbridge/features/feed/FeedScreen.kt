package com.eyther.lumbridge.features.feed

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun FeedScreen(
    navController: NavHostController = rememberNavController()
) {
    FeedNavHost(navController)
}
