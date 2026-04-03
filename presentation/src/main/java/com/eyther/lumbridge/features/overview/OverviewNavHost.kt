package com.eyther.lumbridge.features.overview

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.eyther.lumbridge.features.editfinancialprofile.screens.EditFinancialProfileScreen
import com.eyther.lumbridge.features.editloan.screens.EditLoanScreen
import com.eyther.lumbridge.features.overview.breakdown.screens.BreakdownScreen
import com.eyther.lumbridge.features.overview.financialprofiledetails.screens.DetailsFinancialProfileScreen
import com.eyther.lumbridge.features.overview.loandetails.screens.LoanDetailsScreen
import com.eyther.lumbridge.features.overview.navigation.OverviewNavigationItem
import com.eyther.lumbridge.features.overview.navigation.OverviewNavigationItem.Loan.Companion.ARG_LOAN_ID

@Composable
fun OverviewNavHost(
    navController: NavHostController
) {
    NavHost(
        modifier = Modifier
            .fillMaxSize(),
        navController = navController,
        startDestination = OverviewNavigationItem.Breakdown.route
    ) {
        composable(
            route = OverviewNavigationItem.Breakdown.route
        ) {
            BreakdownScreen(
                navController = navController,
                label = OverviewNavigationItem.Breakdown.label
            )
        }

        navigation(
            startDestination = OverviewNavigationItem.FinancialProfile.Details.route,
            route = OverviewNavigationItem.FinancialProfile.HOST_ROUTE
        ) {
            composable(
                enterTransition = {
                    slideInHorizontally { it }
                },
                exitTransition = {
                    slideOutHorizontally { -it }
                },
                popEnterTransition = {
                    slideInHorizontally { -it }
                },
                popExitTransition = {
                    slideOutHorizontally { it }
                },
                route = OverviewNavigationItem.FinancialProfile.Details.route
            ) {
                DetailsFinancialProfileScreen(
                    navController = navController,
                    label = OverviewNavigationItem.FinancialProfile.Details.label
                )
            }

            composable(
                enterTransition = {
                    slideInHorizontally { it }
                },
                exitTransition = {
                    slideOutHorizontally { -it }
                },
                popExitTransition = {
                    slideOutHorizontally { it }
                },
                popEnterTransition = {
                    slideInHorizontally { -it }
                },
                route = OverviewNavigationItem.FinancialProfile.Edit.route
            ) {
                EditFinancialProfileScreen(
                    navController = navController,
                    label = OverviewNavigationItem.FinancialProfile.Edit.label
                )
            }
        }

        navigation(
            startDestination = OverviewNavigationItem.Loan.Details.route,
            route = OverviewNavigationItem.Loan.HOST_ROUTE
        ) {
            composable(
                enterTransition = {
                    slideInHorizontally { it }
                },
                exitTransition = {
                    slideOutHorizontally { -it }
                },
                popEnterTransition = {
                    slideInHorizontally { -it }
                },
                popExitTransition = {
                    slideOutHorizontally { it }
                },
                route = OverviewNavigationItem.Loan.Details.route,
                arguments = listOf(
                    navArgument(ARG_LOAN_ID) {
                        type = NavType.LongType
                        nullable = false
                        defaultValue = -1L
                    }
                )
            ) {
                LoanDetailsScreen(
                    navController = navController,
                    label = OverviewNavigationItem.Loan.Details.label
                )
            }

            composable(
                enterTransition = {
                    slideInHorizontally { it }
                },
                exitTransition = {
                    slideOutHorizontally { -it }
                },
                popExitTransition = {
                    slideOutHorizontally { it }
                },
                popEnterTransition = {
                    slideInHorizontally { -it }
                },
                route = OverviewNavigationItem.Loan.Edit.route,
                arguments = listOf(
                    navArgument(ARG_LOAN_ID) {
                        type = NavType.LongType
                        nullable = false
                        defaultValue = -1L
                    }
                )
            ) {
                EditLoanScreen(
                    navController = navController,
                    label = OverviewNavigationItem.Loan.Edit.label
                )
            }
        }
    }
}
