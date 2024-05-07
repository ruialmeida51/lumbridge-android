package com.eyther.lumbridge.features.tools.netsalary.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.eyther.lumbridge.features.tools.netsalary.model.NetSalaryScreenViewState
import com.eyther.lumbridge.features.tools.netsalary.viewmodel.INetSalaryScreenViewModel
import com.eyther.lumbridge.features.tools.netsalary.viewmodel.NetSalaryScreenViewModel
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.LumbridgeTopAppBar
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.TopAppBarVariation
import com.eyther.lumbridge.ui.theme.DefaultPadding

@Composable
fun NetSalaryScreen(
    navController: NavHostController,
    label: String,
    viewModel: INetSalaryScreenViewModel = hiltViewModel<NetSalaryScreenViewModel>()
) {
    val state = viewModel.viewState.collectAsState()

    Scaffold(
        topBar = {
            LumbridgeTopAppBar(
                TopAppBarVariation.TitleAndIcon(
                    title = label,
                    onIconClick = { navController.popBackStack() }
                )
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(DefaultPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (val currentState = state.value) {
                    is NetSalaryScreenViewState.Content -> Content(
                        navController = navController,
                        state = currentState,
                        onCalculateNetSalary = viewModel::onCalculateNetSalary,
                        onEditSalary = viewModel::onEditSalary
                    )

                    is NetSalaryScreenViewState.Loading -> Unit
                }
            }
        }
    }
}

@Composable
fun ColumnScope.Content(
    navController: NavHostController,
    state: NetSalaryScreenViewState.Content,
    onCalculateNetSalary: (Float, Float) -> Unit,
    onEditSalary: () -> Unit
) {
    when (state) {
        is NetSalaryScreenViewState.Content.Overview -> DrawOverviewPerCountry(
            navController = navController,
            overviewState = state,
            onEditSalary = onEditSalary
        )

        is NetSalaryScreenViewState.Content.Input -> DrawInputPerCountry(
            inputState = state,
            onCalculateNetSalary = onCalculateNetSalary
        )
    }
}

@Composable
private fun ColumnScope.DrawOverviewPerCountry(
    navController: NavController,
    overviewState: NetSalaryScreenViewState.Content.Overview,
    onEditSalary: () -> Unit
) {
}

@Composable
private fun ColumnScope.DrawInputPerCountry(
    inputState: NetSalaryScreenViewState.Content.Input,
    onCalculateNetSalary: (Float, Float) -> Unit
) {

}
