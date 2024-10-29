package com.eyther.lumbridge.features.tools.shopping.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.eyther.lumbridge.R
import com.eyther.lumbridge.features.tools.shopping.model.details.ShoppingListDetailsScreenViewEffect
import com.eyther.lumbridge.features.tools.shopping.model.details.ShoppingListDetailsScreenViewState
import com.eyther.lumbridge.features.tools.shopping.viewmodel.details.IShoppingListDetailsScreenViewModel
import com.eyther.lumbridge.features.tools.shopping.viewmodel.details.ShoppingListDetailsScreenViewModel
import com.eyther.lumbridge.ui.common.composables.components.buttons.LumbridgeButton
import com.eyther.lumbridge.ui.common.composables.components.input.BasicTextInput
import com.eyther.lumbridge.ui.common.composables.components.loading.LoadingIndicator
import com.eyther.lumbridge.ui.common.composables.components.setting.SwitchSetting
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.LumbridgeTopAppBar
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.TopAppBarVariation
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.QuarterPadding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@Composable
fun ShoppingListDetailsScreen(
    navController: NavHostController,
    @StringRes label: Int,
    viewModel: IShoppingListDetailsScreenViewModel = hiltViewModel<ShoppingListDetailsScreenViewModel>()
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val shouldShowDeleteShoppingListDialog = remember { mutableStateOf(false) }
    val state = viewModel.viewState.collectAsStateWithLifecycle().value
    val defaultTitle = stringResource(id = R.string.tools_shopping_list)

    LaunchedEffect(Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.viewEffects
                .onEach { viewEffects ->
                    when (viewEffects) {
                        is ShoppingListDetailsScreenViewEffect.NavigateBack -> navController.popBackStack()
                    }
                }
                .collect()
        }
    }

    LifecycleEventEffect(event = Lifecycle.Event.ON_PAUSE) {
        viewModel.saveShoppingList(defaultTitle)
    }

    Scaffold(
        topBar = {
            LumbridgeTopAppBar(
                TopAppBarVariation.TitleAndIcon(
                    title = stringResource(id = label),
                    onIconClick = { navController.popBackStack() },
                ),
                actions = {
                    Icon(
                        modifier = Modifier
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(bounded = false),
                                onClick = { shouldShowDeleteShoppingListDialog.value = true }
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
                .then(
                    if (shouldShowDeleteShoppingListDialog.value) Modifier.blur(5.dp) else Modifier
                )
        ) {
            when (state) {
                is ShoppingListDetailsScreenViewState.Loading -> LoadingIndicator()
                is ShoppingListDetailsScreenViewState.Content -> {
                    ShoppingList(
                        state = state,
                        defaultTitle = defaultTitle,
                        onShowTickedItemsChange = viewModel::onShowTickedItemsChanged,
                        onTitleChanged = viewModel::onTitleChanged,
                        onItemSelected = viewModel::onEntrySelected,
                        onTextUpdated = viewModel::onEntryTextUpdated,
                        onNextKeyboardAction = viewModel::onKeyboardNext,
                        onDeleteKeyboardAction = viewModel::onKeyboardDelete,
                        onClearItem = viewModel::onClearItem
                    )

                    if (shouldShowDeleteShoppingListDialog.value) {
                        ShowDeleteConfirmationDialog(
                            shouldShowDeleteShoppingListDialog = shouldShowDeleteShoppingListDialog,
                            listTitle = state.inputState.title.text.orEmpty().ifEmpty { defaultTitle },
                            onDeleteShoppingList = viewModel::onDeleteShoppingList
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ShoppingList(
    state: ShoppingListDetailsScreenViewState.Content,
    defaultTitle: String,
    onShowTickedItemsChange: (Boolean) -> Unit,
    onTitleChanged: (String) -> Unit,
    onItemSelected: (Int, Boolean) -> Unit,
    onTextUpdated: (Int, String) -> Unit,
    onNextKeyboardAction: (Int) -> Unit,
    onDeleteKeyboardAction: (Int) -> Boolean,
    onClearItem: (Int) -> Unit
) {
    Column(
        Modifier.padding(top = DefaultPadding, start = DefaultPadding, end = DefaultPadding)
    ) {
        val focusedIndex = rememberSaveable { mutableIntStateOf(-1) }
        val listState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()

        SwitchSetting(
            icon = R.drawable.ic_check,
            label = stringResource(id = R.string.tools_shopping_list_show_ticked_items),
            isChecked = state.inputState.showTickedItems,
            onCheckedChange = { onShowTickedItemsChange(it) }
        )

        BasicTextInput(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = DefaultPadding),
            state = state.inputState.title,
            defaultInitialValue = defaultTitle,
            onInputChanged = { title -> onTitleChanged(title) },
            textStyle = MaterialTheme.typography.titleSmall,
            singleLine = true
        )

        if (state.inputState.items.isEmpty()) {
            AddItemText(
                state = state,
                onNextKeyboardAction = onNextKeyboardAction,
                focusedIndex = focusedIndex
            )
        } else {
            LazyColumn(state = listState) {
                items(
                    count = state.inputState.items.size,
                    key = { index -> state.inputState.items.getOrNull(index)?.id ?: index }
                ) { listIndex ->
                    val item = state.inputState.items[listIndex]
                    val (_, checkedState, textState) = item

                    // Create a FocusRequester for each item.
                    val focusRequester = remember { FocusRequester() }

                    if ((!state.inputState.showTickedItems && !checkedState.checked) || state.inputState.showTickedItems) {
                        Row {
                            Checkbox(
                                checked = checkedState.checked,
                                onCheckedChange = { onItemSelected(listIndex, it) }
                            )

                            Spacer(modifier = Modifier.width(DefaultPadding))

                            BasicTextInput(
                                modifier = Modifier
                                    .weight(1f)
                                    .align(Alignment.CenterVertically)
                                    .focusRequester(focusRequester)
                                    .onFocusChanged { focusState ->
                                        if (focusState.isFocused) {
                                            // If this item is focused, update the focused index.
                                            focusedIndex.intValue = listIndex
                                        }
                                    }
                                    .onKeyEvent { keyEvent ->
                                        if (keyEvent.key == Key.Backspace) {
                                            onDeleteKeyboardAction(listIndex)

                                            // If we're at the last index and there are items, focus the previous item.
                                            if (listIndex == state.inputState.items.lastIndex && state.inputState.items.isNotEmpty()) {
                                                focusedIndex.intValue = listIndex - 1
                                            }
                                        }

                                        false
                                    },
                                state = textState,
                                onInputChanged = {
                                    onTextUpdated(listIndex, it)
                                },
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Next
                                ),
                                keyboardActions = KeyboardActions(
                                    onNext = {
                                        onNextKeyboardAction(listIndex)
                                        // Move focus to the next item
                                        focusedIndex.intValue = listIndex + 1

                                        // Scroll to the next item
                                        coroutineScope.launch {
                                            listState.scrollToItem(listIndex + 1)
                                        }
                                    },
                                ),
                                strikethrough = checkedState.checked
                            )

                            Spacer(modifier = Modifier.width(QuarterPadding))

                            // If the item is focused, show a clear icon and request focus.
                            if (focusedIndex.intValue == listIndex) {
                                LaunchedEffect(Unit) {
                                    focusRequester.requestFocus()
                                }

                                Icon(
                                    modifier = Modifier
                                        .align(Alignment.CenterVertically)
                                        .size(16.dp)
                                        .clickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = ripple(bounded = false),
                                            onClick = {
                                                onClearItem(listIndex)

                                                // Focus the previous item if the deleted item is not the first one
                                                if (listIndex > 0) {
                                                    focusedIndex.intValue = listIndex - 1
                                                }
                                            }
                                        ),
                                    painter = painterResource(R.drawable.ic_clear),
                                    contentDescription = stringResource(R.string.delete)
                                )
                            }
                        }
                    }

                    if (listIndex == state.inputState.items.lastIndex) {
                        AddItemText(
                            state = state,
                            onNextKeyboardAction = onNextKeyboardAction,
                            focusedIndex = focusedIndex
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AddItemText(
    state: ShoppingListDetailsScreenViewState.Content,
    onNextKeyboardAction: (Int) -> Unit,
    focusedIndex: MutableIntState
) {
    Text(
        modifier = Modifier
            .clickable {
                val currentLastIndex = state.inputState.items.lastIndex
                onNextKeyboardAction(currentLastIndex)
                focusedIndex.intValue = currentLastIndex + 1
            }
            .fillMaxWidth()
            .padding(DefaultPadding),
        text = "+ ${stringResource(id = R.string.add_item)}",
        style = MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        ),
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
private fun ShowDeleteConfirmationDialog(
    shouldShowDeleteShoppingListDialog: MutableState<Boolean>,
    listTitle: String,
    onDeleteShoppingList: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { shouldShowDeleteShoppingListDialog.value = false },
        confirmButton = {
            LumbridgeButton(
                label = stringResource(id = R.string.yes),
                onClick = {
                    onDeleteShoppingList()
                    shouldShowDeleteShoppingListDialog.value = false
                }
            )
        },
        dismissButton = {
            LumbridgeButton(
                label = stringResource(id = R.string.no),
                onClick = { shouldShowDeleteShoppingListDialog.value = false }
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
                text = stringResource(id = R.string.tools_shopping_list_delete_confirmation, listTitle),
                style = MaterialTheme.typography.bodySmall
            )
        }
    )
}
