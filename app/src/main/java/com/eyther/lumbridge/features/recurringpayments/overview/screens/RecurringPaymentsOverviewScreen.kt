package com.eyther.lumbridge.features.recurringpayments.overview.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.eyther.lumbridge.extensions.platform.navigateTo
import com.eyther.lumbridge.extensions.platform.navigateToWithArgs
import com.eyther.lumbridge.features.tools.navigation.ToolsNavigationItem
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.LumbridgeTopAppBar
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.TopAppBarVariation

@Composable
fun RecurringPaymentsOverviewScreen(
    navController: NavHostController,
    @StringRes label: Int,
) {
    val recurringPaymentToDelete = remember { mutableLongStateOf(-1L) }

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
            Text(
                modifier = Modifier.padding(16.dp)
                    .clickable(onClick = {
                        navController.navigateToWithArgs(ToolsNavigationItem.RecurringPayments.Edit, -1L)
                    }),
                text = "Recurring Payments Overview Screen, click me to navigate to Edit Recurring Payments Screen"
            )
        }
    }
}
