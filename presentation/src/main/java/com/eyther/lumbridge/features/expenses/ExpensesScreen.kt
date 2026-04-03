package com.eyther.lumbridge.features.expenses

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun ExpensesScreen(
    navController: NavHostController = rememberNavController()
) {
    ExpensesNavHost(navController)
}
