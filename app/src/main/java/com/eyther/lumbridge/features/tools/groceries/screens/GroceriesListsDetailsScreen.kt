package com.eyther.lumbridge.features.tools.groceries.screens

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
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.eyther.lumbridge.R
import com.eyther.lumbridge.features.tools.groceries.model.details.GroceriesListDetailsScreenViewState
import com.eyther.lumbridge.features.tools.groceries.viewmodel.details.GroceriesListDetailsScreenViewModel
import com.eyther.lumbridge.features.tools.groceries.viewmodel.details.IGroceriesListDetailsScreenViewModel
import com.eyther.lumbridge.ui.common.composables.components.input.BasicTextInput
import com.eyther.lumbridge.ui.common.composables.components.loading.LoadingIndicator
import com.eyther.lumbridge.ui.common.composables.components.setting.MovementSetting
import com.eyther.lumbridge.ui.common.composables.components.setting.SwitchSetting
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.LumbridgeTopAppBar
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.TopAppBarVariation
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.QuarterPadding
import kotlinx.coroutines.launch

@Composable
fun GroceriesListDetailsScreen(
    navController: NavHostController,
    @StringRes label: Int,
    viewModel: IGroceriesListDetailsScreenViewModel = hiltViewModel<GroceriesListDetailsScreenViewModel>()
) {
    val shouldShowDeleteGroceriesListDialog = remember { mutableStateOf(false) }
    val state = viewModel.viewState.collectAsStateWithLifecycle().value

    Scaffold(
        topBar = {
            LumbridgeTopAppBar(
                TopAppBarVariation.TitleAndIcon(
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
                    if (shouldShowDeleteGroceriesListDialog.value) Modifier.blur(5.dp) else Modifier
                )
        ) {
            when (state) {
                is GroceriesListDetailsScreenViewState.Loading -> LoadingIndicator()
                is GroceriesListDetailsScreenViewState.Content -> GroceriesList(
                    state = state,
                    onShowTickedItemsChange = viewModel::onShowTickedItemsChanged,
                    onTitleChanged = viewModel::onTitleChanged,
                    onItemSelected = viewModel::onEntrySelected,
                    onTextUpdated = viewModel::onEntryTextUpdated,
                    onNextKeyboardAction = viewModel::onKeyboardNext,
                    onDeleteKeyboardAction = viewModel::onKeyboardDelete,
                    onClearItem = viewModel::onClearItem
                )
            }
        }
    }
}

@Composable
private fun GroceriesList(
    state: GroceriesListDetailsScreenViewState.Content,
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
        val defaultTitle = stringResource(id = R.string.tools_groceries_list)
        val listState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()

        SwitchSetting(
            icon = R.drawable.ic_check,
            label = stringResource(id = R.string.tools_groceries_list_show_ticked_items),
            isChecked = state.inputState.showTickedItems,
            onCheckedChange = { onShowTickedItemsChange(it) }
        )

        BasicTextInput(
            modifier = Modifier
                .fillMaxWidth()
                .padding(DefaultPadding),
            state = state.inputState.title,
            defaultInitialValue = defaultTitle,
            onInputChanged = { title -> onTitleChanged(title) },
            textStyle = MaterialTheme.typography.titleSmall,
            singleLine = true
        )

        LazyColumn(
            state = listState
        ) {
            items(
                state.inputState.items.size,
                key = { index ->
                    state.inputState.items.getOrNull(index)?.let { (uuid, _, _) -> uuid } ?: index
                }
            ) { index ->
                val item = state.inputState.items.getOrNull(index) ?: return@items
                val (_, checkedState, textState) = item

                // Create a FocusRequester for each item.
                val focusRequester = remember { FocusRequester() }

                if (!state.inputState.showTickedItems && checkedState.checked) {
                    return@items
                }

                Row {
                    Checkbox(
                        checked = checkedState.checked,
                        onCheckedChange = { onItemSelected(index, it) }
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
                                    focusedIndex.intValue = index
                                }
                            }
                            .onKeyEvent { keyEvent ->
                                if (keyEvent.key == Key.Backspace) {
                                    onDeleteKeyboardAction(index)

                                    // If we're at the last index and there are items, focus the previous item.
                                    if (index == state.inputState.items.lastIndex && state.inputState.items.isNotEmpty()) {
                                        focusedIndex.intValue = index - 1
                                    }
                                }

                                false
                            },
                        state = textState,
                        onInputChanged = {
                            onTextUpdated(index, it)
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = {
                                onNextKeyboardAction(index)
                                // Move focus to the next item
                                focusedIndex.intValue = index + 1

                                // Scroll to the next item
                                coroutineScope.launch {
                                    listState.scrollToItem(index + 1)
                                }
                            },
                        ),
                        strikethrough = checkedState.checked
                    )

                    Spacer(modifier = Modifier.width(QuarterPadding))

                    // If the item is focused, show a clear icon and request focus.
                    if (focusedIndex.intValue == index) {
                        LaunchedEffect(Unit) {
                            focusRequester.requestFocus()
                        }

                        Icon(
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .size(16.dp)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = rememberRipple(bounded = false),
                                    onClick = {
                                        onClearItem(index)

                                        // Focus the previous item if the deleted item is not the first one
                                        if (index > 0) {
                                            focusedIndex.intValue = index - 1
                                        }
                                    }
                                ),
                            painter = painterResource(R.drawable.ic_clear),
                            contentDescription = stringResource(R.string.delete)
                        )
                    }
                }

            }
        }

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
                color = MaterialTheme.typography.bodyMedium.color.copy(alpha = 0.7f)
            )
        )
    }
}
