package com.eyther.lumbridge.features.expenses

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.eyther.lumbridge.features.expenses.navigation.ExpensesNavigationItem
import com.eyther.lumbridge.features.expenses.screens.ExpensesOverview

@Composable
fun ExpensesNavHost(
    navController: NavHostController
) {
    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = ExpensesNavigationItem.ExpensesOverview.route
    ) {
        composable(
            route = ExpensesNavigationItem.ExpensesOverview.route
        ) {
            ExpensesOverview(
                navController = navController,
                label = ExpensesNavigationItem.ExpensesOverview.label
            )
        }
    }
}
