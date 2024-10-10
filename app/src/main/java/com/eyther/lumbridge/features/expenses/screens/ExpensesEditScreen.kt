package com.eyther.lumbridge.features.expenses.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.eyther.lumbridge.R
import com.eyther.lumbridge.features.expenses.model.edit.ExpensesEditScreenViewEffect
import com.eyther.lumbridge.features.expenses.model.edit.ExpensesEditScreenViewState
import com.eyther.lumbridge.features.expenses.viewmodel.edit.ExpensesEditScreenViewModel
import com.eyther.lumbridge.features.expenses.viewmodel.edit.IExpensesEditScreenViewModel
import com.eyther.lumbridge.ui.common.composables.components.buttons.LumbridgeButton
import com.eyther.lumbridge.ui.common.composables.components.card.ColumnCardWrapper
import com.eyther.lumbridge.ui.common.composables.components.input.DropdownInput
import com.eyther.lumbridge.ui.common.composables.components.input.NumberInput
import com.eyther.lumbridge.ui.common.composables.components.input.TextInput
import com.eyther.lumbridge.ui.common.composables.components.loading.LoadingIndicator
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.LumbridgeTopAppBar
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.TopAppBarVariation
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.HalfPadding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun ExpensesEditScreen(
    navController: NavHostController,
    @StringRes label: Int,
    viewModel: IExpensesEditScreenViewModel = hiltViewModel<ExpensesEditScreenViewModel>()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val lifecycleOwner = LocalLifecycleOwner.current
    val state = viewModel.viewState.collectAsStateWithLifecycle().value
    val shouldShowDialog = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.viewEffects
                .onEach { viewEffects ->
                    when (viewEffects) {
                        is ExpensesEditScreenViewEffect.ShowError -> {
                            snackbarHostState.showSnackbar(
                                message = viewEffects.message,
                                duration = SnackbarDuration.Short
                            )
                        }

                        is ExpensesEditScreenViewEffect.Finish -> {
                            navController.popBackStack()
                        }
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
                    onIconClick = { navController.popBackStack() }
                )
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
                .padding(vertical = DefaultPadding)
                .then(
                    if (shouldShowDialog.value) Modifier.blur(5.dp) else Modifier
                )
        ) {
            when (state) {
                is ExpensesEditScreenViewState.Loading -> {
                    LoadingIndicator()
                }

                is ExpensesEditScreenViewState.Content -> {
                    Content(
                        state = state,
                        viewModel = viewModel,
                        shouldShowDialog = shouldShowDialog
                    )
                }
            }
        }
    }
}

@Composable
private fun ColumnScope.Content(
    state: ExpensesEditScreenViewState.Content,
    viewModel: IExpensesEditScreenViewModel,
    shouldShowDialog: MutableState<Boolean>
) {

    Text(
        modifier = Modifier
            .padding(start = DefaultPadding, end = DefaultPadding, bottom = HalfPadding)
            .align(Alignment.Start),
        text = stringResource(id = R.string.expenses_edit_hint),
        style = MaterialTheme.typography.bodyLarge
    )

    ColumnCardWrapper {
        DropdownInput(
            label = stringResource(id = R.string.expenses_add_type),
            selectedOption = stringResource(state.inputState.categoryType.categoryRes),
            items = state.availableCategories.map { it.ordinal.toString() to stringResource(it.categoryRes) },
            onItemClick = { ordinal, _ -> viewModel.onTypeChanged(ordinal.toIntOrNull()) }
        )

        Spacer(modifier = Modifier.height(DefaultPadding))

        TextInput(
            modifier = Modifier.padding(bottom = DefaultPadding),
            state = state.inputState.expenseName,
            label = stringResource(id = R.string.name),
            onInputChanged = viewModel::onExpenseNameChanged,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            )
        )

        NumberInput(
            label = stringResource(id = R.string.spent),
            placeholder = "0",
            state = state.inputState.expenseAmount,
            onInputChanged = { input -> viewModel.onExpenseAmountChanged(input.toFloatOrNull()) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            )
        )

        Spacer(modifier = Modifier.padding(top = DefaultPadding))

        LumbridgeButton(
            label = stringResource(id = R.string.save),
            enableButton = state.shouldEnableSaveButton,
            onClick = viewModel::save
        )

        Spacer(modifier = Modifier.padding(top = DefaultPadding))

        LumbridgeButton(
            label = stringResource(id = R.string.delete),
            enableButton = true,
            onClick = { shouldShowDialog.value = true }
        )
    }

    if (shouldShowDialog.value) {
        AlertDialog(
            onDismissRequest = { shouldShowDialog.value = false },
            confirmButton = {
                LumbridgeButton(
                    label = stringResource(id = R.string.yes),
                    onClick = {
                        viewModel.delete()
                        shouldShowDialog.value = false
                    }
                )
            },
            dismissButton = {
                LumbridgeButton(
                    label = stringResource(id = R.string.no),
                    onClick = { shouldShowDialog.value = false }
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
                    text = stringResource(id = R.string.expense_delete_confirmation),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        )
    }
}
