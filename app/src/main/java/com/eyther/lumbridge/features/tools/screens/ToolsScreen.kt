package com.eyther.lumbridge.features.tools.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eyther.lumbridge.features.tools.viewmodel.ToolsScreenViewModel
import com.eyther.lumbridge.features.tools.viewmodel.ToolsScreenViewModelInterface.ToolScreenViewState
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.runescapeTypography

@Composable
@Preview
fun ToolsScreen(
	toolsScreenViewModel: ToolsScreenViewModel = hiltViewModel()
) {
	when(val state = toolsScreenViewModel.viewState.collectAsState().value) {
		is ToolScreenViewState.Initial -> Unit
		is ToolScreenViewState.Content -> Content(state)
	}
}

@Composable
fun Content(state: ToolScreenViewState.Content) {
	Column(modifier = Modifier.padding(DefaultPadding)) {
		Text(text = "Here you can find a variety of tools", style = runescapeTypography.titleMedium)
		
		Spacer(modifier = Modifier.height(DefaultPadding))
		
		LazyHorizontalGrid(GridCells.Adaptive(50.dp)) {
			items(state.options.size) {
				GridItem()
			}
		}
	}
}

@Composable
fun LazyGridItemScope.GridItem() {
	Surface (
		modifier = Modifier
			.aspectRatio(1f)
			.padding(12.dp),
		color = Color(0xffbb2187),
	) {
		RandomSizeBox()
	}
}
