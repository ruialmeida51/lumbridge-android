package com.ruialmeida.lumbridgelibrary.extensions.bindings

import android.view.View
import com.ruialmeida.lumbridgelibrary.extensions.ExtConstants.THROTTLE_TIME
import com.ruialmeida.lumbridgelibrary.extensions.platform.coroutines.throttleFirst
import kotlinx.coroutines.flow.Flow
import reactivecircus.flowbinding.android.view.clicks

fun View.clicksThrottle(throttleTimeInMillis: Long = THROTTLE_TIME): Flow<Unit> = this.clicks().throttleFirst(throttleTimeInMillis)
