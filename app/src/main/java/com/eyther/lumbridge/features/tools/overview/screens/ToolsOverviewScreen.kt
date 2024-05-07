package com.eyther.lumbridge.features.tools.overview.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.eyther.lumbridge.features.tools.overview.model.ToolItem
import com.eyther.lumbridge.features.tools.overview.model.ToolScreenViewState
import com.eyther.lumbridge.features.tools.overview.viewmodel.IToolsScreenViewModel
import com.eyther.lumbridge.features.tools.overview.viewmodel.ToolsScreenViewModel
import com.eyther.lumbridge.ui.common.composables.components.setting.MovementSetting
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.LumbridgeTopAppBar
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.TopAppBarVariation
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.HalfPadding
import com.eyther.lumbridge.ui.theme.LumbridgeTheme
import com.eyther.lumbridge.ui.theme.QuarterPadding
import com.eyther.lumbridge.ui.theme.runescapeTypography

@Composable
fun ToolsOverviewScreen(
    navController: NavHostController,
    label: String,
    toolsScreenViewModel: IToolsScreenViewModel = hiltViewModel<ToolsScreenViewModel>()
) {
    Scaffold(
        topBar = {
            LumbridgeTopAppBar(TopAppBarVariation.Title(title = label))
        }
    ) { paddingValues ->
        val state = toolsScreenViewModel.viewState.collectAsState()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val currentState = state.value) {
                is ToolScreenViewState.Content -> Content(
                    state = currentState,
                    navController = navController,
                    onItemClick = toolsScreenViewModel::navigate
                )
            }
        }
    }
}

@Composable
fun Content(
    state: ToolScreenViewState.Content,
    navController: NavController,
    onItemClick: (toolItem: ToolItem, navController: NavController) -> Unit
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
                val key = state.options.keys.elementAt(index)
                Text(
                    modifier = Modifier
                        .padding(
                            top = if (index > 0) DefaultPadding else 0.dp,
                            bottom = HalfPadding
                        )
                        .align(Alignment.Start),
                    text = key,
                    style = runescapeTypography.bodyLarge
                )
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .shadow(elevation = QuarterPadding)
                        .background(MaterialTheme.colorScheme.surfaceContainer)
                        .padding(DefaultPadding),
                    verticalArrangement = Arrangement.spacedBy(DefaultPadding)
                ) {
                    state.options[key]?.forEach { item ->
                        MovementSetting(
                            icon = item.icon,
                            label = item.text,
                            onClick = { onItemClick(item, navController) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview
private fun Preview() {
    val navController = rememberNavController()
    LumbridgeTheme(darkTheme = false) { ToolsOverviewScreen(navController, "Tools") }
}
