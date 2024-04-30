package com.eyther.lumbridge.features.profile.screens

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eyther.lumbridge.R
import com.eyther.lumbridge.features.profile.model.ProfileScreenViewState
import com.eyther.lumbridge.features.profile.viewmodel.ProfileScreenViewModel
import com.eyther.lumbridge.launcher.viewmodel.MainActivityViewModel
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.LumbridgeTheme
import com.eyther.lumbridge.ui.theme.runescapeTypography

@Composable
@Preview
fun ProfileScreen(
    viewModel: ProfileScreenViewModel = hiltViewModel()
) {
    when(val state = viewModel.viewState.collectAsState().value) {
        is ProfileScreenViewState.Content -> Content(
            state = state,
            onDarkModeChange = viewModel::toggleDarkMode
        )
        is ProfileScreenViewState.Loading -> Unit
    }

}

@Composable
private fun Content(
    state: ProfileScreenViewState.Content,
    onDarkModeChange: (Boolean) -> Unit
) {
    Column {
        ProfileHeader()
        ProfileSettings(state, onDarkModeChange)
    }
}

@Composable
private fun ProfileHeader() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(DefaultPadding),
        shape = RoundedCornerShape(8),
    ) {
        Column(
            modifier = Modifier.padding(DefaultPadding)
        ) {
            Row {
                Icon(
                    imageVector = Icons.Rounded.AccountCircle,
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(80.dp)
                )

                Column(
                    modifier = Modifier
                        .padding(start = DefaultPadding)
                        .align(Alignment.CenterVertically),
                    verticalArrangement = Arrangement.spacedBy(DefaultPadding),
                ) {
                    Text(
                        text = "John Doe",
                        style = runescapeTypography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                    )

                    Text(
                        text = "eytherdeveloper@gmail.com",
                        style = runescapeTypography.bodyLarge
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileSettings(
    state: ProfileScreenViewState.Content,
    onDarkModeChange: (Boolean) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(DefaultPadding),
        shape = RoundedCornerShape(8),
    ) {

        Column(
            modifier = Modifier
                .padding(DefaultPadding),
            verticalArrangement = Arrangement.spacedBy(DefaultPadding)
        ) {
            SwitchSetting(
                icon = R.drawable.ic_sun,
                label = "Dark Mode",
                isChecked = state.isDarkModeEnabled ?: isSystemInDarkTheme(),
                onCheckedChange = { onDarkModeChange(it) }
            )
        }
    }
}

@Composable
private fun Setting(
    icon: Int? = null,
    vector: ImageVector? = null,
    label: String,
    option: @Composable () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(DefaultPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {

        icon?.let { icon ->
            Icon(
                painter = painterResource(id = icon),
                contentDescription = "$label Icon",
                modifier = Modifier.size(24.dp)
            )
        }

        vector?.let { vector ->
            Icon(
                imageVector = vector,
                contentDescription = "$label Icon",
                modifier = Modifier.size(24.dp)
            )
        }

        Text(
            text = label,
            style = runescapeTypography.bodyLarge
        )

        Spacer(Modifier.weight(1f))

        option()
    }
}

@Composable
private fun SwitchSetting(
    icon: Int? = null,
    vector: ImageVector? = null,
    label: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Setting(icon, vector, label) {
        Switch(
            modifier = Modifier.height(16.dp),
            checked = isChecked,
            onCheckedChange = onCheckedChange
        )
    }
}
