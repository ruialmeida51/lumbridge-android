package com.eyther.lumbridge.features.tools.overview.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
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
import androidx.navigation.compose.rememberNavController
import com.eyther.lumbridge.features.tools.overview.model.ToolItem
import com.eyther.lumbridge.features.tools.overview.model.ToolScreenViewState
import com.eyther.lumbridge.features.tools.overview.viewmodel.ToolsScreenViewModel
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.HalfPadding
import com.eyther.lumbridge.ui.theme.LumbridgeTheme
import com.eyther.lumbridge.ui.theme.runescapeTypography

@Composable
fun ToolsScreen(
    navController: NavController,
    label: String,
    toolsScreenViewModel: ToolsScreenViewModel = hiltViewModel()
) {
    val state = toolsScreenViewModel.viewState.collectAsState()

    when (val currentState = state.value) {
        is ToolScreenViewState.Content -> Content(
            state = currentState,
            label = label,
            navController = navController,
            onItemClick = toolsScreenViewModel::onItemClick
        )
    }
}

@Composable
fun Content(
    state: ToolScreenViewState.Content,
    label: String,
    navController: NavController,
    onItemClick: (toolItem: ToolItem, navController: NavController) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(DefaultPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = runescapeTypography.titleLarge
        )

        Spacer(
            modifier = Modifier.height(DefaultPadding)
        )

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
    LumbridgeTheme(darkTheme = false) { ToolsScreen(navController, "Tools") }
}
