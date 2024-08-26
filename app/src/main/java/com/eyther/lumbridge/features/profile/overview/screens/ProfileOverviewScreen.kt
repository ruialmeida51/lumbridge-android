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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.eyther.lumbridge.R
import com.eyther.lumbridge.extensions.kotlin.capitalise
import com.eyther.lumbridge.features.profile.navigation.ProfileNavigationItem
import com.eyther.lumbridge.features.profile.overview.model.ProfileOverviewScreenViewState
import com.eyther.lumbridge.features.profile.overview.viewmodel.IProfileOverviewScreenViewModel
import com.eyther.lumbridge.features.profile.overview.viewmodel.ProfileOverviewScreenViewModel
import com.eyther.lumbridge.ui.common.composables.components.card.ColumnCardWrapper
import com.eyther.lumbridge.ui.common.composables.components.loading.LoadingIndicator
import com.eyther.lumbridge.ui.common.composables.components.setting.MovementSetting
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.LumbridgeTopAppBar
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.TopAppBarVariation
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.HalfPadding

private const val LOG_TAG = "ProfileOverviewScreen"

@Composable
fun ProfileOverviewScreen(
    navController: NavHostController,
    @StringRes label: Int,
    viewModel: IProfileOverviewScreenViewModel = hiltViewModel<ProfileOverviewScreenViewModel>()
) {
    val state = viewModel.viewState.collectAsStateWithLifecycle().value

    Scaffold(
        topBar = {
            LumbridgeTopAppBar(
                topAppBarVariation = TopAppBarVariation.Title(title = stringResource(id = label))
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
    state: ProfileOverviewScreenViewState.Content
) {
    Column {
        ProfileHeader(
            navController = navController,
            state = state,
            constructBitmap = viewModel::constructBitmapFromUri,
            saveImage = viewModel::saveImage,
            onNavigate = viewModel::navigate
        )

        Text(
            text = stringResource(id = R.string.profile_financial_settings),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(horizontal = DefaultPadding, vertical = HalfPadding)
        )

        ProfileFinancialSettings(navController, viewModel::navigate)

        Text(
            text = stringResource(id = R.string.profile_app_settings),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(horizontal = DefaultPadding, vertical = HalfPadding)
        )

        ProfileAppSettings(navController, viewModel::navigate)
    }
}

@Composable
private fun ProfileHeader(
    navController: NavHostController,
    state: ProfileOverviewScreenViewState.Content,
    constructBitmap: suspend (Uri, Context) -> Bitmap,
    saveImage: (Bitmap) -> Unit,
    onNavigate: (ProfileNavigationItem, NavController) -> Unit
) {
    val context = LocalContext.current
    var selectedImage by remember { mutableStateOf<Uri?>(null) }

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            selectedImage = it
        }
    )

    LaunchedEffect(selectedImage) {
        selectedImage?.let { nonNullUri ->
            saveImage(constructBitmap(nonNullUri, context))
        }
    }

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
                val usernameText = if (state.username != null) {
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

                state.email?.let {
                    Text(
                        text = it,
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
                        indication = rememberRipple(bounded = false)
                    ) {
                        onNavigate(
                            ProfileNavigationItem.EditProfile,
                            navController
                        )
                    },
                painter = painterResource(id = R.drawable.ic_edit),
                contentDescription = stringResource(id = R.string.edit_profile)
            )
        }
    }
}

@Composable
private fun ProfileFinancialSettings(
    navController: NavHostController,
    onNavigate: (ProfileNavigationItem, NavController) -> Unit
) {
    ColumnCardWrapper(
        verticalArrangement = Arrangement.spacedBy(DefaultPadding)
    ) {
        MovementSetting(
            icon = R.drawable.ic_savings,
            label = stringResource(id = R.string.edit_financial_profile),
            onClick = { onNavigate(ProfileNavigationItem.EditFinancialProfile, navController) }
        )

        MovementSetting(
            icon = R.drawable.ic_bank,
            label = stringResource(id = R.string.edit_mortgage_profile),
            onClick = { onNavigate(ProfileNavigationItem.EditMortgageProfile, navController) }
        )
    }
}

@Composable
private fun ProfileAppSettings(
    navController: NavHostController,
    onNavigate: (ProfileNavigationItem, NavController) -> Unit
) {
    ColumnCardWrapper {
        MovementSetting(
            icon = R.drawable.ic_settings,
            label = stringResource(id = R.string.settings),
            onClick = { onNavigate(ProfileNavigationItem.Settings, navController) }
        )
    }
}
