package com.eyther.lumbridge.features.tools.reminders.screens

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.platform.LocalContext
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
import com.eyther.lumbridge.features.tools.reminders.model.overview.RemindersOverviewScreenViewEffects
import com.eyther.lumbridge.features.tools.reminders.model.overview.RemindersOverviewScreenViewState
import com.eyther.lumbridge.features.tools.reminders.viewmodel.overview.IRemindersOverviewScreenViewModel
import com.eyther.lumbridge.features.tools.reminders.viewmodel.overview.RemindersOverviewScreenViewModel
import com.eyther.lumbridge.model.reminders.ReminderUi
import com.eyther.lumbridge.shared.time.extensions.toDayMonthYearHourMinuteString
import com.eyther.lumbridge.ui.common.composables.components.buttons.LumbridgeButton
import com.eyther.lumbridge.ui.common.composables.components.card.ColumnCardWrapper
import com.eyther.lumbridge.ui.common.composables.components.defaults.EmptyScreenWithButton
import com.eyther.lumbridge.ui.common.composables.components.loading.LoadingIndicator
import com.eyther.lumbridge.ui.common.composables.components.text.TabbedDataOverview
import com.eyther.lumbridge.ui.common.composables.components.text.TabbedTextAndIcon
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.LumbridgeTopAppBar
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.TopAppBarVariation
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.HalfPadding
import com.eyther.lumbridge.ui.theme.QuarterPadding
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
    val reminderToDelete = remember { mutableLongStateOf(-1L) }

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
                .imePadding()
                .then(
                    if (reminderToDelete.longValue >= 0L) Modifier.blur(5.dp) else Modifier
                )
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
                    Content(
                        state = state,
                        navController = navController,
                        reminderToDelete = reminderToDelete,
                        onDeleteReminder = viewModel::deleteReminder,
                        onEdit = { navController.navigateToWithArgs(ToolsNavigationItem.Reminders.RemindersDetails, it.reminderId) }
                    )
                }
            }
        }
    }
}

@Composable
private fun Content(
    state: RemindersOverviewScreenViewState.Content,
    navController: NavHostController,
    reminderToDelete: MutableLongState,
    onDeleteReminder: (Long) -> Unit,
    onEdit: (ReminderUi) -> Unit
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = DefaultPadding)
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(HalfPadding)
        ) {
            itemsIndexed(state.reminders) { index, reminder ->
                ReminderItem(
                    reminder = reminder,
                    reminderToDelete = reminderToDelete,
                    onEdit = onEdit,
                    context = context
                )

                if (index == state.reminders.lastIndex) {
                    Spacer(modifier = Modifier.height(DefaultPadding * 2 + 56.0.dp)) // 56dp is the height of the FAB
                }
            }
        }

        AddFab(
            modifier = Modifier.align(Alignment.BottomEnd),
            navController = navController
        )

        ShowDeleteConfirmationDialog(
            reminderUi = state.reminders.find { it.reminderId == reminderToDelete.longValue },
            reminderToDelete = reminderToDelete,
            onDeleteReminder = onDeleteReminder
        )
    }
}

@Composable
private fun ReminderItem(
    reminder: ReminderUi,
    context: Context,
    reminderToDelete: MutableLongState,
    onEdit: (ReminderUi) -> Unit
) {
    ColumnCardWrapper {
        TabbedTextAndIcon(
            modifier = Modifier.padding(end = QuarterPadding),
            text = reminder.label,
            textStyle = MaterialTheme.typography.bodyLarge,
            textColour = MaterialTheme.colorScheme.tertiary,
            icons = {
                Icon(
                    modifier = Modifier
                        .size(20.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(bounded = false),
                            onClick = { reminderToDelete.longValue = reminder.reminderId }
                        ),
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = stringResource(id = R.string.delete)
                )

                if (!reminder.noLongerRelevant()) {
                    Spacer(Modifier.width(QuarterPadding))

                    Icon(
                        modifier = Modifier
                            .size(20.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(bounded = false),
                                onClick = { onEdit(reminder) }
                            ),
                        painter = painterResource(id = R.drawable.ic_edit),
                        contentDescription = stringResource(id = R.string.edit)
                    )
                }
            }
        )

        TabbedDataOverview(
            modifier = Modifier
                .padding(top = HalfPadding)
                .alpha(if (reminder.noLongerRelevant()) 0.5f else 1f),
            label = stringResource(id = R.string.tools_reminders_due_date),
            text = reminder.dueDate.toDayMonthYearHourMinuteString()
        )

        TabbedDataOverview(
            modifier = Modifier
                .alpha(if (reminder.noLongerRelevant()) 0.5f else 1f),
            label = stringResource(id = R.string.tools_reminders_remind_me_in),
            text = reminder.remindMeIn.getPeriodicityHumanReadable().getString(context)
        )

        if (reminder.isInThePast()) {
            TabbedDataOverview(
                modifier = Modifier
                    .padding(top = HalfPadding)
                    .alpha(if (reminder.noLongerRelevant()) 0.5f else 1f),
                label = stringResource(R.string.tools_reminders_no_longer_relevant),
                text = stringResource(R.string.yes),
                icon = {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = Icons.Outlined.CheckCircle,
                        contentDescription = null
                    )
                }
            )
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

@Composable
private fun AddFab(
    modifier: Modifier,
    navController: NavHostController
) {
    FloatingActionButton(
        modifier = modifier.then(
            Modifier.padding(DefaultPadding)
        ),
        onClick = {
            navController.navigateToWithArgs(ToolsNavigationItem.Reminders.RemindersDetails, -1L)
        }
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_add),
            contentDescription = stringResource(
                id = R.string.tools_reminders_add_reminder
            )
        )
    }
}

@Composable
private fun ShowDeleteConfirmationDialog(
    reminderUi: ReminderUi?,
    reminderToDelete: MutableLongState,
    onDeleteReminder: (Long) -> Unit
) {
    if (reminderToDelete.longValue >= 0L && reminderUi != null) {
        AlertDialog(
            onDismissRequest = { reminderToDelete.longValue = -1L },
            confirmButton = {
                LumbridgeButton(
                    label = stringResource(id = R.string.yes),
                    onClick = {
                        onDeleteReminder(reminderUi.reminderId)
                        reminderToDelete.longValue = -1L
                    }
                )
            },
            dismissButton = {
                LumbridgeButton(
                    label = stringResource(id = R.string.no),
                    onClick = { reminderToDelete.longValue = -1L }
                )
            },
            title = {
                Text(
                    text = stringResource(id = R.string.delete),
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            text = {
                Text(
                    text = stringResource(id = R.string.tools_reminders_list_delete_confirmation, reminderUi.label),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        )
    }
}
