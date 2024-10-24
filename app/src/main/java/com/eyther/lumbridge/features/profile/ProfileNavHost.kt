package com.eyther.lumbridge.features.profile

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.eyther.lumbridge.features.editfinancialprofile.screens.EditFinancialProfileScreen
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

        composable(
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start)
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End)
            },
            route = ProfileNavigationItem.EditLoans.route
        ) {
            EditLoansListScreen(
                navController = navController,
                label = ProfileNavigationItem.EditLoans.label
            )
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
