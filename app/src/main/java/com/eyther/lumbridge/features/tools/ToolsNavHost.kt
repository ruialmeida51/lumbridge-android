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
import com.eyther.lumbridge.features.tools.navigation.ToolsNavigationItem
import com.eyther.lumbridge.features.tools.navigation.ToolsNavigationItem.Notes.Companion.ARG_NOTE_ID
import com.eyther.lumbridge.features.tools.navigation.ToolsNavigationItem.RecurringPayments.Companion.ARG_RECURRING_PAYMENT_ID
import com.eyther.lumbridge.features.tools.navigation.ToolsNavigationItem.Reminders.Companion.ARG_REMINDER_ID
import com.eyther.lumbridge.features.tools.navigation.ToolsNavigationItem.Shopping.Companion.ARG_SHOPPING_LIST_ID
import com.eyther.lumbridge.features.tools.netsalary.arguments.NetSalaryScreenArgumentsCacheViewModel
import com.eyther.lumbridge.features.tools.netsalary.screens.NetSalaryInputScreen
import com.eyther.lumbridge.features.tools.netsalary.screens.NetSalaryResultScreen
import com.eyther.lumbridge.features.tools.notes.screens.NoteDetailsScreen
import com.eyther.lumbridge.features.tools.notes.screens.NotesListScreen
import com.eyther.lumbridge.features.tools.overview.screens.ToolsOverviewScreen
import com.eyther.lumbridge.features.tools.recurringpayments.screens.EditRecurringPaymentsScreen
import com.eyther.lumbridge.features.tools.recurringpayments.screens.RecurringPaymentsOverviewScreen
import com.eyther.lumbridge.features.tools.shopping.screens.ShoppingListDetailsScreen
import com.eyther.lumbridge.features.tools.shopping.screens.ShoppingListsScreen
import com.eyther.lumbridge.features.tools.tasksandreminders.screens.RemindersEditScreen
import com.eyther.lumbridge.features.tools.tasksandreminders.screens.RemindersOverviewScreen

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
            startDestination = ToolsNavigationItem.Shopping.ShoppingList.route,
            route = ToolsNavigationItem.Shopping.HOST_ROUTE
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
                route = ToolsNavigationItem.Shopping.ShoppingList.route
            ) {
                ShoppingListsScreen(
                    navController = navController,
                    label = ToolsNavigationItem.Shopping.ShoppingList.label
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
                route = ToolsNavigationItem.Shopping.ShoppingListDetails.route,
                arguments = listOf(
                    navArgument(ARG_SHOPPING_LIST_ID) {
                        type = NavType.LongType
                        nullable = false
                        defaultValue = -1L
                    }
                )
            ) {
                ShoppingListDetailsScreen(
                    navController = navController,
                    label = ToolsNavigationItem.Shopping.ShoppingListDetails.label
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

        navigation(
            startDestination = ToolsNavigationItem.Reminders.RemindersList.route,
            route = ToolsNavigationItem.Reminders.HOST_ROUTE
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
                route = ToolsNavigationItem.Reminders.RemindersList.route
            ) {
                RemindersOverviewScreen(
                    navController = navController,
                    label = ToolsNavigationItem.Reminders.RemindersList.label
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
                route = ToolsNavigationItem.Reminders.RemindersDetails.route,
                arguments = listOf(
                    navArgument(ARG_REMINDER_ID) {
                        type = NavType.LongType
                        nullable = false
                        defaultValue = 0L
                    }
                )
            ) {
                RemindersEditScreen(
                    navController = navController,
                    label = ToolsNavigationItem.Reminders.RemindersDetails.label
                )
            }
        }

        navigation(
            startDestination = ToolsNavigationItem.RecurringPayments.Overview.route,
            route = ToolsNavigationItem.RecurringPayments.HOST_ROUTE
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
                route = ToolsNavigationItem.RecurringPayments.Overview.route
            ) {
                RecurringPaymentsOverviewScreen(
                    navController = navController,
                    label = ToolsNavigationItem.RecurringPayments.Overview.label
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
                route = ToolsNavigationItem.RecurringPayments.Edit.route,
                arguments = listOf(
                    navArgument(ARG_RECURRING_PAYMENT_ID) {
                        type = NavType.LongType
                        nullable = false
                        defaultValue = 0L
                    }
                )
            ) {
                EditRecurringPaymentsScreen(
                    navController = navController,
                    label = ToolsNavigationItem.RecurringPayments.Edit.label
                )
            }
        }
    }
}
