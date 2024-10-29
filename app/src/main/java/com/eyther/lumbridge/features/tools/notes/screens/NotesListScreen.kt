package com.eyther.lumbridge.features.tools.notes.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.eyther.lumbridge.R
import com.eyther.lumbridge.extensions.platform.navigateWithArgs
import com.eyther.lumbridge.features.tools.notes.model.list.NotesListScreenViewState
import com.eyther.lumbridge.features.tools.notes.viewmodel.list.INotesListScreenViewModel
import com.eyther.lumbridge.features.tools.notes.viewmodel.list.NotesListScreenViewModel
import com.eyther.lumbridge.features.tools.overview.navigation.ToolsNavigationItem
import com.eyther.lumbridge.model.notes.NoteUi
import com.eyther.lumbridge.ui.common.composables.components.buttons.LumbridgeButton
import com.eyther.lumbridge.ui.common.composables.components.card.PeekContentCard
import com.eyther.lumbridge.ui.common.composables.components.defaults.EmptyScreenWithButton
import com.eyther.lumbridge.ui.common.composables.components.loading.LoadingIndicator
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.LumbridgeTopAppBar
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.TopAppBarVariation
import com.eyther.lumbridge.ui.common.composables.model.card.PeekContentCardType
import com.eyther.lumbridge.ui.theme.DefaultPadding

@Composable
fun NotesListScreen(
    navController: NavHostController,
    @StringRes label: Int,
    viewModel: INotesListScreenViewModel = hiltViewModel<NotesListScreenViewModel>()
) {
    val state = viewModel.viewState.collectAsStateWithLifecycle().value
    val notesToDelete = remember { mutableLongStateOf(-1) }

    Scaffold(
        topBar = {
            LumbridgeTopAppBar(
                topAppBarVariation = TopAppBarVariation.TitleAndIcon(
                    title = stringResource(id = label),
                    onIconClick = { navController.popBackStack() }
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .then(
                    if (notesToDelete.longValue >= 0L) Modifier.blur(5.dp) else Modifier
                )
        ) {
            when (state) {
                is NotesListScreenViewState.Loading -> {
                    LoadingIndicator()
                }

                is NotesListScreenViewState.Empty -> {
                    EmptyScreen {
                        navController.navigateWithArgs(ToolsNavigationItem.Notes.NotesDetails, -1L)
                    }
                }

                is NotesListScreenViewState.Content -> {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Content(
                            modifier = Modifier.fillMaxSize(),
                            notesList = state.notesList,
                            navController = navController,
                            onDelete = viewModel::onDeleteNote,
                            notesToDelete = notesToDelete
                        )

                        AddFab(
                            modifier = Modifier.align(Alignment.BottomEnd),
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Content(
    modifier: Modifier,
    notesList: List<NoteUi>,
    navController: NavHostController,
    notesToDelete: MutableState<Long>,
    onDelete: (NoteUi) -> Unit
) {
    BoxWithConstraints(modifier = modifier) {
        val boxWithConstraintsScope = this

        LazyVerticalGrid(
            modifier = Modifier.padding(start = DefaultPadding, end = DefaultPadding, top = DefaultPadding),
            columns = GridCells.Adaptive((boxWithConstraintsScope.maxWidth - DefaultPadding * 3) / 2),
            horizontalArrangement = Arrangement.spacedBy(DefaultPadding),
            verticalArrangement = Arrangement.spacedBy(DefaultPadding)
        ) {
            items(notesList) { notesListItem ->
                PeekContentCard(
                    modifier = Modifier
                        .sizeIn(
                            minHeight = boxWithConstraintsScope.maxHeight / 6
                        ),
                    title = notesListItem.title,
                    content = notesListItem.getPreviewEntries().map { it },
                    actions = {
                        Icon(
                            modifier = Modifier
                                .size(20.dp)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = ripple(bounded = false),
                                    onClick = { notesToDelete.value = notesListItem.id }
                                ),
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = stringResource(id = R.string.delete)
                        )
                    },
                    peekContentCardType = PeekContentCardType.PlainText,
                    onClick = { navController.navigateWithArgs(ToolsNavigationItem.Notes.NotesDetails, notesListItem.id) }
                )
            }

            item {
                Spacer(modifier = Modifier.size(DefaultPadding + 56.dp)) // 56dp is the height of the FAB
            }
        }
    }

    ShowDeleteConfirmationDialog(
        noteUi = notesList.find { it.id == notesToDelete.value },
        selectedNotesList = notesToDelete,
        onDeleteNotes = onDelete
    )
}

@Composable
private fun ShowDeleteConfirmationDialog(
    noteUi: NoteUi?,
    selectedNotesList: MutableState<Long>,
    onDeleteNotes: (NoteUi) -> Unit
) {
    if (selectedNotesList.value >= 0L && noteUi != null) {
        AlertDialog(
            onDismissRequest = { selectedNotesList.value = -1L },
            confirmButton = {
                LumbridgeButton(
                    label = stringResource(id = R.string.yes),
                    onClick = {
                        onDeleteNotes(noteUi)
                        selectedNotesList.value = -1L
                    }
                )
            },
            dismissButton = {
                LumbridgeButton(
                    label = stringResource(id = R.string.no),
                    onClick = { selectedNotesList.value = -1L }
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
                    text = stringResource(id = R.string.tools_notes_list_delete_confirmation, noteUi.title),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        )
    }
}

@Composable
private fun EmptyScreen(
    onCreateNotes: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        EmptyScreenWithButton(
            modifier = Modifier.padding(DefaultPadding),
            text = stringResource(id = R.string.tools_notes_no_list_message),
            buttonText = stringResource(id = R.string.tools_notes_no_list_create),
            icon = {
                Icon(
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(id = R.drawable.ic_sticky_note),
                    contentDescription = stringResource(id = R.string.tools_notes_list)
                )
            },
            onButtonClick = onCreateNotes
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
            navController.navigateWithArgs(ToolsNavigationItem.Notes.NotesDetails, -1L)
        }
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_add),
            contentDescription = stringResource(
                id = R.string.tools_notes_add_note
            )
        )
    }
}

