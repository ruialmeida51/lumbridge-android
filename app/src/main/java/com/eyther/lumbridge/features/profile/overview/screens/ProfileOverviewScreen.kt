package com.eyther.lumbridge.features.profile.overview.screens

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.eyther.lumbridge.R
import com.eyther.lumbridge.extensions.kotlin.capitalise
import com.eyther.lumbridge.extensions.platform.SendEmailStatus
import com.eyther.lumbridge.extensions.platform.navigateTo
import com.eyther.lumbridge.extensions.platform.sendEmail
import com.eyther.lumbridge.features.profile.navigation.ProfileNavigationItem
import com.eyther.lumbridge.features.profile.overview.model.ProfileOverviewScreenViewState
import com.eyther.lumbridge.features.profile.overview.viewmodel.IProfileOverviewScreenViewModel
import com.eyther.lumbridge.features.profile.overview.viewmodel.ProfileOverviewScreenViewModel
import com.eyther.lumbridge.ui.common.composables.components.buttons.LumbridgeButton
import com.eyther.lumbridge.ui.common.composables.components.card.ColumnCardWrapper
import com.eyther.lumbridge.ui.common.composables.components.loading.LoadingIndicator
import com.eyther.lumbridge.ui.common.composables.components.setting.MovementSetting
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.LumbridgeTopAppBar
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.TopAppBarVariation
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.HalfPadding
import kotlinx.coroutines.launch

private const val LOG_TAG = "ProfileOverviewScreen"

@Composable
fun ProfileOverviewScreen(
    navController: NavHostController,
    @StringRes label: Int,
    viewModel: IProfileOverviewScreenViewModel = hiltViewModel<ProfileOverviewScreenViewModel>()
) {
    val state = viewModel.viewState.collectAsStateWithLifecycle().value
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            LumbridgeTopAppBar(
                topAppBarVariation = TopAppBarVariation.Title(title = stringResource(id = label))
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
                .fillMaxWidth()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            when (state) {
                is ProfileOverviewScreenViewState.Content -> Content(
                    navController = navController,
                    viewModel = viewModel,
                    snackbarHostState = snackbarHostState,
                    state = state
                )

                is ProfileOverviewScreenViewState.Loading -> LoadingIndicator()
            }
        }
    }
}

@Composable
private fun Content(
    viewModel: IProfileOverviewScreenViewModel,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    state: ProfileOverviewScreenViewState.Content
) {
    Column {
        Spacer(modifier = Modifier.height(DefaultPadding))

        ProfileHeader(
            navController = navController,
            state = state,
            constructBitmap = viewModel::constructBitmapFromUri,
            saveImage = viewModel::saveImage
        )

        Text(
            text = stringResource(id = R.string.profile_financial_settings),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(horizontal = DefaultPadding, vertical = HalfPadding)
        )

        ProfileFinancialSettings(navController)

        Text(
            text = stringResource(id = R.string.profile_app_settings),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(horizontal = DefaultPadding, vertical = HalfPadding)
        )

        ProfileAppSettings(navController)

        Text(
            text = stringResource(id = R.string.profile_support_the_app),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(horizontal = DefaultPadding, vertical = HalfPadding)
        )

        SupportAndCommunityHub(
            state = state,
            snackbarHostState = snackbarHostState
        )

        Spacer(modifier = Modifier.height(DefaultPadding))
    }
}

