@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.eyther.lumbridge.features.expenses.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.eyther.lumbridge.R
import com.eyther.lumbridge.features.expenses.model.add.ExpensesAddScreenViewEffect
import com.eyther.lumbridge.features.expenses.model.add.ExpensesAddScreenViewState
import com.eyther.lumbridge.features.expenses.model.add.ExpensesAddSurplusOrExpenseChoice
import com.eyther.lumbridge.features.expenses.viewmodel.add.ExpensesAddScreenViewModel
import com.eyther.lumbridge.features.expenses.viewmodel.add.IExpensesAddScreenViewModel
import com.eyther.lumbridge.ui.common.composables.components.buttons.ChoiceTab
import com.eyther.lumbridge.ui.common.composables.components.buttons.LumbridgeButton
import com.eyther.lumbridge.ui.common.composables.components.card.ColumnCardWrapper
import com.eyther.lumbridge.ui.common.composables.components.datepicker.LumbridgeDatePickerDialog
import com.eyther.lumbridge.ui.common.composables.components.input.DateInput
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
fun ExpensesAddScreen(
    navController: NavHostController,
    @StringRes label: Int,
    viewModel: IExpensesAddScreenViewModel = hiltViewModel<ExpensesAddScreenViewModel>()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    val state = viewModel.viewState.collectAsStateWithLifecycle().value

    LaunchedEffect(Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.viewEffects
                .onEach { viewEffects ->
                    when (viewEffects) {
                        is ExpensesAddScreenViewEffect.ShowError -> {
                            snackbarHostState.showSnackbar(
                                message = viewEffects.message,
                                duration = SnackbarDuration.Short
                            )
                        }

                        is ExpensesAddScreenViewEffect.Finish -> {
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
                .verticalScroll(rememberScrollState())
        ) {
            when (state) {
                is ExpensesAddScreenViewState.Loading -> {
                    LoadingIndicator()
                }

                is ExpensesAddScreenViewState.Content -> {
                    Content(
                        state = state,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
private fun ColumnScope.Content(
    state: ExpensesAddScreenViewState.Content,
    viewModel: IExpensesAddScreenViewModel
) {
    val selectableYears = viewModel.getMinSelectableYear()..viewModel.getMaxSelectableYear()
    val isSelectableYear = { year: Int -> year in selectableYears }

    val showDateDialog = remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        yearRange = selectableYears,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return true
            }

            override fun isSelectableYear(year: Int): Boolean {
                return isSelectableYear(year)
            }
        }
    )

    Spacer(modifier = Modifier.height(DefaultPadding))

    Text(
        modifier = Modifier
            .padding(start = DefaultPadding, end = DefaultPadding, bottom = HalfPadding)
            .align(Alignment.Start),
        text = stringResource(id = R.string.expenses_add_hint),
        style = MaterialTheme.typography.bodyLarge
    )

    ColumnCardWrapper {
        ChoiceTab(
            modifier = Modifier.padding(bottom = DefaultPadding),
            choiceTabState = state.inputState.surplusOrExpenseChoice,
            onOptionClicked = { viewModel.onSurplusOrExpenseChanged(it)}
        )

        DateInput(
            modifier = Modifier.fillMaxWidth(),
            state = state.inputState.dateInput,
            label = stringResource(id = R.string.date),
            placeholder = stringResource(id = R.string.edit_loan_profile_invalid_start_date),
            onClick = { showDateDialog.value = true }
        )

        LumbridgeDatePickerDialog(
            showDialog = showDateDialog,
            datePickerState = datePickerState,
            onSaveDate = { viewModel.onDateChanged(it) }
        )

        if (!state.inputState.isSurplusSelected) {
            DropdownInput(
                label = stringResource(id = R.string.expenses_add_type),
                selectedOption = stringResource(state.inputState.categoryType.categoryRes),
                items = state.availableCategories.map { it.ordinal.toString() to stringResource(it.categoryRes) },
                onItemClick = { ordinal, _ -> viewModel.onTypeChanged(ordinal.toIntOrNull()) }
            )
        }

        TextInput(
            state = state.inputState.nameInput,
            label = stringResource(id = R.string.name),
            onInputChanged = viewModel::onNameChanged,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            )
        )

        NumberInput(
            label = stringResource(id = if (state.inputState.isSurplusSelected) R.string.gained else R.string.spent),
            placeholder = "0",
            state = state.inputState.amountInput,
            onInputChanged = { input -> viewModel.onAmountChanged(input.toFloatOrNull()) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            )
        )

        Spacer(modifier = Modifier.padding(top = DefaultPadding))

        LumbridgeButton(
            label = stringResource(id = R.string.save),
            enableButton = state.shouldEnableSaveButton,
            onClick = viewModel::onAddExpense
        )
    }
}
