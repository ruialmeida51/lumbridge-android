package com.eyther.lumbridge.features.tools.overview.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.eyther.lumbridge.extensions.platform.navigateTo
import com.eyther.lumbridge.features.tools.overview.model.ToolItem
import com.eyther.lumbridge.features.tools.overview.model.ToolScreenViewState
import com.eyther.lumbridge.features.tools.overview.viewmodel.IToolsScreenViewModel
import com.eyther.lumbridge.features.tools.overview.viewmodel.ToolsScreenViewModel
import com.eyther.lumbridge.ui.common.composables.components.setting.MovementSetting
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.LumbridgeTopAppBar
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.TopAppBarVariation
import com.eyther.lumbridge.ui.navigation.NavigationItem
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.DefaultRoundedCorner
import com.eyther.lumbridge.ui.theme.HalfPadding
import com.eyther.lumbridge.ui.theme.QuarterPadding

@Composable
fun ToolsOverviewScreen(
    navController: NavHostController,
    @StringRes label: Int,
    toolsScreenViewModel: IToolsScreenViewModel = hiltViewModel<ToolsScreenViewModel>()
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            LumbridgeTopAppBar(TopAppBarVariation.Title(title = stringResource(id = label)))
        }
    ) { paddingValues ->
        val state = toolsScreenViewModel.viewState.collectAsStateWithLifecycle()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val currentState = state.value) {
                is ToolScreenViewState.Content -> Content(
                    state = currentState,
                    navController = navController,
                    getRouteToNavigate = toolsScreenViewModel::getRouteToNavigate
                )
            }
        }
    }
}

@Composable
private fun Content(
    state: ToolScreenViewState.Content,
    navController: NavController,
    getRouteToNavigate: (ToolItem) -> NavigationItem
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(DefaultPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(state.options.keys.size) { index ->
                val keyRes = state.options.keys.elementAt(index)
                Text(
                    modifier = Modifier
                        .padding(
                            top = if (index > 0) DefaultPadding else 0.dp,
                            bottom = HalfPadding
                        )
                        .align(Alignment.Start),
                    text = stringResource(id = keyRes),
                    style = MaterialTheme.typography.bodyLarge
                )

                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(DefaultRoundedCorner))
                        .shadow(elevation = QuarterPadding)
                        .background(MaterialTheme.colorScheme.surfaceContainer)
                        .padding(DefaultPadding),
                    verticalArrangement = Arrangement.spacedBy(DefaultPadding)
                ) {
                    state.options[keyRes]?.forEach { item ->
                        MovementSetting(
                            icon = item.icon,
                            label = stringResource(id = item.text),
                            onClick = { navController.navigateTo(getRouteToNavigate(item)) }
                        )
                    }
                }
            }
        }
    }
}
