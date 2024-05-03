@file:OptIn(ExperimentalMaterial3Api::class)

package com.eyther.lumbridge.features.profile.editprofile.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.extensions.kotlin.capitalise
import com.eyther.lumbridge.features.profile.editprofile.model.EditProfileScreenViewState
import com.eyther.lumbridge.features.profile.editprofile.viewmodel.EditProfileScreenViewModel
import com.eyther.lumbridge.features.profile.editprofile.viewmodel.EditProfileScreenViewModelInterface
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.LumbridgeTopAppBar
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.TopAppBarVariation
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.runescapeTypography

@Composable
fun EditProfileScreen(
    navController: NavHostController,
    label: String,
    viewModel: EditProfileScreenViewModelInterface = hiltViewModel<EditProfileScreenViewModel>()
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
        ) {
            when (state) {
                is EditProfileScreenViewState.Content -> Content(
                    navController = navController,
                    state = state,
                    saveUserData = viewModel::saveUserData
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
    saveUserData: (String, String, SupportedLocales, NavHostController) -> Unit
) {
    Column(
        modifier = Modifier.padding(DefaultPadding)
    ) {
        var name by remember {
            mutableStateOf(TextFieldValue(state.getUserData()?.name.orEmpty()))
        }

        var email by remember {
            mutableStateOf(TextFieldValue(state.getUserData()?.email.orEmpty()))
        }

        var locale by remember {
            mutableStateOf(state.getUserData()?.locale ?: SupportedLocales.PORTUGAL)
        }

        var dropDownExpanded by remember {
            mutableStateOf(false)
        }

        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = name,
            textStyle = runescapeTypography.bodyLarge,
            onValueChange = { name = it },
            label = {
                Text(
                    text = "Name",
                    style = runescapeTypography.bodyLarge
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            colors = TextFieldDefaults.colors(
                focusedLabelColor = MaterialTheme.colorScheme.onSecondary
            )
        )

        Spacer(modifier = Modifier.height(DefaultPadding))

        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = email,
            textStyle = runescapeTypography.bodyLarge,
            onValueChange = { email = it },
            label = {
                Text(
                    text = "Email",
                    style = runescapeTypography.bodyLarge
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = TextFieldDefaults.colors(
                focusedLabelColor = MaterialTheme.colorScheme.onSecondary
            )
        )

        Spacer(modifier = Modifier.height(DefaultPadding))

        ExposedDropdownMenuBox(
            expanded = dropDownExpanded,
            onExpandedChange = { dropDownExpanded = !dropDownExpanded }
        ) {
            TextField(
                value = locale.name.capitalise(),
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropDownExpanded)
                },
                modifier = Modifier.menuAnchor().fillMaxWidth()
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
                            locale = it
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(DefaultPadding))

        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = shouldEnableButton(name.text, email.text, locale),
            onClick = { saveUserData(name.text, email.text, locale, navController) }
        ) {
            Text(text = "Save Profile")
        }
    }
}

private fun shouldEnableButton(name: String?, email: String?, locale: SupportedLocales?): Boolean {
    return name?.isNotEmpty() == true && email?.isNotEmpty() == true && locale != null
}
