package com.eyther.lumbridge.data.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
object UtilModule {
    @Provides
    @DefaultGson
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @ComplexGson
    fun provideComplexGson(): Gson {
        return GsonBuilder()
            .enableComplexMapKeySerialization()
            .create()
    }

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class DefaultGson

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class ComplexGson
}
