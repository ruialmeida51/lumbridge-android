@file:OptIn(ExperimentalMaterial3Api::class)

package com.eyther.lumbridge.features.profile.editprofile.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.eyther.lumbridge.extensions.kotlin.capitalise
import com.eyther.lumbridge.features.profile.editprofile.model.EditProfileScreenViewState
import com.eyther.lumbridge.features.profile.editprofile.viewmodel.EditProfileScreenViewModel
import com.eyther.lumbridge.features.profile.editprofile.viewmodel.IEditProfileScreenViewModel
import com.eyther.lumbridge.ui.common.composables.components.components.LumbridgeButton
import com.eyther.lumbridge.ui.common.composables.components.components.TextInput
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.LumbridgeTopAppBar
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.TopAppBarVariation
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.HalfPadding
import com.eyther.lumbridge.ui.theme.QuarterPadding
import com.eyther.lumbridge.ui.theme.runescapeTypography

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
        Column(
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
private fun ColumnScope.Content(
    navController: NavHostController,
    state: EditProfileScreenViewState.Content,
    viewModel: IEditProfileScreenViewModel
) {
    Text(
        modifier = Modifier
            .padding(
                start = DefaultPadding,
                end = DefaultPadding,
                top = DefaultPadding,
                bottom = HalfPadding
            )
            .align(Alignment.Start),
        text = "How should we contact you?",
        style = runescapeTypography.bodyLarge
    )

    Column(
        modifier = Modifier
            .padding(horizontal = DefaultPadding)
            .clip(RoundedCornerShape(8.dp))
            .shadow(elevation = QuarterPadding)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(DefaultPadding)
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

        Text(
            text = "Select your preferred country",
            style = runescapeTypography.bodyLarge,
            modifier = Modifier.padding(bottom = QuarterPadding),
            color = MaterialTheme.colorScheme.tertiary
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
                textStyle = runescapeTypography.bodyMedium,
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
                        text = {
                            Text(
                                it.name.capitalise(),
                                style = runescapeTypography.bodyMedium
                            )
                        },
                        onClick = {
                            dropDownExpanded = false
                            viewModel.onLocaleChanged(it)
                        }
                    )
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(DefaultPadding))

    LumbridgeButton(
        modifier = Modifier.padding(horizontal = DefaultPadding),
        enableButton = state.shouldEnableSaveButton,
        onClick = { viewModel.saveUserData(navController) },
        label = "Save Profile"
    )
}
