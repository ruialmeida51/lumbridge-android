package com.eyther.lumbridge.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteDataModule {

    @Provides
    @Named("base_url")
    fun provideBaseUrl(): String = "TBA"

    @Provides
    @Singleton
    @AppRetrofitClient
    fun provideRetrofit(
        @Named("base_url") baseUrl: String
    ): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class AppRetrofitClient
}