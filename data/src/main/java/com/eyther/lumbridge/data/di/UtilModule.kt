package com.eyther.lumbridge.data.di

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UtilModule {
    @Provides
    fun provideGson(): Gson {
        return Gson()
    }
}
