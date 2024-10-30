package com.eyther.lumbridge.extensions.platform

import android.content.Context
import android.content.Intent
import com.eyther.lumbridge.launcher.MainActivity

/**
 * Creates the intent that launches the main activity.
 */
fun Context.getLaunchMainActivityIntent(): Intent {
    return Intent(this, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
}
