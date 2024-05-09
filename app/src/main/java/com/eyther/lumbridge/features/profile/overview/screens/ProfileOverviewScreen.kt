package com.eyther.lumbridge.features.profile.overview.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.eyther.lumbridge.R
import com.eyther.lumbridge.extensions.kotlin.capitalise
import com.eyther.lumbridge.features.profile.navigation.ProfileNavigationItem
import com.eyther.lumbridge.features.profile.overview.model.ProfileOverviewScreenViewState
import com.eyther.lumbridge.features.profile.overview.viewmodel.IProfileOverviewScreenViewModel
import com.eyther.lumbridge.features.profile.overview.viewmodel.ProfileOverviewScreenViewModel
import com.eyther.lumbridge.ui.common.composables.components.loading.LoadingIndicator
import com.eyther.lumbridge.ui.common.composables.components.setting.MovementSetting
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.LumbridgeTopAppBar
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.TopAppBarVariation
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.HalfPadding
import com.eyther.lumbridge.ui.theme.QuarterPadding
import com.eyther.lumbridge.ui.theme.runescapeTypography

@Composable
fun ProfileOverviewScreen(
    navController: NavHostController,
    @StringRes label: Int,
    viewModel: IProfileOverviewScreenViewModel = hiltViewModel<ProfileOverviewScreenViewModel>()
) {
    val state = viewModel.viewState.collectAsState().value

    Scaffold(
        topBar = {
            LumbridgeTopAppBar(
                topAppBarVariation = TopAppBarVariation.Title(title = stringResource(id = label))
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(DefaultPadding)
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
        ProfileHeader(navController, state, viewModel::navigate)

        Text(
            text = stringResource(id = R.string.profile_financial_settings),
            style = runescapeTypography.bodyLarge,
            modifier = Modifier.padding(vertical = HalfPadding)
        )

        ProfileFinancialSettings(navController, viewModel::navigate)

        Text(
            text = stringResource(id = R.string.profile_app_settings),
            style = runescapeTypography.bodyLarge,
            modifier = Modifier.padding(vertical = HalfPadding)
        )

        ProfileAppSettings(navController, viewModel::navigate)
    }
}

@Composable
private fun ProfileHeader(
    navController: NavHostController,
    state: ProfileOverviewScreenViewState.Content,
    onNavigate: (ProfileNavigationItem, NavController) -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .shadow(elevation = QuarterPadding)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(DefaultPadding)
    ) {
        Row {
            Icon(
                imageVector = Icons.Rounded.AccountCircle,
                contentDescription = stringResource(id = R.string.profile_avatar_content_description),
                modifier = Modifier
                    .clip(CircleShape)
                    .size(80.dp),
                tint = MaterialTheme.colorScheme.onSurface
            )

            Column(
                modifier = Modifier
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
                    style = runescapeTypography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )

                state.email?.let {
                    Text(
                        text = it,
                        style = runescapeTypography.bodyMedium
                    )
                }

                Text(
                    text = localeText,
                    style = runescapeTypography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                modifier = Modifier
                    .wrapContentSize()
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
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .shadow(elevation = QuarterPadding)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(DefaultPadding),
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
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .shadow(elevation = QuarterPadding)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(DefaultPadding),
        verticalArrangement = Arrangement.spacedBy(DefaultPadding)
    ) {
        MovementSetting(
            icon = R.drawable.ic_settings,
            label = stringResource(id = R.string.settings),
            onClick = { onNavigate(ProfileNavigationItem.Settings, navController) }
        )
    }
}
