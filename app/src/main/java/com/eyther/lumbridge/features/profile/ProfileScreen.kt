package com.eyther.lumbridge.features.profile

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun ProfileScreen(navController: NavHostController = rememberNavController()) {
    ProfileNavHost(navController = navController)
}
