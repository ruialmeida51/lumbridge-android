package com.eyther.lumbridge.features.profile.editloans.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableLongState
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
import com.eyther.lumbridge.extensions.platform.navigateToWithArgs
import com.eyther.lumbridge.features.profile.editloans.model.EditLoansViewState
import com.eyther.lumbridge.features.profile.editloans.viewmodel.EditLoansViewModel
import com.eyther.lumbridge.features.profile.editloans.viewmodel.IEditLoansViewModel
import com.eyther.lumbridge.features.profile.navigation.ProfileNavigationItem
import com.eyther.lumbridge.model.loan.LoanCalculationUi
import com.eyther.lumbridge.model.loan.LoanUi
import com.eyther.lumbridge.ui.common.composables.components.buttons.LumbridgeButton
import com.eyther.lumbridge.ui.common.composables.components.defaults.EmptyScreenWithButton
import com.eyther.lumbridge.ui.common.composables.components.loading.LoadingIndicator
import com.eyther.lumbridge.ui.common.composables.components.loan.PeekLoanCard
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.LumbridgeTopAppBar
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.TopAppBarVariation
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.HalfPadding

@Composable
fun EditLoansListScreen(
    navController: NavHostController,
    @StringRes label: Int,
    viewModel: IEditLoansViewModel = hiltViewModel<EditLoansViewModel>()
) {
    val viewState = viewModel.viewState.collectAsStateWithLifecycle().value
    val loanToDelete = remember { mutableLongStateOf(-1L) }

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
                    if (loanToDelete.longValue >= 0L) Modifier.blur(5.dp) else Modifier
                )
        ) {
            when (viewState) {
                is EditLoansViewState.Content -> Content(
                    loans = viewState.loansUi,
                    currencySymbol = viewState.currencySymbol,
                    navController = navController,
                    loanToDelete = loanToDelete,
                    onDeleteLoan = viewModel::onDeleteLoan
                )

                is EditLoansViewState.Empty -> EmptyScreen(
                    onCreateLoan = { navController.navigateToWithArgs(ProfileNavigationItem.Loans.Edit, -1L) }
                )

                is EditLoansViewState.Loading -> LoadingIndicator()
            }
        }
    }
}

@Composable
private fun Content(
    loans: List<Pair<LoanUi, LoanCalculationUi>>,
    currencySymbol: String,
    navController: NavHostController,
    loanToDelete: MutableLongState,
    onDeleteLoan: (LoanUi) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            Modifier.padding(top = DefaultPadding)
        ) {
            item {
                Text(
                    modifier = Modifier
                        .padding(start = DefaultPadding, end = DefaultPadding, bottom = HalfPadding),
                    text = stringResource(id = R.string.breakdown_loans_title),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            items(loans) { (loanUi, loanCalculationUi) ->
                PeekLoanCard(
                    loanUi = loanUi,
                    loanCalculationUi = loanCalculationUi,
                    currencySymbol = currencySymbol,
                    onDelete = { loanToDelete.longValue = it },
                    onCardClick = { navController.navigateToWithArgs(ProfileNavigationItem.Loans.Edit, loanUi.id) }
                )

                Spacer(modifier = Modifier.height(DefaultPadding))
            }

            item {
                Spacer(modifier = Modifier.height(DefaultPadding + 56.dp)) // 56dp is the height of the FAB
            }
        }

        if (loans.isNotEmpty()) {
            AddFab(
                modifier = Modifier.align(Alignment.BottomEnd),
                navController = navController
            )
        }

        ShowDeleteConfirmationDialog(
            loanUi = loans.find { it.first.id == loanToDelete.longValue }?.first,
            loanToDelete = loanToDelete,
            onDeleteLoan = onDeleteLoan
        )
    }
}

@Composable
private fun EmptyScreen(
    onCreateLoan: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        EmptyScreenWithButton(
            modifier = Modifier.padding(DefaultPadding),
            text = stringResource(id = R.string.breakdown_loans_no_loans),
            buttonText = stringResource(id = R.string.financial_overview_create_loan),
            icon = {
                Icon(
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(id = R.drawable.ic_bank),
                    contentDescription = stringResource(id = R.string.breakdown_loans_no_loans_create)
                )
            },
            onButtonClick = onCreateLoan
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
            navController.navigateToWithArgs(ProfileNavigationItem.Loans.Edit, -1L)
        }
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_add),
            contentDescription = stringResource(
                id = R.string.tools_shopping_list
            )
        )
    }
}

@Composable
private fun ShowDeleteConfirmationDialog(
    loanUi: LoanUi?,
    loanToDelete: MutableLongState,
    onDeleteLoan: (LoanUi) -> Unit
) {
    if (loanToDelete.longValue >= 0L && loanUi != null) {
        AlertDialog(
            onDismissRequest = { loanToDelete.longValue = -1L },
            confirmButton = {
                LumbridgeButton(
                    label = stringResource(id = R.string.yes),
                    onClick = {
                        onDeleteLoan(loanUi)
                        loanToDelete.longValue = -1L
                    }
                )
            },
            dismissButton = {
                LumbridgeButton(
                    label = stringResource(id = R.string.no),
                    onClick = { loanToDelete.longValue = -1L }
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
                    text = stringResource(id = R.string.breakdown_delete_loan_confirmation_message, loanUi.name),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        )
    }
}

