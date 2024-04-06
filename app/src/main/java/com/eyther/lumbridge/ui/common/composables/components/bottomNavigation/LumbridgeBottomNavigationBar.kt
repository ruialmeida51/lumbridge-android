package com.eyther.lumbridge.ui.common.composables.components.bottomNavigation

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.eyther.lumbridge.ui.navigation.bottomNavigation.LumbridgeNavigationItem

@Composable
fun LumbridgeBottomNavigationBar(navController: NavController) {
	var selectedItemIndex by remember { mutableIntStateOf(0) }
	NavigationBar(tonalElevation = 0.dp) {
		LumbridgeNavigationItem.items().forEachIndexed { index, navigationItem ->
			val selected = index == selectedItemIndex
			NavigationBarItem(
				selected = selected,
				label = {
					Text(navigationItem.label)
				},
				icon = {
					Icon(
						painter = painterResource(navigationItem.icon),
						contentDescription = navigationItem.label,
						modifier = Modifier.size(24.dp)
					)
				},
				onClick = {
					if (selectedItemIndex != index) {
						selectedItemIndex = index
						
						navController.navigate(navigationItem.route) {
							popUpTo(navController.graph.startDestinationId)
							launchSingleTop = true
						}
					}
				}
			)
		}
	}
}