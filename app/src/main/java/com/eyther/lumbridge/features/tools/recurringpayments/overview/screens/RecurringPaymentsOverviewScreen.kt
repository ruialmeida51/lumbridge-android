package com.eyther.lumbridge.features.tools.recurringpayments.overview.screens

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.eyther.lumbridge.R
import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.extensions.kotlin.forceTwoDecimalsPlaces
import com.eyther.lumbridge.extensions.platform.navigateToWithArgs
import com.eyther.lumbridge.features.tools.navigation.ToolsNavigationItem
import com.eyther.lumbridge.features.tools.recurringpayments.overview.model.RecurringPaymentsOverviewScreenViewState
import com.eyther.lumbridge.features.tools.recurringpayments.overview.viewmodel.IRecurringPaymentsOverviewScreenViewModel
import com.eyther.lumbridge.features.tools.recurringpayments.overview.viewmodel.RecurringPaymentsOverviewScreenViewModel
import com.eyther.lumbridge.model.recurringpayments.RecurringPaymentUi
import com.eyther.lumbridge.shared.time.extensions.toDayMonthYearDateString
import com.eyther.lumbridge.shared.time.extensions.toIsoLocalDateString
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
import java.util.Locale

@Composable
fun RecurringPaymentsOverviewScreen(
    navController: NavHostController,
    @StringRes label: Int,
    viewModel: IRecurringPaymentsOverviewScreenViewModel = hiltViewModel<RecurringPaymentsOverviewScreenViewModel>()
) {
    val viewState = viewModel.viewState.collectAsStateWithLifecycle().value
    val lifecycleOwner = LocalLifecycleOwner.current
    val recurringPaymentToDelete = remember { mutableLongStateOf(-1L) }

    LaunchedEffect(Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.viewEffects
                .onEach { viewEffects ->
                    when (viewEffects) {
                        else -> Unit
                    }
                }
                .collect()
        }
    }

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
                    if (recurringPaymentToDelete.longValue >= 0L) Modifier.blur(5.dp) else Modifier
                )
        ) {
            when (viewState) {
                is RecurringPaymentsOverviewScreenViewState.Loading -> LoadingIndicator()
                is RecurringPaymentsOverviewScreenViewState.Empty -> EmptyScreen(
                    onAddRecurringPayment = { navController.navigateToWithArgs(ToolsNavigationItem.RecurringPayments.Edit, -1L) }
                )

                is RecurringPaymentsOverviewScreenViewState.Content -> Content(
                    state = viewState,
                    navController = navController,
                    recurringPaymentToDelete = recurringPaymentToDelete,
                    onDeleteIconTapped = { recurringPaymentToDelete.longValue = it },
                    onDeleteRecurringPayment = { viewModel.deleteRecurringPayment(it.id) },
                    onEdit = { navController.navigateToWithArgs(ToolsNavigationItem.RecurringPayments.Edit, it) }
                )
            }
        }
    }
}

@Composable
private fun ColumnScope.Content(
    state: RecurringPaymentsOverviewScreenViewState.Content,
    navController: NavHostController,
    recurringPaymentToDelete: MutableLongState,
    onDeleteIconTapped: (Long) -> Unit,
    onDeleteRecurringPayment: (RecurringPaymentUi) -> Unit,
    onEdit: (Long) -> Unit
) {
    val context = LocalContext.current
    val jvmLocale = remember { Locale.getDefault() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = DefaultPadding)
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(HalfPadding)
        ) {
            itemsIndexed(state.recurringPayments) { index, recurringPayment ->
                RecurringPaymentItem(
                    recurringPayment = recurringPayment,
                    context = context,
                    supportedLocale = state.locale,
                    locale = jvmLocale,
                    onDelete = onDeleteIconTapped,
                    onEdit = onEdit
                )

                if (index == state.recurringPayments.lastIndex) {
                    Spacer(modifier = Modifier.height(DefaultPadding * 2 + 56.0.dp)) // 56dp is the height of the FAB
                }
            }
        }

        AddFab(
            modifier = Modifier.align(Alignment.BottomEnd),
            navController = navController
        )

        ShowDeleteConfirmationDialog(
            recurringPaymentUi = state.recurringPayments.find { it.id == recurringPaymentToDelete.longValue },
            recurringPaymentToDelete = recurringPaymentToDelete,
            onDeleteRecurringPayment = onDeleteRecurringPayment
        )
    }
}

