package com.eyther.lumbridge.features.profile.editprofile.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.eyther.lumbridge.R
import com.eyther.lumbridge.extensions.kotlin.capitalise
import com.eyther.lumbridge.features.profile.editprofile.model.EditProfileScreenViewEffects
import com.eyther.lumbridge.features.profile.editprofile.model.EditProfileScreenViewState
import com.eyther.lumbridge.features.profile.editprofile.viewmodel.EditProfileScreenViewModel
import com.eyther.lumbridge.features.profile.editprofile.viewmodel.IEditProfileScreenViewModel
import com.eyther.lumbridge.ui.common.composables.components.buttons.LumbridgeButton
import com.eyther.lumbridge.ui.common.composables.components.card.ColumnCardWrapper
import com.eyther.lumbridge.ui.common.composables.components.input.DropdownInput
import com.eyther.lumbridge.ui.common.composables.components.input.TextInput
import com.eyther.lumbridge.ui.common.composables.components.loading.LoadingIndicator
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.LumbridgeTopAppBar
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.TopAppBarVariation
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.HalfPadding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun EditProfileScreen(
    navController: NavHostController,
    @StringRes label: Int,
    viewModel: IEditProfileScreenViewModel = hiltViewModel<EditProfileScreenViewModel>()
) {
    val state = viewModel.viewState.collectAsStateWithLifecycle().value
    val lifecycleOwner = LocalLifecycleOwner.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.viewEffects
                .onEach { viewEffects ->
                    when (viewEffects) {
                        is EditProfileScreenViewEffects.ShowError -> {
                            snackbarHostState.showSnackbar(
                                message = viewEffects.message,
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                }
                .collect()
        }
    }

    Scaffold(
        topBar = {
            LumbridgeTopAppBar(
                topAppBarVariation = TopAppBarVariation.TitleAndIcon(
                    title = stringResource(id = label)
                ) {
                    navController.popBackStack()
                }
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
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
                is EditProfileScreenViewState.Content -> Content(
                    navController = navController,
                    state = state,
                    viewModel = viewModel
                )

                is EditProfileScreenViewState.Loading -> LoadingIndicator()
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
        text = stringResource(id = R.string.edit_profile_how_to_contact),
        style = MaterialTheme.typography.bodyLarge
    )

    ColumnCardWrapper {
        TextInput(
            modifier = Modifier.padding(bottom = DefaultPadding),
            state = state.inputState.name,
            label = stringResource(id = R.string.name),
            onInputChanged = viewModel::onNameChanged,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            )
        )

        TextInput(
            modifier = Modifier.padding(bottom = DefaultPadding),
            state = state.inputState.email,
            label = stringResource(id = R.string.email),
            onInputChanged = viewModel::onEmailChanged,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email
            )
        )

        DropdownInput(
            label = stringResource(id = R.string.edit_profile_select_country),
            selectedOption = state.inputState.locale.name.capitalise(),
            items = state.availableLocales.map { it.countryCode to it.name.capitalise() },
            onItemClick = { identifier, _ ->
                viewModel.onLocaleChanged(identifier)
            }
        )
    }

    Spacer(modifier = Modifier.height(DefaultPadding))

    LumbridgeButton(
        modifier = Modifier.padding(horizontal = DefaultPadding),
        enableButton = state.shouldEnableSaveButton,
        onClick = { viewModel.saveUserData(navController) },
        label = stringResource(id = R.string.edit_profile_save)
    )
}
