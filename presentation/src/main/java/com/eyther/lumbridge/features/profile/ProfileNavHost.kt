package com.eyther.lumbridge.features.profile

import androidx.compose.animation.AnimatedContentTransitionScope
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
import com.eyther.lumbridge.features.overview.navigation.OverviewNavigationItem.Loan
import com.eyther.lumbridge.features.profile.editloans.screens.EditLoansListScreen
import com.eyther.lumbridge.features.profile.editprofile.screens.EditProfileScreen
import com.eyther.lumbridge.features.profile.navigation.ProfileNavigationItem
import com.eyther.lumbridge.features.profile.overview.screens.ProfileOverviewScreen
import com.eyther.lumbridge.features.profile.settings.screens.ProfileSettingsScreen

@Composable
fun ProfileNavHost(
    navController: NavHostController
) {
    NavHost(
        modifier = Modifier
            .fillMaxSize(),
        navController = navController,
        startDestination = ProfileNavigationItem.ProfileOverview.route
    ) {
        composable(
            route = ProfileNavigationItem.ProfileOverview.route
        ) {
            ProfileOverviewScreen(
                navController = navController,
                label = ProfileNavigationItem.ProfileOverview.label
            )
        }

        composable(
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start)
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End)
            },
            route = ProfileNavigationItem.EditProfile.route
        ) {
            EditProfileScreen(
                navController = navController,
                label = ProfileNavigationItem.EditProfile.label
            )
        }

        composable(
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start)
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End)
            },
            route = ProfileNavigationItem.EditFinancialProfile.route
        ) {
            EditFinancialProfileScreen(
                navController = navController,
                label = ProfileNavigationItem.EditFinancialProfile.label
            )
        }

        navigation(
            startDestination = ProfileNavigationItem.Loans.List.route,
            route = ProfileNavigationItem.Loans.HOST_ROUTE
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
                route = ProfileNavigationItem.Loans.List.route
            ) {
                EditLoansListScreen(
                    navController = navController,
                    label =  ProfileNavigationItem.Loans.List.label
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
                route = ProfileNavigationItem.Loans.Edit.route,
                arguments = listOf(
                    navArgument(Loan.ARG_LOAN_ID) {
                        type = NavType.LongType
                        nullable = false
                        defaultValue = -1L
                    }
                )
            ) {
                EditLoanScreen(
                    navController = navController,
                    label = ProfileNavigationItem.Loans.Edit.label
                )
            }
        }

        composable(
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start)
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End)
            },
            route = ProfileNavigationItem.Settings.route
        ) {
            ProfileSettingsScreen(
                label = ProfileNavigationItem.Settings.label,
                navController = navController
            )
        }
    }
}