@Composable
private fun RecurringPaymentItem(
    recurringPayment: RecurringPaymentUi,
    supportedLocale: SupportedLocales,
    context: Context,
    locale: Locale,
    onDelete: (Long) -> Unit,
    onEdit: (Long) -> Unit
) {
    ColumnCardWrapper {
        TabbedTextAndIcon(
            modifier = Modifier.padding(end = QuarterPadding),
            text = recurringPayment.label,
            textStyle = MaterialTheme.typography.bodyLarge,
            textColour = MaterialTheme.colorScheme.tertiary,
            icons = {
                Icon(
                    modifier = Modifier
                        .size(20.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(bounded = false),
                            onClick = { onDelete(recurringPayment.id) }
                        ),
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = stringResource(id = R.string.delete)
                )

                Spacer(Modifier.width(QuarterPadding))

                Icon(
                    modifier = Modifier
                        .size(20.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(bounded = false),
                            onClick = { onEdit(recurringPayment.id) }
                        ),
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = stringResource(id = R.string.edit)
                )
            }
        )

        TabbedDataOverview(
            modifier = Modifier.padding(top = HalfPadding),
            label = stringResource(id = R.string.recurring_payments_periodicity),
            text = recurringPayment.periodicity.getPeriodicityHumanReadable(locale).getString(context),
        )

        TabbedDataOverview(
            label = stringResource(id = R.string.recurring_payment_category),
            text = stringResource(recurringPayment.categoryTypesUi.categoryRes),
        )


        Spacer(modifier = Modifier.height(HalfPadding))

        TabbedDataOverview(
            label = stringResource(id = R.string.recurring_payments_next_due_date),
            text = recurringPayment.periodicity.nextDueDate?.toDayMonthYearDateString().orEmpty(),
        )

        TabbedDataOverview(
            label = stringResource(id = R.string.recurring_payment_amount),
            text = "${recurringPayment.amountToPay.forceTwoDecimalsPlaces()}${supportedLocale.getCurrencySymbol()}",
        )

        Spacer(modifier = Modifier.height(HalfPadding))

        TabbedDataOverview(
            label = stringResource(id = R.string.recurring_payment_notify_when_added),
            text = if (recurringPayment.shouldNotifyWhenPaid) {
                stringResource(id = R.string.yes)
            } else {
                stringResource(id = R.string.no)
            }
        )
    }
}

@Composable
private fun EmptyScreen(
    onAddRecurringPayment: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        EmptyScreenWithButton(
            modifier = Modifier.padding(DefaultPadding),
            text = stringResource(id = R.string.recurring_payments_empty_title),
            buttonText = stringResource(id = R.string.recurring_payment_add_button),
            icon = {
                Icon(
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(id = R.drawable.ic_repeat),
                    contentDescription = stringResource(id = R.string.recurring_payments_overview)
                )
            },
            onButtonClick = onAddRecurringPayment
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
            navController.navigateToWithArgs(ToolsNavigationItem.RecurringPayments.Edit, -1L)
        }
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_add),
            contentDescription = stringResource(
                id = R.string.recurring_payment_add_button
            )
        )
    }
}

@Composable
private fun ShowDeleteConfirmationDialog(
    recurringPaymentUi: RecurringPaymentUi?,
    recurringPaymentToDelete: MutableLongState,
    onDeleteRecurringPayment: (RecurringPaymentUi) -> Unit
) {
    if (recurringPaymentToDelete.longValue >= 0L && recurringPaymentUi != null) {
        AlertDialog(
            onDismissRequest = { recurringPaymentToDelete.longValue = -1L },
            confirmButton = {
                LumbridgeButton(
                    label = stringResource(id = R.string.yes),
                    onClick = {
                        onDeleteRecurringPayment(recurringPaymentUi)
                        recurringPaymentToDelete.longValue = -1L
                    }
                )
            },
            dismissButton = {
                LumbridgeButton(
                    label = stringResource(id = R.string.no),
                    onClick = { recurringPaymentToDelete.longValue = -1L }
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
                    text = stringResource(id = R.string.recurring_payment_delete_confirmation, recurringPaymentUi.label),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        )
    }
}
