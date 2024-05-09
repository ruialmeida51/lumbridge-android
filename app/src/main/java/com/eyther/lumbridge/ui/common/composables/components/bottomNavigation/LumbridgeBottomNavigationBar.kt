package com.eyther.lumbridge.ui.common.composables.components.bottomNavigation

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.eyther.lumbridge.ui.navigation.bottomNavigation.LumbridgeNavigationItem

@Composable
fun LumbridgeBottomNavigationBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentDestinationHierarchy = currentDestination?.hierarchy.orEmpty()

    NavigationBar(tonalElevation = 0.dp) {
        LumbridgeNavigationItem.items().forEach { navigationItem ->
            NavigationBarItem(
                selected = currentDestinationHierarchy.any { it.route == navigationItem.route },
                label = {
                    Text(stringResource(id = navigationItem.label))
                },
                icon = {
                    Icon(
                        painter = painterResource(navigationItem.icon),
                        contentDescription = stringResource(id = navigationItem.label),
                        modifier = Modifier.size(24.dp)
                    )
                },
                onClick = {
                    navController.navigate(navigationItem.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }

                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}