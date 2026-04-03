package com.eyther.lumbridge.launcher.model.permissions

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import com.eyther.lumbridge.R

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
sealed class NeededPermission(
    val permission: String,
    @StringRes val title: Int,
    @StringRes val description: Int,
    @StringRes val permanentlyDeniedDescription: Int
) {
    data object Notifications : NeededPermission(
        permission = android.Manifest.permission.POST_NOTIFICATIONS,
        title = R.string.permission_dialog_notification_title,
        description = R.string.permission_dialog_notification_message,
        permanentlyDeniedDescription = R.string.permission_dialog_notification_permanently_denied
    )
}
