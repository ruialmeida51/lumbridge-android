package com.eyther.lumbridge.data.extensions.okhttp

import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * This extension function allows us to use OkHttp's Call class with coroutines. It returns a
 * Response object, which we can use to get the response body, headers, etc.
 *
 * @return The response object from the OkHttp call.
 */
suspend fun Call.await(): Response {
    return suspendCancellableCoroutine { continuation ->
        enqueue(object : Callback {
            // If the call fails, we resume the coroutine with an exception.
            override fun onFailure(call: Call, e: IOException) = continuation.resumeWithException(e)

            // If the call is successful, we resume the coroutine with the response.
            override fun onResponse(call: Call, response: Response) = continuation.resume(response)
        })

        continuation.invokeOnCancellation {
            // If the coroutine is cancelled, we cancel the OkHttp call as well.
            cancel()
        }
    }
}
