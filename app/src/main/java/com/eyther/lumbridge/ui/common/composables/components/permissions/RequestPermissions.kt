@file:OptIn(ExperimentalPermissionsApi::class)

package com.eyther.lumbridge.ui.common.composables.components.permissions

import android.app.Activity
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.eyther.lumbridge.extensions.platform.openAppSettings
import com.eyther.lumbridge.launcher.model.permissions.NeededPermission
import com.eyther.lumbridge.ui.common.composables.components.dialog.PermissionAlertDialog
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.shouldShowRationale

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun TryRequestPermission(
    neededPermission: NeededPermission,
    permissionState: PermissionState,
    askForNotificationsPermission: MutableState<Boolean>,
    onGranted: (Boolean) -> Unit
) {
    val activity = LocalContext.current as Activity
    val shouldShowPermissionDialog = remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted -> onGranted(isGranted) }
    )

    if (permissionState.status.isGranted) {
        activity.openAppSettings()
        askForNotificationsPermission.value = false
    } else {
        shouldShowPermissionDialog.value = true
    }

    if (shouldShowPermissionDialog.value) {
        PermissionAlertDialog(
            neededPermission = neededPermission,
            onDenyPermission = {
                shouldShowPermissionDialog.value = false
                askForNotificationsPermission.value = false
                onGranted(false)
            },
            onGrantPermission = {
                shouldShowPermissionDialog.value = false
                askForNotificationsPermission.value = false
                permissionLauncher.launch(neededPermission.permission)
            },
            onGoToAppSettings = {
                shouldShowPermissionDialog.value = false
                askForNotificationsPermission.value = false
                activity.openAppSettings()
            },
            isPermissionDeclined = permissionState.status.shouldShowRationale
        )
    }
}
