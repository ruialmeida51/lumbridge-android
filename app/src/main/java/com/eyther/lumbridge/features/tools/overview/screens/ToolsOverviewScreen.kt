package com.eyther.lumbridge.features.tools.overview.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
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
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.LumbridgeTopAppBar
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.TopAppBarVariation
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.HalfPadding
import com.eyther.lumbridge.ui.theme.LumbridgeTheme
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

        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
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
        LazyVerticalGrid(
            columns = GridCells.Fixed(2)
        ) {
            items(state.options.size) {
                val item = state.options[it]

                ToolGrid(
                    toolItem = item,
                    navController = navController,
                    onItemClick = onItemClick
                )
            }
        }
    }
}

@Composable
fun ToolGrid(
    toolItem: ToolItem,
    navController: NavController,
    onItemClick: (toolItem: ToolItem, navController: NavController) -> Unit
) {
    Surface(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(HalfPadding),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onPrimary),
        color = MaterialTheme.colorScheme.surface,
        onClick = { onItemClick(toolItem, navController) }
    ) {
        Column(
            modifier = Modifier.padding(HalfPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = toolItem.text,
                style = runescapeTypography.titleSmall,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
@Preview
private fun Preview() {
    val navController = rememberNavController()
    LumbridgeTheme(darkTheme = false) { ToolsOverviewScreen(navController, "Tools") }
}
