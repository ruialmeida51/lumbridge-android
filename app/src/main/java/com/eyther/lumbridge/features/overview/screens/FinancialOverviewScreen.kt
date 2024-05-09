package com.eyther.lumbridge.features.overview.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.eyther.lumbridge.features.overview.model.FinancialOverviewScreenViewState
import com.eyther.lumbridge.features.overview.model.FinancialOverviewTabItem
import com.eyther.lumbridge.features.overview.viewmodel.FinancialOverviewScreenViewModel
import com.eyther.lumbridge.features.overview.viewmodel.IFinancialOverviewScreenViewModel
import com.eyther.lumbridge.ui.common.composables.components.loading.LoadingIndicator
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.LumbridgeTopAppBar
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.TopAppBarVariation
import com.eyther.lumbridge.ui.navigation.NavigationItem
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.runescapeTypography

@Composable
fun FinancialOverviewScreen(
    navController: NavHostController,
    @StringRes label: Int,
    viewModel: IFinancialOverviewScreenViewModel = hiltViewModel<FinancialOverviewScreenViewModel>()
) {
    val state = viewModel.viewState.collectAsState().value

    Scaffold(
        topBar = {
            LumbridgeTopAppBar(
                topAppBarVariation = TopAppBarVariation.Title(title = stringResource(id = label))
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            when (state) {
                is FinancialOverviewScreenViewState.Content -> Overview(
                    navController = navController,
                    state = state,
                    navigate = viewModel::navigate,
                    onTabClicked = viewModel::onTabSelected
                )

                is FinancialOverviewScreenViewState.Loading -> LoadingIndicator()
            }
        }
    }
}

@Composable
private fun ColumnScope.Overview(
    navController: NavHostController,
    state: FinancialOverviewScreenViewState.Content,
    navigate: (NavigationItem, NavHostController) -> Unit,
    onTabClicked: (tabItem: FinancialOverviewTabItem) -> Unit
) {
    val currencySymbol = remember { state.locale.getCurrencySymbol() }

    TabRow(
        selectedTabIndex = state.selectedTabItem.ordinal,
        modifier = Modifier.padding(DefaultPadding)
    ) {
        FinancialOverviewTabItem.getItems().forEach { tabItem ->
            Tab(
                text = {
                    Text(
                        stringResource(id = tabItem.label),
                        style = runescapeTypography.bodyLarge
                    )
                },
                selected = state.selectedTabItem.ordinal == tabItem.ordinal,
                onClick = { onTabClicked(tabItem) }
            )
        }
    }

    when (state.selectedTabItem.ordinal) {
        FinancialOverviewTabItem.PersonalOverview.ordinal -> PersonalOverview(
            navController = navController,
            state = state,
            navigate = navigate,
            currencySymbol = currencySymbol
        )

        FinancialOverviewTabItem.MortgageOverview.ordinal -> MortgageOverview(
            navController = navController,
            state = state,
            navigate = navigate,
            currencySymbol = currencySymbol
        )
    }
}

