package com.ruialmeida.lumbridge.extensions.bindings

import android.view.View
import com.ruialmeida.lumbridge.extensions.ExtConstants.THROTTLE_TIME
import com.ruialmeida.lumbridge.extensions.platform.coroutines.throttleFirst
import kotlinx.coroutines.flow.Flow
import reactivecircus.flowbinding.android.view.clicks

fun View.clicksThrottle(throttleTimeInMillis: Long = THROTTLE_TIME): Flow<Unit> = this.clicks().throttleFirst(throttleTimeInMillis)
