package com.eyther.lumbridge.features.tools

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.eyther.lumbridge.features.tools.ctc.screens.CostToCompanyScreen
import com.eyther.lumbridge.features.tools.currencyconverter.screens.CurrencyConverterScreen
import com.eyther.lumbridge.features.tools.mortgage.screens.MortgageScreen
import com.eyther.lumbridge.features.tools.netsalary.screens.NetSalaryScreen
import com.eyther.lumbridge.features.tools.overview.navigation.ToolsNavigationItem
import com.eyther.lumbridge.features.tools.overview.screens.ToolsOverviewScreen
import com.eyther.lumbridge.features.tools.savings.screens.SavingsScreen

@Composable
fun ToolsNavHost(
    navController: NavHostController
) {
    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = ToolsNavigationItem.Overview.route
    ) {
        composable(route = ToolsNavigationItem.Overview.route) {
            ToolsOverviewScreen(
                navController,
                ToolsNavigationItem.Overview.label
            )
        }

        composable(
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start)
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End)
            },
            route = ToolsNavigationItem.NetSalary.route
        ) {
            NetSalaryScreen(
                navController = navController,
                label = ToolsNavigationItem.NetSalary.label
            )
        }

        composable(
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start)
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End)
            }, route = ToolsNavigationItem.CostToCompany.route
        ) {
            CostToCompanyScreen(
                navController = navController,
                label = ToolsNavigationItem.CostToCompany.label
            )
        }

        composable(
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start)
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End)
            }, route = ToolsNavigationItem.Savings.route
        ) {
            SavingsScreen(
                navController = navController,
                label = ToolsNavigationItem.Savings.label
            )
        }

        composable(enterTransition = {
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start)
        },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End)
            }, route = ToolsNavigationItem.Mortgage.route
        ) {
            MortgageScreen(
                navController = navController,
                label = ToolsNavigationItem.Mortgage.label
            )
        }

        composable(enterTransition = {
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start)
        },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End)
            }, route = ToolsNavigationItem.CurrencyConverter.route
        ) {
            CurrencyConverterScreen(
                navController = navController,
                label = ToolsNavigationItem.CurrencyConverter.label
            )
        }
    }
}