@Composable
private fun ProfileHeader(
    navController: NavHostController,
    state: ProfileOverviewScreenViewState.Content,
    constructBitmap: suspend (Uri, Context) -> Bitmap,
    saveImage: (Bitmap) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { imageUri ->
            imageUri?.let {
                coroutineScope.launch {
                    saveImage(constructBitmap(imageUri, context))
                }
            }
        }
    )

    ColumnCardWrapper(
        verticalArrangement = Arrangement.spacedBy(DefaultPadding)
    ) {
        Row {
            AsyncImage(
                model = state.imageBitmap,
                contentDescription = stringResource(id = R.string.profile_avatar_content_description),
                error = painterResource(id = R.drawable.ic_profile_circle),
                onError = {
                    Log.e(LOG_TAG, "Error loading image: $it")
                },
                modifier = Modifier
                    .clip(CircleShape)
                    .size(80.dp)
                    .clickable {
                        singlePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    },
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = DefaultPadding)
                    .align(Alignment.CenterVertically),
                verticalArrangement = Arrangement.spacedBy(DefaultPadding),
            ) {
                val usernameText = if (!state.username.isNullOrEmpty()) {
                    stringResource(
                        id = R.string.profile_greeting,
                        state.username
                    )
                } else {
                    stringResource(
                        id = R.string.profile_greeting_no_name
                    )
                }

                val localeText = if (state.locale != null) {
                    stringResource(
                        id = R.string.profile_greeting_locale,
                        state.locale.name.capitalise()
                    )
                } else {
                    stringResource(
                        id = R.string.profile_greeting_no_locale
                    )
                }

                Text(
                    text = usernameText,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )

                if (!state.email.isNullOrEmpty()) {
                    Text(
                        text = state.email,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Text(
                    text = localeText,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Icon(
                modifier = Modifier
                    .weight(0.2f)
                    .padding(start = HalfPadding)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(bounded = false)
                    ) {
                        navController.navigateTo(ProfileNavigationItem.EditProfile)
                    },
                painter = painterResource(id = R.drawable.ic_edit),
                contentDescription = stringResource(id = R.string.edit_profile)
            )
        }
    }
}

@Composable
private fun ProfileFinancialSettings(
    navController: NavHostController
) {
    ColumnCardWrapper(
        verticalArrangement = Arrangement.spacedBy(DefaultPadding)
    ) {
        MovementSetting(
            icon = R.drawable.ic_savings,
            label = stringResource(id = R.string.edit_financial_profile),
            onClick = { navController.navigateTo(ProfileNavigationItem.EditFinancialProfile) }
        )

        MovementSetting(
            icon = R.drawable.ic_bank,
            label = stringResource(id = R.string.profile_edit_loans),
            onClick = { navController.navigateTo(ProfileNavigationItem.Loans.List) }
        )
    }
}

@Composable
private fun ProfileAppSettings(
    navController: NavHostController
) {
    ColumnCardWrapper {
        MovementSetting(
            icon = R.drawable.ic_settings,
            label = stringResource(id = R.string.settings),
            onClick = { navController.navigateTo(ProfileNavigationItem.Settings) }
        )
    }
}

@Composable
private fun SupportAndCommunityHub(
    state: ProfileOverviewScreenViewState.Content,
    snackbarHostState: SnackbarHostState
) {
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val shouldShowDonationDialog = remember { mutableStateOf(false) }

    val emailAppNotFoundErrorMessage = stringResource(id = R.string.profile_support_feedback_email_error_no_clients)
    val emailUnexpectedErrorMessage = stringResource(id = R.string.profile_support_feedback_email_error_sending)

    ColumnCardWrapper(
        verticalArrangement = Arrangement.spacedBy(DefaultPadding)
    ) {
        MovementSetting(
            icon = R.drawable.ic_feedback,
            label = stringResource(id = R.string.profile_support_feedback),
            onClick = {
                val sendEmailResult = context.sendEmail(
                    emailTo = arrayOf(context.getString(R.string.developer_email)),
                    subject = if (state.username != null) {
                        context.getString(R.string.profile_support_feedback_email_subject, state.username)
                    } else {
                        context.getString(R.string.profile_support_feedback_email_subject_no_name)
                    },
                    body = context.getString(R.string.profile_support_feedback_email_body),
                    selectorTitle = context.getString(R.string.profile_support_feedback_email_selector)
                )

                coroutineScope.launch {
                    when (sendEmailResult) {
                        SendEmailStatus.NoEmailAppAvailable -> snackbarHostState.showSnackbar(emailAppNotFoundErrorMessage)
                        SendEmailStatus.UnexpectedError -> snackbarHostState.showSnackbar(emailUnexpectedErrorMessage)
                        else -> Unit
                    }
                }
            }
        )

        MovementSetting(
            icon = R.drawable.ic_code,
            label = stringResource(id = R.string.profile_support_source_code),
            onClick = { uriHandler.openUri(context.getString(R.string.source_code_url)) }
        )

        MovementSetting(
            icon = R.drawable.ic_bmc,
            label = stringResource(id = R.string.profile_support_donate),
            onClick = { shouldShowDonationDialog.value = true }
        )

        MovementSetting(
            icon = R.drawable.ic_discord,
            label = stringResource(id = R.string.profile_support_join_discord),
            onClick = { uriHandler.openUri(context.getString(R.string.discord_invite_url)) }
        )
    }

    DonationDialog(
        shouldShowDialog = shouldShowDonationDialog,
        uriHandler = uriHandler,
        context = context
    )
}

@Composable
fun DonationDialog(
    shouldShowDialog: MutableState<Boolean>,
    uriHandler: UriHandler,
    context: Context
) {
    if (shouldShowDialog.value) {
        AlertDialog(
            onDismissRequest = { shouldShowDialog.value = false },
            confirmButton = {
                LumbridgeButton(
                    label = stringResource(id = R.string.proceed),
                    onClick = {
                        uriHandler.openUri(context.getString(R.string.buy_me_a_coffee_url))
                        shouldShowDialog.value = false
                    }
                )
            },
            dismissButton = {
                LumbridgeButton(
                    label = stringResource(id = R.string.back),
                    onClick = { shouldShowDialog.value = false }
                )
            },
            title = {
                Text(
                    text = stringResource(id = R.string.profile_support_donate),
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            text = {
                Text(
                    text = stringResource(id = R.string.profile_support_donate_disclaimer),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        )
    }
}
