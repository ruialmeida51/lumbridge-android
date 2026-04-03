package com.eyther.lumbridge.data.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultGson

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ComplexGson

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AndroidFileReader
