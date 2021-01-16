package com.ruialmeida.lumbridge.extensions.platform.coroutines

import com.ruialmeida.lumbridge.extensions.ExtConstants.THROTTLE_TIME
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.withContext

@FlowPreview
fun <T> Flow<T>.merge(vararg flows: Flow<T>): Flow<T> = flowOf(*flows).flattenMerge(concurrency = flows.size) //https://github.com/Kotlin/kotlinx.coroutines/issues/1491#issuecomment-527424128

fun <T> Flow<T>.throttle() = throttleFirst(THROTTLE_TIME)

fun <T> Flow<T>.launchOnIO() = launchIn(CoroutineScope(Dispatchers.IO))
fun <T> Flow<T>.launchOnMain() = launchIn(CoroutineScope(Dispatchers.Main))

fun <T> Flow<T>.flowOnIO() = flowOn(Dispatchers.IO)
fun <T> Flow<T>.flowOnMain() = flowOn(Dispatchers.Main)

suspend fun <T> Flow<T>.withIOContext(action: (() -> Unit)) = withContext(Dispatchers.IO) { action.invoke() }
suspend fun <T> Flow<T>.withMainContext(action: (() -> Unit)) = withContext(Dispatchers.Main) { action.invoke() }

fun <T> Flow<T>.throttleFirst(throttleTimeInMillis: Long): Flow<T> {
    return flow {
        var lastTime = 0L
        collect { value ->
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastTime >= throttleTimeInMillis) {
                lastTime = currentTime
                emit(value)
            }
        }
    }
}