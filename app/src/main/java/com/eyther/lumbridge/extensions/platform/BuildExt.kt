package com.eyther.lumbridge.extensions.platform

import android.content.Context
import android.content.pm.ApplicationInfo

/**
 * Check if the application is running in debug mode, regardless of the build type.
 *
 * This is useful, for example, if we want to enable debug tools on beta or release builds for testing
 * purposes. Always nice not to be tied to the debug build type.
 *
 * @return true if the application is running in debug mode, false otherwise.
 */
fun Context?.isDebuggable(): Boolean {
    return this?.let { applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0 } == true
}
