package com.eyther.lumbridge.ui.common.composables.components.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.eyther.lumbridge.R
import com.eyther.lumbridge.launcher.model.permissions.NeededPermission
import com.eyther.lumbridge.ui.common.composables.components.buttons.LumbridgeButton
import com.eyther.lumbridge.ui.theme.DefaultPadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionAlertDialog(
    neededPermission: NeededPermission,
    isPermissionDeclined: Boolean,
    onGrantPermission: () -> Unit,
    onDenyPermission: () -> Unit,
    onGoToAppSettings: () -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = {
            onDenyPermission()
        },
        content = {
            Surface(
                shape = AlertDialogDefaults.shape,
                color = AlertDialogDefaults.containerColor
            ) {
                Column(
                    modifier = Modifier.padding(DefaultPadding),
                    verticalArrangement = Arrangement.spacedBy(DefaultPadding)
                ) {
                    Text(
                        text = stringResource(id = neededPermission.title),
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Text(
                        text = stringResource(
                            id = if (isPermissionDeclined) {
                                neededPermission.permanentlyDeniedDescription
                            } else {
                                neededPermission.description
                            }
                        ),
                        style = MaterialTheme.typography.bodySmall
                    )

                    LumbridgeButton(
                        label = stringResource(
                            id = if (isPermissionDeclined) {
                                R.string.permission_dialog_open_settings
                            } else {
                                R.string.allow
                            }
                        ),
                        onClick = if (isPermissionDeclined) {
                            onGoToAppSettings
                        } else {
                            onGrantPermission
                        }
                    )

                    LumbridgeButton(
                        label = stringResource(id = R.string.cancel),
                        onClick = onDenyPermission
                    )
                }

            }
        }
    )
}
