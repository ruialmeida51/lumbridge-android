package com.eyther.lumbridge.ui.common.composables.components.topAppBar

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.eyther.lumbridge.R
import com.eyther.lumbridge.ui.navigation.bottomNavigation.LumbridgeNavigationItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LumbridgeTopAppBar(
    navController: NavHostController,
    showBackButton: Boolean = false,
    text: String = stringResource(id = R.string.app_name)
) {
    TopAppBar(
        navigationIcon = {
            if (showBackButton) {
                Icon(
                    modifier = Modifier.clickable { navController.navigateUp() },
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        title = { Text(text = text) }
    )
}