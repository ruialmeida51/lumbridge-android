@file:OptIn(ExperimentalMaterial3Api::class)

package com.eyther.lumbridge.launcher.screens

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
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
        modifier = modifier
            .fillMaxSize(),
        bottomBar = { LumbridgeBottomNavigationBar(navController) },
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.exclude(NavigationBarDefaults.windowInsets)
    ) { paddingValues ->
        LumbridgeNavigationHost(
            modifier = modifier
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues),
            navController = navController
        )
    }
}

@Composable
@Preview
private fun MainScreenPreview() {
    LumbridgeTheme {
        MainScreen()
    }
}
