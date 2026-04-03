package com.eyther.lumbridge.features.tools

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController

@Composable
fun ToolsScreen() {
    ToolsNavHost(
        navController = rememberNavController()
    )
}
