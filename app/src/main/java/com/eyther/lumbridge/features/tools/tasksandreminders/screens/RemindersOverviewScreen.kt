@file:OptIn(ExperimentalPermissionsApi::class)

package com.eyther.lumbridge.features.tools.tasksandreminders.screens

import android.os.Build
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.eyther.lumbridge.R
import com.eyther.lumbridge.extensions.platform.navigateToWithArgs
import com.eyther.lumbridge.features.tools.navigation.ToolsNavigationItem
import com.eyther.lumbridge.features.tools.tasksandreminders.model.overview.RemindersOverviewScreenViewEffects
import com.eyther.lumbridge.features.tools.tasksandreminders.model.overview.RemindersOverviewScreenViewState
import com.eyther.lumbridge.features.tools.tasksandreminders.viewmodel.overview.IRemindersOverviewScreenViewModel
import com.eyther.lumbridge.features.tools.tasksandreminders.viewmodel.overview.RemindersOverviewScreenViewModel
import com.eyther.lumbridge.launcher.model.permissions.NeededPermission
import com.eyther.lumbridge.ui.common.composables.components.defaults.EmptyScreenWithButton
import com.eyther.lumbridge.ui.common.composables.components.loading.LoadingIndicator
import com.eyther.lumbridge.ui.common.composables.components.permissions.TryRequestPermission
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.LumbridgeTopAppBar
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.TopAppBarVariation
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun RemindersOverviewScreen(
    navController: NavHostController,
    @StringRes label: Int,
    viewModel: IRemindersOverviewScreenViewModel = hiltViewModel<RemindersOverviewScreenViewModel>()
) {
    val state = viewModel.viewState.collectAsStateWithLifecycle().value
    val snackbarHostState = remember { SnackbarHostState() }
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    val neededPermission = remember { NeededPermission.Notifications }
    val notificationsPermissionState = rememberPermissionState(neededPermission.permission)
    val askForNotificationsPermission = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.viewEffects
                .onEach { viewEffects ->
                    when (viewEffects) {
                        is RemindersOverviewScreenViewEffects.ShowError -> {
                            snackbarHostState.showSnackbar(
                                message = viewEffects.message,
                                duration = SnackbarDuration.Short
                            )
                        }

                        is RemindersOverviewScreenViewEffects.CloseScreen -> navController.popBackStack()
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
        ) {
            when (state) {
                is RemindersOverviewScreenViewState.Loading -> {
                    LoadingIndicator()
                }

                is RemindersOverviewScreenViewState.Empty -> {
                    EmptyScreen {
                        navController.navigateToWithArgs(ToolsNavigationItem.Reminders.RemindersDetails, -1L)
                    }
                }

                is RemindersOverviewScreenViewState.Content -> {

                    if (askForNotificationsPermission.value && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        TryRequestPermission(
                            neededPermission = neededPermission,
                            permissionState = notificationsPermissionState,
                            askForNotificationsPermission = askForNotificationsPermission
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyScreen(
    onCreateReminder: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        EmptyScreenWithButton(
            modifier = Modifier.padding(DefaultPadding),
            text = stringResource(id = R.string.tools_reminders_no_list_message),
            buttonText = stringResource(id = R.string.tools_reminders_no_list_create),
            icon = {
                Icon(
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(id = R.drawable.ic_checklist),
                    contentDescription = stringResource(id = R.string.tools_reminders_list)
                )
            },
            onButtonClick = onCreateReminder
        )
    }
}
