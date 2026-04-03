package com.eyther.lumbridge.features.overview

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun OverviewScreen(
    navController: NavHostController = rememberNavController()
) {
    OverviewNavHost(navController = navController)
}
