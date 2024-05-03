package com.eyther.lumbridge.features.editfinancialprofile.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.features.editfinancialprofile.model.EditFinancialProfileScreenViewState
import com.eyther.lumbridge.features.editfinancialprofile.screens.portugal.SalaryInputPortugal
import com.eyther.lumbridge.features.editfinancialprofile.viewmodel.EditFinancialProfileScreenViewModel
import com.eyther.lumbridge.features.editfinancialprofile.viewmodel.EditFinancialProfileScreenViewModelInterface
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.LumbridgeTopAppBar
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.TopAppBarVariation
import com.eyther.lumbridge.ui.theme.DefaultPadding

@Composable
fun EditFinancialProfileScreen(
    navController: NavHostController,
    label: String,
    viewModel: EditFinancialProfileScreenViewModelInterface = hiltViewModel<EditFinancialProfileScreenViewModel>()
) {
    val state = viewModel.viewState.collectAsState().value

    Scaffold(
        topBar = {
            LumbridgeTopAppBar(
                topAppBarVariation = TopAppBarVariation.TitleAndIcon(title = label) {
                    navController.popBackStack()
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (state) {
                is EditFinancialProfileScreenViewState.Content -> Content(
                    navController = navController,
                    state = state,
                    saveUserData = viewModel::saveUserData
                )

                is EditFinancialProfileScreenViewState.Loading -> Unit
            }
        }
    }
}

@Composable
private fun Content(
    navController: NavHostController,
    state: EditFinancialProfileScreenViewState.Content,
    saveUserData: (Float, Float, NavHostController) -> Unit
) {
    Column(
        Modifier.padding(DefaultPadding)
    ) {
        when (state.locale) {
            SupportedLocales.PORTUGAL -> SalaryInputPortugal(
                navController = navController,
                state = state,
                saveUserData = saveUserData
            )
        }
    }
}
