package com.eyther.lumbridge.features.tools.groceries.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.eyther.lumbridge.R
import com.eyther.lumbridge.extensions.platform.navigate
import com.eyther.lumbridge.extensions.platform.navigateWithArgs
import com.eyther.lumbridge.features.tools.groceries.model.list.GroceriesListsScreenViewState
import com.eyther.lumbridge.features.tools.groceries.viewmodel.list.GroceriesListsScreenViewModel
import com.eyther.lumbridge.features.tools.groceries.viewmodel.list.IGroceriesListScreenViewModel
import com.eyther.lumbridge.features.tools.overview.navigation.ToolsNavigationItem
import com.eyther.lumbridge.model.groceries.GroceriesListUi
import com.eyther.lumbridge.ui.common.composables.components.buttons.LumbridgeButton
import com.eyther.lumbridge.ui.common.composables.components.card.PeekContentCard
import com.eyther.lumbridge.ui.common.composables.components.defaults.EmptyScreenWithButton
import com.eyther.lumbridge.ui.common.composables.components.loading.LoadingIndicator
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.LumbridgeTopAppBar
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.TopAppBarVariation
import com.eyther.lumbridge.ui.common.composables.model.card.PeekContentCardType
import com.eyther.lumbridge.ui.theme.DefaultPadding

@Composable
fun GroceriesListsScreen(
    navController: NavHostController,
    @StringRes label: Int,
    viewModel: IGroceriesListScreenViewModel = hiltViewModel<GroceriesListsScreenViewModel>()
) {
    val state = viewModel.viewState.collectAsStateWithLifecycle().value
    val groceriesListToDelete = remember { mutableLongStateOf(-1) }

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
                    if (groceriesListToDelete.longValue >= 0L) Modifier.blur(5.dp) else Modifier
                )
        ) {
            when (state) {
                is GroceriesListsScreenViewState.Loading -> {
                    LoadingIndicator()
                }
                is GroceriesListsScreenViewState.Empty -> {
                    EmptyScreen {
                        navController.navigateWithArgs(ToolsNavigationItem.Groceries.GroceriesListDetails, -1L)
                    }
                }
                is GroceriesListsScreenViewState.Content -> {
                    Content(
                        groceriesList = state.groceriesList,
                        navController = navController,
                        onDelete = viewModel::onDeleteGroceryItem,
                        groceriesListToDelete = groceriesListToDelete
                    )
                }
            }
        }
    }
}

@Composable
private fun Content(
    groceriesList: List<GroceriesListUi>,
    navController: NavHostController,
    groceriesListToDelete: MutableState<Long>,
    onDelete: (GroceriesListUi) -> Unit
) {
    BoxWithConstraints {
        val boxWithConstraintsScope = this

        LazyVerticalGrid(
            modifier = Modifier.padding(start = DefaultPadding, end = DefaultPadding, top = DefaultPadding),
            columns = GridCells.Adaptive((boxWithConstraintsScope.maxWidth - DefaultPadding * 3) / 2),
            horizontalArrangement = Arrangement.spacedBy(DefaultPadding),
            verticalArrangement = Arrangement.spacedBy(DefaultPadding)
        ) {
            itemsIndexed(groceriesList) { index, groceriesListItem ->
                PeekContentCard(
                    modifier = Modifier.sizeIn(
                        minHeight = boxWithConstraintsScope.maxHeight / 6
                    ).then(
                        if (index == groceriesList.lastIndex) Modifier.padding(bottom = DefaultPadding) else Modifier
                    ),
                    title = groceriesListItem.title,
                    content = groceriesListItem.entries.map { it.text },
                    actions = {
                        Icon(
                            modifier = Modifier
                                .size(20.dp)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = rememberRipple(bounded = false),
                                    onClick = { groceriesListToDelete.value = groceriesListItem.id }
                                ),
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = stringResource(id = R.string.delete)
                        )
                    },
                    peekContentCardType = PeekContentCardType.Checkbox,
                    onClick = { navController.navigateWithArgs(ToolsNavigationItem.Groceries.GroceriesListDetails, groceriesListItem.id) }
                )
            }
        }
    }

    ShowDeleteConfirmationDialog(
        groceriesListUi = groceriesList.find { it.id == groceriesListToDelete.value },
        selectedGroceriesList = groceriesListToDelete,
        onDeleteExpense = onDelete
    )
}

@Composable
private fun ShowDeleteConfirmationDialog(
    groceriesListUi: GroceriesListUi?,
    selectedGroceriesList: MutableState<Long>,
    onDeleteExpense: (GroceriesListUi) -> Unit
) {
    if (selectedGroceriesList.value >= 0L && groceriesListUi != null) {
        AlertDialog(
            onDismissRequest = { selectedGroceriesList.value = -1L },
            confirmButton = {
                LumbridgeButton(
                    label = stringResource(id = R.string.yes),
                    onClick = {
                        onDeleteExpense(groceriesListUi)
                        selectedGroceriesList.value = -1L
                    }
                )
            },
            dismissButton = {
                LumbridgeButton(
                    label = stringResource(id = R.string.no),
                    onClick = { selectedGroceriesList.value = -1L }
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
                    text = stringResource(id = R.string.tools_groceries_list_delete_confirmation, groceriesListUi.title),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        )
    }
}

@Composable
private fun EmptyScreen(
    onCreateGroceriesList: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        EmptyScreenWithButton(
            modifier = Modifier.padding(DefaultPadding),
            text = stringResource(id = R.string.tools_groceries_no_list_message),
            buttonText = stringResource(id = R.string.tools_groceries_no_list_create),
            icon = {
                Icon(
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(id = R.drawable.ic_basket),
                    contentDescription = stringResource(id = R.string.tools_groceries_list)
                )
            },
            onButtonClick = onCreateGroceriesList
        )
    }
}