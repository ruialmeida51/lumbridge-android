package com.eyther.lumbridge.shared.di.model

import kotlinx.coroutines.CoroutineDispatcher

data class Schedulers(
    val io: CoroutineDispatcher,
    val main: CoroutineDispatcher,
    val cpu: CoroutineDispatcher
)
