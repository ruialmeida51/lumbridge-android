package com.eyther.lumbridge.launcher.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.eyther.lumbridge.ui.common.composables.components.bottomNavigation.LumbridgeBottomNavigationBar
import com.eyther.lumbridge.ui.navigation.bottomNavigation.LumbridgeNavigationHost
import com.eyther.lumbridge.ui.theme.LumbridgeTheme

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = { LumbridgeBottomNavigationBar(navController) },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            LumbridgeNavigationHost(modifier, navController)
        }
    }
}

@Composable
@Preview
private fun MainScreenPreview() {
    LumbridgeTheme {
        MainScreen()
    }
}
