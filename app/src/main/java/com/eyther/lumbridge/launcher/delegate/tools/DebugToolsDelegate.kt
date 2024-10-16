package com.eyther.lumbridge.launcher.delegate.tools

import android.content.Context
import android.util.Log
import com.eyther.lumbridge.extensions.platform.isDebuggable
import com.eyther.lumbridge.shared.di.model.Schedulers
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * Debug tools delegate to setup debug tools for the application.
 *
 * @param context the application context, used to check if the application is running in debug mode
 * @param schedulers the injected schedulers, used for threading
 */
class DebugToolsDelegate @Inject constructor(
    @ApplicationContext private val context: Context,
    private val schedulers: Schedulers
) {

    companion object {
        private const val TAG = "DebugToolsDelegate"
    }

    /**
     * Initialize the debug tools. Take care not to make operations in the main thread.
     */
    suspend fun init() {
        if (context.isDebuggable()) {
            initTool { snoopFirebaseMessagingToken() }
        }
    }

    /**
     * Setup Firebase Messaging observer for debug builds, useful to test push notifications and
     * retrieve the FCM token to declare the device as a test device in the Firebase console.
     */
    private suspend fun snoopFirebaseMessagingToken() {
        val token = suspendCoroutine { continuation ->
            FirebaseMessaging.getInstance().token
                .addOnCompleteListener {
                    continuation.resume(it.result)
                }
                .addOnFailureListener {
                    continuation.resumeWithException(
                        Throwable("💥 Something went wrong while fetching the Firebase Messaging Token.", it)
                    )
                }
        }

        Log.d(TAG, "Firebase Messaging Token is: $token")
    }

    /**
     * Just a wrapper that ensures we're initializing the tool outside the main thread.
     *
     * @param coroutineContext the coroutine context to run the setup tool
     * @param setupTool the setup tool to run
     *
     * @throws IllegalStateException if the method is called from the main thread
     */
    private suspend fun initTool(
        coroutineContext: CoroutineContext = schedulers.io,
        setupTool: suspend (coroutineContext: CoroutineContext) -> Unit
    ) {
        if (coroutineContext == schedulers.main) {
            throw IllegalStateException("⚠️ You should not initialize debug tools in the main thread.")
        }

        setupTool(coroutineContext)
    }
}
