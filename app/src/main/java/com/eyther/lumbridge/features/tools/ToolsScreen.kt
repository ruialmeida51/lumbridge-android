package com.eyther.lumbridge.features.tools

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun ToolsScreen() {
    val nestedNavController = rememberNavController()
    ToolsNavHost(nestedNavController)
}
