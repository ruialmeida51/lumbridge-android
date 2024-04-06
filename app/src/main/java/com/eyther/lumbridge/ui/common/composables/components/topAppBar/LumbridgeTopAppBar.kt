package com.eyther.lumbridge.ui.common.composables.components.topAppBar

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.eyther.lumbridge.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun LumbridgeTopAppBar() {
	TopAppBar(
		title = { Text(text = stringResource(id = R.string.app_name)) }
	)
}