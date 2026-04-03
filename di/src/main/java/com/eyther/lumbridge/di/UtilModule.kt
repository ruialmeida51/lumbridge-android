package com.eyther.lumbridge.di

import android.content.Context
import com.eyther.lumbridge.data.di.AndroidFileReader
import com.eyther.lumbridge.data.di.ComplexGson
import com.eyther.lumbridge.data.di.DefaultGson
import com.eyther.lumbridge.data.input.IFileReader
import com.eyther.lumbridge.data.input.platform.AndroidFileReaderImpl
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

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

    @Provides
    @AndroidFileReader
    fun provideAndroidFileReader(
        @ApplicationContext context: Context
    ): IFileReader {
        return AndroidFileReaderImpl(context)
    }
}
