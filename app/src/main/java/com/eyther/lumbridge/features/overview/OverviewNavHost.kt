package com.eyther.lumbridge.features.overview

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.eyther.lumbridge.features.editfinancialprofile.screens.EditFinancialProfileScreen
import com.eyther.lumbridge.features.editmortgageprofile.screens.EditMortgageProfileScreen
import com.eyther.lumbridge.features.overview.navigation.FinancialOverviewNavigationItem
import com.eyther.lumbridge.features.overview.screens.FinancialOverviewScreen

@Composable
fun OverviewNavHost(
    navController: NavHostController
) {
    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = FinancialOverviewNavigationItem.FinancialOverview.route
    ) {
        composable(
            route = FinancialOverviewNavigationItem.FinancialOverview.route
        ) {
            FinancialOverviewScreen(
                navController = navController,
                label = FinancialOverviewNavigationItem.FinancialOverview.label
            )
        }

        composable(
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start)
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End)
            },
            route = FinancialOverviewNavigationItem.EditFinancialProfile.route
        ) {
            EditFinancialProfileScreen(
                navController = navController,
                label = FinancialOverviewNavigationItem.EditFinancialProfile.label
            )
        }

        composable(
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start)
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End)
            },
            route = FinancialOverviewNavigationItem.EditMortgageProfile.route
        ) {
            EditMortgageProfileScreen(
                navController = navController, label =
                FinancialOverviewNavigationItem.EditMortgageProfile.label
            )
        }
    }
}
