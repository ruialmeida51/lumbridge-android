package com.eyther.lumbridge.features.overview.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.eyther.lumbridge.R
import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.features.overview.model.FinancialOverviewScreenViewState
import com.eyther.lumbridge.features.overview.navigation.FinancialOverviewNavigationItem
import com.eyther.lumbridge.features.overview.screens.portugal.FinancialOverviewPortugal
import com.eyther.lumbridge.features.overview.viewmodel.FinancialOverviewScreenViewModel
import com.eyther.lumbridge.features.overview.viewmodel.FinancialOverviewScreenViewModelInterface
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.LumbridgeTopAppBar
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.TopAppBarVariation
import com.eyther.lumbridge.ui.navigation.NavigationItem
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.runescapeTypography

@Composable
fun FinancialOverviewScreen(
    navController: NavHostController,
    label: String,
    viewModel: FinancialOverviewScreenViewModelInterface = hiltViewModel<FinancialOverviewScreenViewModel>()
) {
    val state = viewModel.viewState.collectAsState().value

    Scaffold(
        topBar = {
            LumbridgeTopAppBar(
                topAppBarVariation = TopAppBarVariation.Title(title = label)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(DefaultPadding)
        ) {
            when (state) {
                is FinancialOverviewScreenViewState.Content.Overview -> Overview(
                    navController = navController,
                    state = state,
                    navigate = viewModel::navigate
                )

                is FinancialOverviewScreenViewState.Content.Input -> Input(
                    navController = navController,
                    navigate = viewModel::navigate
                )

                is FinancialOverviewScreenViewState.Loading -> Unit
            }
        }
    }
}

@Composable
private fun ColumnScope.Overview(
    navController: NavHostController,
    state: FinancialOverviewScreenViewState.Content.Overview,
    navigate: (NavigationItem, NavHostController) -> Unit
) {
    when (state.locale) {
        SupportedLocales.PORTUGAL -> FinancialOverviewPortugal(
            state = state,
            onEditSalary = {
                navigate(FinancialOverviewNavigationItem.EditFinancialProfile, navController)
            }
        )
    }
}

@Composable
private fun ColumnScope.Input(
    navController: NavHostController,
    navigate: (NavigationItem, NavHostController) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .align(Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "You don't seem have an financial profile yet. " +
                    "Press the button below to create one.",
            style = runescapeTypography.titleSmall,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.padding(DefaultPadding))

        Icon(
            modifier = Modifier.size(32.dp).align(Alignment.CenterHorizontally),
            painter = painterResource(id = R.drawable.ic_savings),
            contentDescription = "Create financial Profile"
        )

        Spacer(modifier = Modifier.padding(DefaultPadding))

        Button(
            modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally),
            onClick = {
                navigate(
                    FinancialOverviewNavigationItem.EditFinancialProfile,
                    navController
                )
            }
        ) {
            Text(text = "Add Financial Profile")
        }
    }
}










