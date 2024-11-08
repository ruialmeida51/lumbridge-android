package com.eyther.lumbridge.features.tools.notes.screens

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.eyther.lumbridge.R
import com.eyther.lumbridge.features.tools.notes.model.details.NoteDetailsScreenViewEffect
import com.eyther.lumbridge.features.tools.notes.model.details.NoteDetailsScreenViewState
import com.eyther.lumbridge.features.tools.notes.viewmodel.details.INoteDetailsScreenViewModel
import com.eyther.lumbridge.features.tools.notes.viewmodel.details.NoteDetailsScreenViewModel
import com.eyther.lumbridge.ui.common.composables.components.buttons.LumbridgeButton
import com.eyther.lumbridge.ui.common.composables.components.input.BasicOutlinedTextInput
import com.eyther.lumbridge.ui.common.composables.components.input.BasicTextInput
import com.eyther.lumbridge.ui.common.composables.components.loading.LoadingIndicator
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.LumbridgeTopAppBar
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.TopAppBarVariation
import com.eyther.lumbridge.ui.theme.DefaultPadding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NoteDetailsScreen(
    navController: NavHostController,
    @StringRes label: Int,
    viewModel: INoteDetailsScreenViewModel = hiltViewModel<NoteDetailsScreenViewModel>()
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val shouldShowDeleteNoteDialog = remember { mutableStateOf(false) }
    val state = viewModel.viewState.collectAsStateWithLifecycle().value
    val defaultTitle = stringResource(id = R.string.tools_notes_details)

    BackHandler {
        viewModel.saveNotes()
    }

    LaunchedEffect(defaultTitle) {
        viewModel.setDefaultTitle(defaultTitle)
    }

    LaunchedEffect(Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.viewEffects
                .onEach { viewEffects ->
                    when (viewEffects) {
                        is NoteDetailsScreenViewEffect.NavigateBack -> navController.popBackStack()
                    }
                }
                .collect()
        }
    }

    Scaffold(
        topBar = {
            LumbridgeTopAppBar(
                TopAppBarVariation.TitleAndIcon(
                    title = stringResource(id = label),
                    onIconClick = { viewModel.saveNotes() },
                ),
                actions = {
                    Icon(
                        modifier = Modifier
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(bounded = false),
                                onClick = { shouldShowDeleteNoteDialog.value = true }
                            ),
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = stringResource(id = R.string.delete)
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .imePadding()
                .then(
                    if (shouldShowDeleteNoteDialog.value) Modifier.blur(5.dp) else Modifier
                )
        ) {
            when (state) {
                is NoteDetailsScreenViewState.Loading -> LoadingIndicator()
                is NoteDetailsScreenViewState.Content -> {
                    WriteNote(
                        state = state,
                        defaultTitle = defaultTitle,
                        onTitleChanged = viewModel::onTitleChanged,
                        onTextUpdated = viewModel::onTextUpdated
                    )

                    if (shouldShowDeleteNoteDialog.value) {
                        ShowDeleteConfirmationDialog(
                            shouldShowDeleteNoteDialog = shouldShowDeleteNoteDialog,
                            listTitle = state.inputState.title.text.orEmpty().ifEmpty { defaultTitle },
                            onDeleteNote = viewModel::onDeleteNote
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WriteNote(
    state: NoteDetailsScreenViewState.Content,
    defaultTitle: String,
    onTitleChanged: (String) -> Unit,
    onTextUpdated: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(DefaultPadding)
    ) {
        BasicTextInput(
            modifier = Modifier
                .fillMaxWidth(),
            state = state.inputState.title,
            defaultInitialValue = defaultTitle,
            onInputChanged = { cursorPos, title -> onTitleChanged(title) },
            textStyle = MaterialTheme.typography.titleSmall,
            singleLine = true
        )

        Spacer(modifier = Modifier.height(DefaultPadding))

        BasicOutlinedTextInput(
            modifier = Modifier
                .fillMaxSize()
                .imePadding(),
            state = state.inputState.text,
            onInputChanged = { text -> onTextUpdated(text) },
            textStyle = MaterialTheme.typography.bodyMedium,
            maxLength = Int.MAX_VALUE
        )
    }

}

@Composable
private fun ShowDeleteConfirmationDialog(
    shouldShowDeleteNoteDialog: MutableState<Boolean>,
    listTitle: String,
    onDeleteNote: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { shouldShowDeleteNoteDialog.value = false },
        confirmButton = {
            LumbridgeButton(
                label = stringResource(id = R.string.yes),
                onClick = {
                    onDeleteNote()
                    shouldShowDeleteNoteDialog.value = false
                }
            )
        },
        dismissButton = {
            LumbridgeButton(
                label = stringResource(id = R.string.no),
                onClick = { shouldShowDeleteNoteDialog.value = false }
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
                text = stringResource(id = R.string.tools_notes_list_delete_confirmation, listTitle),
                style = MaterialTheme.typography.bodySmall
            )
        }
    )
}

