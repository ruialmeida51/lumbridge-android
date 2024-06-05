package com.eyther.lumbridge.features.tools.savings.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.eyther.lumbridge.features.tools.savings.viewModel.ISavingsScreenViewModel
import com.eyther.lumbridge.features.tools.savings.viewModel.SavingsScreenViewModel
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.LumbridgeTopAppBar
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.TopAppBarVariation

@Composable
fun SavingsScreen(
    navController: NavController,
    @StringRes label: Int,
    viewModel: ISavingsScreenViewModel = hiltViewModel<SavingsScreenViewModel>()
) {
    Scaffold(
        topBar = {
            LumbridgeTopAppBar(
                TopAppBarVariation.TitleAndIcon(
                    title = stringResource(id = label),
                    onIconClick = { navController.popBackStack() }
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            Text(text = "${stringResource(id = label)} - Work in Progress")
        }
    }
}
