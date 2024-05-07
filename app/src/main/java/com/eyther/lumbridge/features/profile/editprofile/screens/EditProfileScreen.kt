@file:OptIn(ExperimentalMaterial3Api::class)

package com.eyther.lumbridge.features.profile.editprofile.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.eyther.lumbridge.extensions.kotlin.capitalise
import com.eyther.lumbridge.features.profile.editprofile.model.EditProfileScreenViewState
import com.eyther.lumbridge.features.profile.editprofile.viewmodel.EditProfileScreenViewModel
import com.eyther.lumbridge.features.profile.editprofile.viewmodel.IEditProfileScreenViewModel
import com.eyther.lumbridge.ui.common.composables.components.components.TextInput
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.LumbridgeTopAppBar
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.TopAppBarVariation
import com.eyther.lumbridge.ui.theme.DefaultPadding

@Composable
fun EditProfileScreen(
    navController: NavHostController,
    label: String,
    viewModel: IEditProfileScreenViewModel = hiltViewModel<EditProfileScreenViewModel>()
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
                .verticalScroll(rememberScrollState())
                .height(IntrinsicSize.Max)
        ) {
            when (state) {
                is EditProfileScreenViewState.Content -> Content(
                    navController = navController,
                    state = state,
                    viewModel = viewModel
                )

                is EditProfileScreenViewState.Loading -> Unit
            }
        }
    }
}

@Composable
private fun Content(
    navController: NavHostController,
    state: EditProfileScreenViewState.Content,
    viewModel: IEditProfileScreenViewModel
) {
    Column(
        modifier = Modifier.padding(DefaultPadding)
    ) {
        var dropDownExpanded by remember {
            mutableStateOf(false)
        }

        TextInput(
            modifier = Modifier.padding(bottom = DefaultPadding),
            state = state.inputState.email,
            label = "Name",
            onInputChanged = viewModel::onNameChanged
        )

        TextInput(
            modifier = Modifier.padding(bottom = DefaultPadding),
            state = state.inputState.email,
            label = "Email",
            onInputChanged = viewModel::onEmailChanged
        )

        ExposedDropdownMenuBox(
            modifier = Modifier.padding(bottom = DefaultPadding),
            expanded = dropDownExpanded,
            onExpandedChange = { dropDownExpanded = !dropDownExpanded }
        ) {
            TextField(
                value = state.inputState.locale.name.capitalise(),
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropDownExpanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = dropDownExpanded,
                onDismissRequest = { dropDownExpanded = false }
            ) {
                state.availableLocales.forEach {
                    DropdownMenuItem(
                        text = { Text(it.name.capitalise()) },
                        onClick = {
                            dropDownExpanded = false
                            viewModel.onLocaleChanged(it)
                        }
                    )
                }
            }
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = state.shouldEnableSaveButton,
            onClick = { viewModel.saveUserData(navController) }
        ) {
            Text(text = "Save Profile")
        }
    }
}
