package com.eyther.lumbridge.features.tools

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.eyther.lumbridge.features.tools.ctc.screens.CostToCompanyScreen
import com.eyther.lumbridge.features.tools.netsalary.screens.NetSalaryScreen
import com.eyther.lumbridge.features.tools.overview.navigation.ToolsNavigationItem
import com.eyther.lumbridge.features.tools.overview.screens.ToolsScreen

fun NavGraphBuilder.toolsNavigation(navController: NavController) {
    composable(route = ToolsNavigationItem.Overview.route) {
        ToolsScreen(
            navController,
            ToolsNavigationItem.Overview.label
        )
    }

    composable(route = ToolsNavigationItem.NetSalary.route) {
        NetSalaryScreen(
            navController = navController,
            label = ToolsNavigationItem.NetSalary.label
        )
    }

    composable(route = ToolsNavigationItem.CostToCompany.route) {
        CostToCompanyScreen(
            navController = navController,
            label = ToolsNavigationItem.CostToCompany.label
        )
    }
}
