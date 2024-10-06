package com.eyther.lumbridge.data.di

import com.eyther.lumbridge.data.datasource.currencyexchange.interceptor.CurrencyExchangeCacheInterceptor
import com.eyther.lumbridge.data.datasource.currencyexchange.service.CurrencyExchangeClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
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
        .client(
            OkHttpClient
                .Builder()
                .build()
        )
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    @CurrencyExchangeRetrofitClient
    fun provideCurrencyExchangeRetrofit(): Retrofit = Retrofit.Builder()
        .client(
            OkHttpClient
                .Builder()
                .addInterceptor(CurrencyExchangeCacheInterceptor())
                .build()
        )
        .baseUrl("https://hexarate.paikama.co/api/rates/latest/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideCurrencyExchangeClient(
        @CurrencyExchangeRetrofitClient retrofit: Retrofit
    ): CurrencyExchangeClient = retrofit.create(CurrencyExchangeClient::class.java)

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class AppRetrofitClient

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class CurrencyExchangeRetrofitClient
}