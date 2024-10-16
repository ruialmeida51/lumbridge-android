package com.eyther.lumbridge

import android.app.Application
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.os.StrictMode.VmPolicy
import com.eyther.lumbridge.extensions.platform.isDebuggable
import com.eyther.lumbridge.launcher.delegate.tools.DebugToolsDelegate
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class LumbridgeApplication : Application() {

    @Inject
    lateinit var debugToolsDelegate: DebugToolsDelegate

    override fun onCreate() {
        setupStrictMode()
        super.onCreate()
        tryStartDebugTools()
    }

    /**
     * Tries to start the debug tools. The [DebugToolsDelegate] is only started if the application is
     * running in debug mode, and that check is done inside the [DebugToolsDelegate] itself. Here,
     * we just call the init method and let the delegate handle the rest.
     */
    private fun tryStartDebugTools() {
        MainScope().launch {
            debugToolsDelegate.init()
        }
    }

    /**
     * Strict mode is used to catch accidental disk or network operations on the main thread and bring them to our
     * attention so we can fix them. This is useful for debugging and catching issues early on.
     *
     * This is only enabled in debug builds.
     */
    private fun setupStrictMode() {
        if (applicationContext.isDebuggable()) {
            StrictMode.setThreadPolicy(
                ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build()
            )

            StrictMode.setVmPolicy(
                VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .build()
            )
        }
    }
}
