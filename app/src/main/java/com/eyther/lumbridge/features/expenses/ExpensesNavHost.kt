package com.eyther.lumbridge.features.expenses

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.eyther.lumbridge.features.editfinancialprofile.screens.EditFinancialProfileScreen
import com.eyther.lumbridge.features.editmortgageprofile.screens.EditMortgageProfileScreen
import com.eyther.lumbridge.features.expenses.navigation.ExpensesNavigationItem
import com.eyther.lumbridge.features.expenses.navigation.ExpensesNavigationItem.Companion.ARG_EXPENSE_ID
import com.eyther.lumbridge.features.expenses.screens.ExpensesAddScreen
import com.eyther.lumbridge.features.expenses.screens.ExpensesEditScreen
import com.eyther.lumbridge.features.expenses.screens.ExpensesOverviewScreen

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
            ExpensesOverviewScreen(
                navController = navController,
                label = ExpensesNavigationItem.ExpensesOverview.label
            )
        }

        composable(
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start)
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End)
            },
            route = ExpensesNavigationItem.EditFinancialProfile.route
        ) {
            EditFinancialProfileScreen(
                navController = navController,
                label = ExpensesNavigationItem.EditFinancialProfile.label
            )
        }

        composable(
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start)
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End)
            },
            route = ExpensesNavigationItem.AddExpense.route
        ) {
            EditMortgageProfileScreen(
                navController = navController,
                label = ExpensesNavigationItem.AddExpense.label
            )
        }

        composable(
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start)
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End)
            },
            route = ExpensesNavigationItem.EditExpense.route,
            arguments = listOf(
                navArgument(ARG_EXPENSE_ID) {
                    type = NavType.LongType
                    nullable = false
                    defaultValue = 0L
                }
            )
        ) {
            ExpensesEditScreen(
                navController = navController,
                label = ExpensesNavigationItem.EditExpense.label
            )
        }

        composable(
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start)
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End)
            },
            route = ExpensesNavigationItem.AddExpense.route
        ) {
            ExpensesAddScreen(
                navController = navController,
                label = ExpensesNavigationItem.AddExpense.label
            )
        }
    }
}
