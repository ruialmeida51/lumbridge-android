package com.eyther.lumbridge.features.tools

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
import com.eyther.lumbridge.extensions.platform.sharedViewModel
import com.eyther.lumbridge.features.tools.currencyconverter.screens.CurrencyConverterScreen
import com.eyther.lumbridge.features.tools.groceries.screens.GroceriesListDetailsScreen
import com.eyther.lumbridge.features.tools.groceries.screens.GroceriesListsScreen
import com.eyther.lumbridge.features.tools.netsalary.arguments.NetSalaryScreenArgumentsCacheViewModel
import com.eyther.lumbridge.features.tools.netsalary.screens.NetSalaryInputScreen
import com.eyther.lumbridge.features.tools.netsalary.screens.NetSalaryResultScreen
import com.eyther.lumbridge.features.tools.notes.screens.NoteDetailsScreen
import com.eyther.lumbridge.features.tools.notes.screens.NotesListScreen
import com.eyther.lumbridge.features.tools.overview.navigation.ToolsNavigationItem
import com.eyther.lumbridge.features.tools.overview.navigation.ToolsNavigationItem.Groceries.Companion.ARG_GROCERIES_LIST_ID
import com.eyther.lumbridge.features.tools.overview.navigation.ToolsNavigationItem.Notes.Companion.ARG_NOTE_ID
import com.eyther.lumbridge.features.tools.overview.screens.ToolsOverviewScreen

@Composable
fun ToolsNavHost(
    navController: NavHostController
) {
    NavHost(
        modifier = Modifier
            .fillMaxSize(),
        navController = navController,
        startDestination = ToolsNavigationItem.Overview.route
    ) {
        composable(route = ToolsNavigationItem.Overview.route) {
            ToolsOverviewScreen(
                navController,
                ToolsNavigationItem.Overview.label
            )
        }

        navigation(
            startDestination = ToolsNavigationItem.NetSalary.Input.route,
            route = ToolsNavigationItem.NetSalary.HOST_ROUTE
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
                route = ToolsNavigationItem.NetSalary.Input.route
            ) { backstackEntry ->
                val argumentsCache =
                    backstackEntry.sharedViewModel<NetSalaryScreenArgumentsCacheViewModel>(navController)

                NetSalaryInputScreen(
                    navController = navController,
                    argumentsCache = argumentsCache,
                    label = ToolsNavigationItem.NetSalary.Input.label
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
                route = ToolsNavigationItem.NetSalary.Result.route
            ) { backstackEntry ->
                val argumentsCache =
                    backstackEntry.sharedViewModel<NetSalaryScreenArgumentsCacheViewModel>(navController)

                NetSalaryResultScreen(
                    navController = navController,
                    argumentsCache = argumentsCache,
                    label = ToolsNavigationItem.NetSalary.Result.label
                )
            }
        }

        composable(
            enterTransition = {
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

        navigation(
            startDestination = ToolsNavigationItem.NetSalary.Input.route,
            route = ToolsNavigationItem.NetSalary.HOST_ROUTE
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
                route = ToolsNavigationItem.NetSalary.Input.route
            ) { backstackEntry ->
                val argumentsCache =
                    backstackEntry.sharedViewModel<NetSalaryScreenArgumentsCacheViewModel>(navController)

                NetSalaryInputScreen(
                    navController = navController,
                    argumentsCache = argumentsCache,
                    label = ToolsNavigationItem.NetSalary.Input.label
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
                route = ToolsNavigationItem.NetSalary.Result.route
            ) { backstackEntry ->
                val argumentsCache =
                    backstackEntry.sharedViewModel<NetSalaryScreenArgumentsCacheViewModel>(navController)

                NetSalaryResultScreen(
                    navController = navController,
                    argumentsCache = argumentsCache,
                    label = ToolsNavigationItem.NetSalary.Result.label
                )
            }
        }

        navigation(
            startDestination = ToolsNavigationItem.Groceries.GroceriesList.route,
            route = ToolsNavigationItem.Groceries.HOST_ROUTE
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
                route = ToolsNavigationItem.Groceries.GroceriesList.route
            ) {
                GroceriesListsScreen(
                    navController = navController,
                    label = ToolsNavigationItem.Groceries.GroceriesList.label
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
                route = ToolsNavigationItem.Groceries.GroceriesListDetails.route,
                arguments = listOf(
                    navArgument(ARG_GROCERIES_LIST_ID) {
                        type = NavType.LongType
                        nullable = false
                        defaultValue = 0L
                    }
                )
            ) {
                GroceriesListDetailsScreen(
                    navController = navController,
                    label = ToolsNavigationItem.Groceries.GroceriesListDetails.label
                )
            }
        }

        navigation(
            startDestination = ToolsNavigationItem.Notes.NotesList.route,
            route = ToolsNavigationItem.Notes.HOST_ROUTE
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
                route = ToolsNavigationItem.Notes.NotesList.route
            ) {
                NotesListScreen(
                    navController = navController,
                    label = ToolsNavigationItem.Notes.NotesList.label
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
                route = ToolsNavigationItem.Notes.NotesDetails.route,
                arguments = listOf(
                    navArgument(ARG_NOTE_ID) {
                        type = NavType.LongType
                        nullable = false
                        defaultValue = 0L
                    }
                )
            ) {
                NoteDetailsScreen(
                    navController = navController,
                    label = ToolsNavigationItem.Notes.NotesDetails.label
                )
            }
        }
    }
}
