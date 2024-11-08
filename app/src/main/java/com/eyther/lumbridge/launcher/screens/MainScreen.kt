@file:OptIn(ExperimentalMaterial3Api::class)

package com.eyther.lumbridge.launcher.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.eyther.lumbridge.ui.common.composables.components.bottomNavigation.LumbridgeBottomNavigationBar
import com.eyther.lumbridge.ui.navigation.bottomNavigation.LumbridgeNavigationHost

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .systemBarsPadding(),
        bottomBar = {
            AnimatedVisibility(
                visible = !WindowInsets.isImeVisible,
                enter = slideInVertically(animationSpec = tween(100)) { it },
                exit = fadeOut(snap())
            ) {
                LumbridgeBottomNavigationBar(navController)
            }
        }
    ) { paddingValues ->
        LumbridgeNavigationHost(
            modifier = Modifier.padding(paddingValues),
            navController = navController
        )
    }
}
